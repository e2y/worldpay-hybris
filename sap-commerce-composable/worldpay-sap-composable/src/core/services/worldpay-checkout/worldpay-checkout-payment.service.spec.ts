import createSpy = jasmine.createSpy;
import { APP_BASE_HREF, PlatformLocation } from '@angular/common';
import { inject, TestBed } from '@angular/core/testing';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { StoreModule } from '@ngrx/store';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
import { CheckoutPaymentDetailsCreatedEvent, CheckoutPaymentDetailsSetEvent, CheckoutQueryFacade } from '@spartacus/checkout/base/root';
import { Address, EventService, PaymentDetails, UserIdService } from '@spartacus/core';
import { Observable, of } from 'rxjs';
import { WorldpayCheckoutPaymentAdapter } from '../../connectors/worldpay-payment-connector/worldpay-checkout-payment.adapter';
import { WorldpayCheckoutPaymentConnector } from '../../connectors/worldpay-payment-connector/worldpay-checkout-payment.connector';
import { WorldpayAdapter } from '../../connectors/worldpay.adapter';
import { WorldpayConnector } from '../../connectors/worldpay.connector';
import { ThreeDsChallengeIframeUrlSetEvent, ThreeDsDDCIframeUrlSetEvent } from '../../events/checkout-payment.events';
import { WorldpayACHFacade } from '../../facade/worldpay-ach.facade';
import { PaymentMethod, ThreeDsDDCInfo } from '../../interfaces';
import { WorldpayCheckoutPaymentService } from './worldpay-checkout-payment.service';

