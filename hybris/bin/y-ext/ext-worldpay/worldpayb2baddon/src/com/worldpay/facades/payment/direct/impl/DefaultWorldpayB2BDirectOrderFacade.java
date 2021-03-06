package com.worldpay.facades.payment.direct.impl;

import com.worldpay.core.services.WorldpayCartService;
import com.worldpay.core.services.WorldpayPaymentInfoService;
import com.worldpay.exception.WorldpayException;
import com.worldpay.facades.payment.direct.WorldpayB2BDirectOrderFacade;
import com.worldpay.facades.payment.merchant.WorldpayMerchantConfigDataFacade;
import com.worldpay.merchant.WorldpayMerchantInfoService;
import com.worldpay.order.data.WorldpayAdditionalInfoData;
import com.worldpay.payment.DirectResponseData;
import com.worldpay.payment.TransactionStatus;
import com.worldpay.service.payment.WorldpayDirectOrderService;
import com.worldpay.strategy.WorldpayAuthenticatedShopperIdStrategy;
import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.WorldpayAPMPaymentInfoModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;

/**
 * Implementation of the authorise operations that enables the Client Side Encryption with Worldpay
 */
@SuppressWarnings("java:S107")
public class DefaultWorldpayB2BDirectOrderFacade extends DefaultWorldpayDirectOrderFacade implements WorldpayB2BDirectOrderFacade {

    private final B2BOrderService b2BOrderService;

    public DefaultWorldpayB2BDirectOrderFacade(final WorldpayAuthenticatedShopperIdStrategy worldpayAuthenticatedShopperIdStrategy,
                                               final WorldpayDirectOrderService worldpayDirectOrderService,
                                               final CartService cartService, AcceleratorCheckoutFacade acceleratorCheckoutFacade,
                                               final WorldpayPaymentInfoService worldpayPaymentInfoService,
                                               final WorldpayMerchantConfigDataFacade worldpayMerchantConfigDataFacade,
                                               final CartFacade cartFacade,
                                               final WorldpayCartService worldpayCartService,
                                               final WorldpayMerchantInfoService worldpayMerchantInfoService, final B2BOrderService b2BOrderService,
                                               final Populator<WorldpayAPMPaymentInfoModel, CCPaymentInfoData> apmPaymentInfoPopulator) {
        super(worldpayAuthenticatedShopperIdStrategy, worldpayDirectOrderService, cartService, acceleratorCheckoutFacade, worldpayPaymentInfoService, worldpayMerchantConfigDataFacade, cartFacade, worldpayCartService, worldpayMerchantInfoService, apmPaymentInfoPopulator);
        this.b2BOrderService = b2BOrderService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectResponseData authoriseRecurringPayment(final String orderCode,
                                                        final WorldpayAdditionalInfoData worldpayAdditionalInfoData) throws WorldpayException, InvalidCartException {
        final AbstractOrderModel abstractOrderModel = b2BOrderService.getOrderForCode(orderCode);
        return internalAuthoriseRecurringPayment(abstractOrderModel, worldpayAdditionalInfoData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectResponseData authorise3DSecureOnOrder(final String orderCode,
                                                       final String paResponse,
                                                       final WorldpayAdditionalInfoData worldpayAdditionalInfoData) throws WorldpayException, InvalidCartException {
        final OrderModel orderModel = b2BOrderService.getOrderForCode(orderCode);
        return internalAuthorise3DSecure(orderModel, paResponse, worldpayAdditionalInfoData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleAuthorisedResponse(final DirectResponseData response) {
        response.setTransactionStatus(TransactionStatus.AUTHORISED);
    }

}