describe('WorldpayCheckoutPaymentService', () => {
  let service: WorldpayCheckoutPaymentService;
  let userIdService: UserIdService;
  let activeCartService: ActiveCartFacade;
  let sanitizer: DomSanitizer;
  let router: Router;
  let platformLocation: PlatformLocation;
  let checkoutQueryFacade: CheckoutQueryFacade;
  let checkoutPaymentConnector: WorldpayCheckoutPaymentConnector;
  let worldpayConnector: WorldpayConnector;
  let worldpayACHFacade: WorldpayACHFacade;
  let eventService: EventService;
  let eventServiceSpy: jasmine.Spy;
  const userId: string = 'testUserId';
  const cartId: string = 'testCartId';

  class ActiveCartServiceStub {
    cartId = cartId;

    public takeActiveCartId() {
      return of(this.cartId);
    }

    isGuestCart() {
      return of(false);
    }
  }

  class UserIdServiceStub implements Partial<UserIdService> {
    takeUserId = createSpy('getUserId').and.returnValue(of(userId));
  }

  class MockPlatformLocation implements Partial<PlatformLocation> {
    getBaseHrefFromDOM = createSpy('getBaseHrefFromDOM').and.returnValue('/spartacus/');
  }

  class MockWorldpayAdapter implements Partial<WorldpayAdapter> {
    getPublicKey = createSpy().and.returnValue(of('pk'));

    setPaymentAddress(): Observable<Address> {
      return of({
        id: 'address-1',
        formattedAddress: '123 Test St, AA1 2BB'
      });
    }

    getDDC3dsJwt(): Observable<ThreeDsDDCInfo> {
      return of({
        jwt: 'jwt',
        ddcUrl: 'https://test.com'
      });
    }
  }

  const paymentDetails: PaymentDetails = {
    id: 'mockPaymentDetails'
  };

  const address: Address = {
    line1: '123 Test St',
    postalCode: 'AA1 2BB'
  };

  class DomSanitizerStub {
    public bypassSecurityTrustResourceUrl(url) {
      return url;
    }
  }

  class MockWorldpayCheckoutPaymentAdapter implements Partial<WorldpayCheckoutPaymentAdapter> {
    getPaymentCardTypes = createSpy().and.returnValue(of([]));
    createWorldpayPaymentDetails = createSpy().and.returnValue(of({ id: 'mockPaymentDetails' }));
    useExistingPaymentDetails = createSpy().and.returnValue(of({ id: 'mockPaymentDetails' }));
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        StoreModule.forRoot({}),
        RouterTestingModule,
      ],
      providers: [
        WorldpayCheckoutPaymentService,
        EventService,
        {
          provide: PlatformLocation,
          useClass: MockPlatformLocation
        },
        {
          provide: APP_BASE_HREF,
          useValue: '/spartacus/'
        },
        {
          provide: ActiveCartFacade,
          useClass: ActiveCartServiceStub
        },
        {
          provide: UserIdService,
          useClass: UserIdServiceStub
        },
        {
          provide: DomSanitizer,
          useClass: DomSanitizerStub
        },
        WorldpayCheckoutPaymentConnector,
        {
          provide: WorldpayCheckoutPaymentAdapter,
          useClass: MockWorldpayCheckoutPaymentAdapter
        },
        WorldpayConnector,
        {
          provide: WorldpayAdapter,
          useClass: MockWorldpayAdapter
        }
      ]
    });

    service = TestBed.inject(WorldpayCheckoutPaymentService);
    checkoutQueryFacade = TestBed.inject(CheckoutQueryFacade);
    activeCartService = TestBed.inject(ActiveCartFacade);
    userIdService = TestBed.inject(UserIdService);
    sanitizer = TestBed.inject(DomSanitizer);
    router = TestBed.inject(Router);
    checkoutPaymentConnector = TestBed.inject(WorldpayCheckoutPaymentConnector);
    worldpayConnector = TestBed.inject(WorldpayConnector);
    eventService = TestBed.inject(EventService);
    worldpayACHFacade = TestBed.inject(WorldpayACHFacade);

    window['Worldpay'] = {
      encrypt: () => 'dummyCseToken',
      setPublicKey: () => {
      }
    };
    spyOn(service, 'getPublicKeyFromState').and.returnValue(of('pk'));
    spyOn(service, 'generatePublicKey').and.callThrough();
    spyOn(service, 'generateCseToken').and.returnValue('dummyCseToken');
    spyOn(service, 'setCseToken').and.callThrough();
    spyOn(checkoutPaymentConnector, 'createWorldpayPaymentDetails').and.callThrough();
    eventServiceSpy = spyOn(eventService, 'dispatch').and.stub();
  });

  it('should inject WorldpayCheckoutPaymentService', inject(
    [WorldpayCheckoutPaymentService],
    (worldpayCheckoutPaymentService: WorldpayCheckoutPaymentService) => {
      expect(worldpayCheckoutPaymentService).toBeTruthy();
    }
  ));

  it('should be able to create payment details', () => {
    service.createPaymentDetails(paymentDetails).subscribe(response => response).unsubscribe();

    expect(service.generatePublicKey).toHaveBeenCalled();
    expect(service.generateCseToken).toHaveBeenCalledWith(paymentDetails);
    expect(service.setCseToken).toHaveBeenCalled();

    expect(checkoutPaymentConnector.createWorldpayPaymentDetails).toHaveBeenCalledWith(userId, cartId, paymentDetails, 'dummyCseToken');
    expect(eventService.dispatch).toHaveBeenCalledWith(
      {
        userId,
        cartId,
        paymentDetails
      },
      CheckoutPaymentDetailsCreatedEvent
    );
  });

  it('should be able to use existing payment details', () => {
    spyOn(checkoutPaymentConnector, 'useExistingPaymentDetails').and.callThrough();
    service.useExistingPaymentDetails(paymentDetails);
    expect(checkoutPaymentConnector.useExistingPaymentDetails).toHaveBeenCalledWith(userId, cartId, paymentDetails);
    checkoutPaymentConnector.useExistingPaymentDetails(userId, cartId, paymentDetails).subscribe(response => response).unsubscribe();
    expect(eventService.dispatch).toHaveBeenCalledWith(
      {
        userId: 'testUserId',
        cartId: 'testCartId',
        paymentDetailsId: 'mockPaymentDetails'
      }, CheckoutPaymentDetailsSetEvent);
  });

  it('should set billing address', () => {
    spyOn(worldpayConnector, 'setPaymentAddress').and.callThrough();
    let paymentAddress = null;
    service.setPaymentAddress(address).subscribe(response => paymentAddress = response).unsubscribe();
    expect(worldpayConnector.setPaymentAddress).toHaveBeenCalledWith('testUserId', 'testCartId', address);
    expect(paymentAddress).toEqual({
      id: 'address-1',
      formattedAddress: '123 Test St, AA1 2BB'
    });
  });

  it('should get the 3ds DDC iframe url', () => {
    const ddcUrl = '/ddc-iframe/action';
    const cardNumber = '4444333322221111';
    const jwt = 'some jwt data';
    const url = service.getSerializedUrl('worldpay-3ds-device-detection', {
      action: ddcUrl,
      bin: cardNumber,
      jwt
    });

    service.setThreeDsDDCIframeUrl(ddcUrl, cardNumber, jwt);
    expect(eventService.dispatch).toHaveBeenCalledWith(
      { threeDsDDCIframeUrl: '/spartacus/worldpay-3ds-device-detection?action=%2Fddc-iframe%2Faction&bin=4444333322221111&jwt=some%20jwt%20data' },
      ThreeDsDDCIframeUrlSetEvent
    );
  });

  it('should get the 3ds challenge iframe url', () => {
    spyOn(service, 'setThreeDsChallengeIframeUrl').and.callThrough();
    const challengeUrl = '/challenge-iframe/action';
    const merchantData = '111020020219';
    const jwt = 'some jwt data';
    const url = service.getSerializedUrl('worldpay-3ds-challenge', {
      action: challengeUrl,
      md: merchantData,
      jwt
    });

    service.setThreeDsChallengeIframeUrl(challengeUrl, jwt, merchantData);
    expect(eventService.dispatch).toHaveBeenCalledWith(
      { worldpayChallengeIframeUrl: '/spartacus/worldpay-3ds-challenge?action=%2Fchallenge-iframe%2Faction&md=111020020219&jwt=some%20jwt%20data' },
      ThreeDsChallengeIframeUrlSetEvent
    );
  });

  it('should update save credit card value', () => {
    let saveCard = false;
    service.getSaveCreditCardValueFromState().subscribe(response => saveCard = response).unsubscribe();
    expect(saveCard).toBeFalse();
    service.setSaveCreditCardValue(true);
    service.getSaveCreditCardValueFromState().subscribe(response => saveCard = response).unsubscribe();
    expect(saveCard).toBeTrue();
  });

  it('should return payment details state with saved and default flags', () => {
    spyOn(service, 'getSaveCreditCardValueFromState').and.returnValue(of(true));
    spyOn(service, 'getSaveAsDefaultCardValueFromState').and.returnValue(of(true));
    spyOn(worldpayACHFacade, 'getACHPaymentFormValue').and.returnValue(of(null));
    spyOn(checkoutQueryFacade, 'getCheckoutDetailsState').and.returnValue(of({
      loading: false,
      error: false,
      data: {
        paymentInfo: { id: '1' },
        worldpayAPMPaymentInfo: { apmCode: 'ACH' }
      }
    }));

    service.getPaymentDetailsState().subscribe(state => {
      expect(state.data.saved).toEqual(true);
      expect(state.data.defaultPayment).toEqual(true);
    });
  });

  it('should return payment details state with ACH payment form', () => {
    const achPaymentFormValue = { accountNumber: '123456789' };
    spyOn(service, 'getSaveCreditCardValueFromState').and.returnValue(of(false));
    spyOn(service, 'getSaveAsDefaultCardValueFromState').and.returnValue(of(false));
    spyOn(worldpayACHFacade, 'getACHPaymentFormValue').and.returnValue(of(achPaymentFormValue));
    spyOn(checkoutQueryFacade, 'getCheckoutDetailsState').and.returnValue(of({
      loading: false,
      error: false,
      data: {
        worldpayAPMPaymentInfo: { apmCode: PaymentMethod.ACH }
      }
    }));

    service.getPaymentDetailsState().subscribe(state => {
      expect(state.data.achPaymentForm).toEqual(achPaymentFormValue);
    });
  });

  it('should return payment details state without saved and default flags when no id', () => {
    spyOn(service, 'getSaveCreditCardValueFromState').and.returnValue(of(true));
    spyOn(service, 'getSaveAsDefaultCardValueFromState').and.returnValue(of(true));
    spyOn(worldpayACHFacade, 'getACHPaymentFormValue').and.returnValue(of(null));
    spyOn(checkoutQueryFacade, 'getCheckoutDetailsState').and.returnValue(of({
      loading: false,
      error: false,
      data: {
        paymentInfo: {},
        worldpayAPMPaymentInfo: { apmCode: 'ACH' }
      }
    }));

    service.getPaymentDetailsState().subscribe(state => {
      expect(state.data.saved).toBeUndefined();
      expect(state.data.defaultPayment).toBeUndefined();
    });
  });

});

