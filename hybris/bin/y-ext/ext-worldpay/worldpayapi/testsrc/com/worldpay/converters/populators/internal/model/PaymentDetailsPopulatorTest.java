package com.worldpay.converters.populators.internal.model;

import com.worldpay.converters.internal.model.payment.PaymentConverterStrategy;
import com.worldpay.enums.PaymentAction;
import com.worldpay.internal.model.PaResponse;
import com.worldpay.data.PaymentDetails;
import com.worldpay.data.Session;
import com.worldpay.data.payment.Payment;
import com.worldpay.data.payment.StoredCredentials;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PaymentDetailsPopulatorTest {

    private static final String AUTHORISE = "AUTHORISE";
    private static final String PA_RESPONSE = "paResponse";

    @InjectMocks
    private PaymentDetailsPopulator testObj;

    @Mock
    private Converter<Session, com.worldpay.internal.model.Session> internalSessionConverterMock;
    @Mock
    private Converter<StoredCredentials, com.worldpay.internal.model.StoredCredentials> internalStoredCredentialsConverterMock;
    @Mock
    private PaymentConverterStrategy internalPaymentConverterStrategyMock;

    @Mock
    private PaymentDetails sourceMock;
    @Mock
    private Payment paymentMock;
    @Mock
    private Object intPaymentMock;
    @Mock
    private Session sessionMock;
    @Mock
    private com.worldpay.internal.model.Session intSessionMock;
    @Mock
    private StoredCredentials storedCredentialsMock;
    @Mock
    private com.worldpay.internal.model.StoredCredentials intStoredCredentials;

    @Before
    public void setUp() {
        testObj = new PaymentDetailsPopulator(internalSessionConverterMock, internalStoredCredentialsConverterMock, internalPaymentConverterStrategyMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void populate_WhenSourceIsNull_ShouldThrowAnException() {
        testObj.populate(null, new com.worldpay.internal.model.PaymentDetails());
    }

    @Test(expected = IllegalArgumentException.class)
    public void populate_WhenTargetIsNull_ShouldThrowAnException() {
        testObj.populate(sourceMock, null);
    }

    @Test
    public void populate_WhenGetPaymentIsNull_ShouldNotPopulatePayment() {
        when(sourceMock.getPayment()).thenReturn(null);

        final com.worldpay.internal.model.PaymentDetails target = new com.worldpay.internal.model.PaymentDetails();
        testObj.populate(sourceMock, target);

        assertThat(target.getVISASSLOrECMCSSLOrBHSSSLOrNEWDAYSSLOrIKEASSLOrAMEXSSLOrELVSSLOrSEPADIRECTDEBITSSLOrDINERSSSLOrCBSSLOrAIRPLUSSSLOrUATPSSLOrCARTEBLEUESSLOrSOLOGBSSLOrLASERSSLOrDANKORTSSLOrDISCOVERSSLOrJCBSSLOrAURORESSLOrGECAPITALSSLOrHIPERCARDSSLOrSOROCREDSSLOrELOSSLOrCARNETSSLOrARGENCARDSSLOrCABALSSLOrCENCOSUDSSLOrCOOPEPLUSSSLOrCREDIMASSSLOrITALCREDSSLOrNARANJASSLOrNATIVASSLOrNEVADASSLOrNEXOSSLOrTARJETASHOPPINGSSLOrPERMANENTSIGNEDDDNLFAXOrSINGLEUNSIGNEDDDNLSSLOrSINGLEUNSIGNEDDDESSSLOrSINGLEUNSIGNEDDDFRSSLOrPERMANENTSIGNEDDDGBSSLOrPERMANENTUNSIGNEDDDGBSSLOrPAYOUTBANKOrVISACHECKOUTSSLOrPAYPALEXPRESSOrGIROPAYSSLOrMAESTROSSLOrSWITCHSSLOrNCPB2BSSLOrNCPSEASONSSLOrNCPGMMSSLOrIDEALSSLOrACHDIRECTDEBITSSLOrCARDSSLOrABAQOOSSSLOrAGMOSSLOrALIPAYSSLOrALIPAYMOBILESSLOrBALOTOSSLOrBANKAXESSSSLOrBANKLINKNORDEASSLOrBILLDESKSSLOrBILLINGPARTNERSSLOrCASHUSSLOrDINEROMAIL7ELEVENSSLOrDINEROMAILOXXOSSLOrDINEROMAILONLINEBTSSLOrDINEROMAILSERVIPAGSSLOrEKONTOSSLOrEPAYSSLOrEUTELLERSSLOrEWIREDKSSLOrEWIRENOSSLOrEWIRESESSLOrHALCASHSSLOrINSTADEBITSSLOrKONBINISSLOrLOBANETARSSLOrLOBANETBRSSLOrLOBANETCLSSLOrLOBANETMXSSLOrLOBANETPESSLOrLOBANETUYSSLOrMISTERCASHSSLOrMULTIBANCOSSLOrNEOSURFSSLOrPAGASSLOrPAGAVERVESSLOrPAYSAFECARDSSLOrPAYUSSLOrPLUSPAYSSLOrPOLISSLOrPOLINZSSLOrPOSTEPAYSSLOrPRZELEWYSSLOrQIWISSLOrSAFETYPAYSSLOrSIDSSLOrSKRILLSSLOrSOFORTSSLOrSOFORTCHSSLOrSPEEDCARDSSLOrSPOROPAYSSLOrSWIFFSSLOrTELEINGRESOSSLOrTICKETSURFSSLOrTRUSTLYSSLOrTRUSTPAYCZSSLOrTRUSTPAYEESSLOrTRUSTPAYSKSSLOrWEBMONEYSSLOrYANDEXMONEYSSLOrASTROPAYCARDSSLOrBANCOSANTANDERSSLOrBOLETOSSLOrBOLETOHTMLOrMONETASSLOrTODITOCARDSSLOrONLINETRANSFERBRSSLOrONLINETRANSFERMYSSLOrONLINETRANSFERTHSSLOrONLINETRANSFERVNSSLOrOPENBANKINGSSLOrSEVENELEVENMYSSLOrPETRONASSSLOrENETSSGSSLOrCASHTHSSLOrATMIDSSLOrTOKENSSLOrCHINAUNIONPAYSSLOrENVOYTRANSFERAUDBANKOrENVOYTRANSFERCADBANKOrENVOYTRANSFERCHFBANKOrENVOYTRANSFERCZKBANKOrENVOYTRANSFERDKKBANKOrENVOYTRANSFEREURBANKOrENVOYTRANSFERGBPBANKOrENVOYTRANSFERHKDBANKOrENVOYTRANSFERHUFBANKOrENVOYTRANSFERJPYBANKOrENVOYTRANSFERNOKBANKOrENVOYTRANSFERNZDBANKOrENVOYTRANSFERPLNBANKOrENVOYTRANSFERRUBBANKOrENVOYTRANSFERSEKBANKOrENVOYTRANSFERSGDBANKOrENVOYTRANSFERTHBBANKOrENVOYTRANSFERTRYBANKOrENVOYTRANSFERUSDBANKOrENVOYTRANSFERZARBANKOrTRANSFERATBANKOrTRANSFERBEBANKOrTRANSFERCHBANKOrTRANSFERDEBANKOrTRANSFERDKBANKOrTRANSFERESBANKOrTRANSFERFIBANKOrTRANSFERFRBANKOrTRANSFERGBBANKOrTRANSFERGRBANKOrTRANSFERITBANKOrTRANSFERJPBANKOrTRANSFERLUBANKOrTRANSFERNLBANKOrTRANSFERNOBANKOrTRANSFERPLBANKOrTRANSFERSEBANKOrTRANSFERUSBANKOrEMVCOTOKENSSLOrAPPLEPAYSSLOrANDROIDPAYSSLOrSAMSUNGPAYSSLOrPAYWITHGOOGLESSLOrCLICKTOPAYSSLOrKLARNASSLOrKLARNAPAYLATERSSLOrKLARNAPAYNOWSSLOrKLARNASLICEITSSLOrKLARNAV2SSLOrWECHATPAYSSLOrBILLKEYSSLOrINIPAYSSLOrWEBPAYSSLOrPBBASSLOrAPMOrMERCADOPAGOSSLOrPAYPALSSLOrFPXSSLOrAFTERPAYSSLOrCLEARPAYSSLOrALIPAYHKSSLOrGRABPAYSSLOrMAESSLOrTRUEMONEYSSLOrBANKTRANSFERSSLOrTOUCHNGOSSLOrBOOSTSSLOrLINEPAYSSLOrPROMPTPAYSSLOrALIPAYCNSSLOrSVSGIFTCARDSSLOrKAKAOPAYSSLOrTROYSSLOrUPISSLOrUPEXSSLOrFFDISBURSESSLOrFFMONEYTRANSFERSSLOrFFHPPSSLOrEPSENVSSLOrMYBANKSSLOrSATISPAYSSLOrBANCOMATPAYSSLOrBLIKSSLOrPAYBYBANKAUSSLOrOXXOSSLOrCardNumberOrExpiryDateOrCardHolderNameOrCvcOrIssueNumberOrStartDateOrPOSRequestOrCardSwipeOrCSEDATA()).isEmpty();
    }

    @Test
    public void populate_WhenGetSessionIsNull_ShouldNotPopulateSession() {
        when(sourceMock.getSession()).thenReturn(null);

        final com.worldpay.internal.model.PaymentDetails target = new com.worldpay.internal.model.PaymentDetails();
        testObj.populate(sourceMock, target);

        assertThat(target.getSession()).isNull();
    }

    @Test
    public void populate_WhenGetPaResponseIsNull_ShouldNotPopulateInfo3DSecure() {
        when(sourceMock.getPaResponse()).thenReturn(null);

        final com.worldpay.internal.model.PaymentDetails target = new com.worldpay.internal.model.PaymentDetails();
        testObj.populate(sourceMock, target);

        assertThat(target.getInfo3DSecure()).isNull();
    }

    @Test
    public void populate_WhenGetStoredCredentialsIsNull_ShouldNotPopulateStoredCredentials() {
        when(sourceMock.getStoredCredentials()).thenReturn(null);

        final com.worldpay.internal.model.PaymentDetails target = new com.worldpay.internal.model.PaymentDetails();
        testObj.populate(sourceMock, target);

        assertThat(target.getStoredCredentials()).isNull();
    }

    @Test
    public void populate_WhenGetActionIsNull_ShouldNotPopulateAction() {
        when(sourceMock.getAction()).thenReturn(null);

        final com.worldpay.internal.model.PaymentDetails target = new com.worldpay.internal.model.PaymentDetails();
        testObj.populate(sourceMock, target);

        assertThat(target.getAction()).isEqualTo(AUTHORISE);
    }

    @Test
    public void populate_WhenSourceAndTargetAreNotNullAndAllTheFieldsAreNotNull_ShouldPopulate() {
        when(sourceMock.getPayment()).thenReturn(paymentMock);
        when(sourceMock.getSession()).thenReturn(sessionMock);
        when(sourceMock.getPaResponse()).thenReturn(PA_RESPONSE);
        when(sourceMock.getStoredCredentials()).thenReturn(storedCredentialsMock);
        when(sourceMock.getAction()).thenReturn(PaymentAction.AUTHORISE);

        when(internalPaymentConverterStrategyMock.convertPayment(paymentMock)).thenReturn(intPaymentMock);
        when(internalSessionConverterMock.convert(sessionMock)).thenReturn(intSessionMock);
        when(internalStoredCredentialsConverterMock.convert(storedCredentialsMock)).thenReturn(intStoredCredentials);

        final com.worldpay.internal.model.PaymentDetails target = new com.worldpay.internal.model.PaymentDetails();
        testObj.populate(sourceMock, target);

        assertThat(target.getVISASSLOrECMCSSLOrBHSSSLOrNEWDAYSSLOrIKEASSLOrAMEXSSLOrELVSSLOrSEPADIRECTDEBITSSLOrDINERSSSLOrCBSSLOrAIRPLUSSSLOrUATPSSLOrCARTEBLEUESSLOrSOLOGBSSLOrLASERSSLOrDANKORTSSLOrDISCOVERSSLOrJCBSSLOrAURORESSLOrGECAPITALSSLOrHIPERCARDSSLOrSOROCREDSSLOrELOSSLOrCARNETSSLOrARGENCARDSSLOrCABALSSLOrCENCOSUDSSLOrCOOPEPLUSSSLOrCREDIMASSSLOrITALCREDSSLOrNARANJASSLOrNATIVASSLOrNEVADASSLOrNEXOSSLOrTARJETASHOPPINGSSLOrPERMANENTSIGNEDDDNLFAXOrSINGLEUNSIGNEDDDNLSSLOrSINGLEUNSIGNEDDDESSSLOrSINGLEUNSIGNEDDDFRSSLOrPERMANENTSIGNEDDDGBSSLOrPERMANENTUNSIGNEDDDGBSSLOrPAYOUTBANKOrVISACHECKOUTSSLOrPAYPALEXPRESSOrGIROPAYSSLOrMAESTROSSLOrSWITCHSSLOrNCPB2BSSLOrNCPSEASONSSLOrNCPGMMSSLOrIDEALSSLOrACHDIRECTDEBITSSLOrCARDSSLOrABAQOOSSSLOrAGMOSSLOrALIPAYSSLOrALIPAYMOBILESSLOrBALOTOSSLOrBANKAXESSSSLOrBANKLINKNORDEASSLOrBILLDESKSSLOrBILLINGPARTNERSSLOrCASHUSSLOrDINEROMAIL7ELEVENSSLOrDINEROMAILOXXOSSLOrDINEROMAILONLINEBTSSLOrDINEROMAILSERVIPAGSSLOrEKONTOSSLOrEPAYSSLOrEUTELLERSSLOrEWIREDKSSLOrEWIRENOSSLOrEWIRESESSLOrHALCASHSSLOrINSTADEBITSSLOrKONBINISSLOrLOBANETARSSLOrLOBANETBRSSLOrLOBANETCLSSLOrLOBANETMXSSLOrLOBANETPESSLOrLOBANETUYSSLOrMISTERCASHSSLOrMULTIBANCOSSLOrNEOSURFSSLOrPAGASSLOrPAGAVERVESSLOrPAYSAFECARDSSLOrPAYUSSLOrPLUSPAYSSLOrPOLISSLOrPOLINZSSLOrPOSTEPAYSSLOrPRZELEWYSSLOrQIWISSLOrSAFETYPAYSSLOrSIDSSLOrSKRILLSSLOrSOFORTSSLOrSOFORTCHSSLOrSPEEDCARDSSLOrSPOROPAYSSLOrSWIFFSSLOrTELEINGRESOSSLOrTICKETSURFSSLOrTRUSTLYSSLOrTRUSTPAYCZSSLOrTRUSTPAYEESSLOrTRUSTPAYSKSSLOrWEBMONEYSSLOrYANDEXMONEYSSLOrASTROPAYCARDSSLOrBANCOSANTANDERSSLOrBOLETOSSLOrBOLETOHTMLOrMONETASSLOrTODITOCARDSSLOrONLINETRANSFERBRSSLOrONLINETRANSFERMYSSLOrONLINETRANSFERTHSSLOrONLINETRANSFERVNSSLOrOPENBANKINGSSLOrSEVENELEVENMYSSLOrPETRONASSSLOrENETSSGSSLOrCASHTHSSLOrATMIDSSLOrTOKENSSLOrCHINAUNIONPAYSSLOrENVOYTRANSFERAUDBANKOrENVOYTRANSFERCADBANKOrENVOYTRANSFERCHFBANKOrENVOYTRANSFERCZKBANKOrENVOYTRANSFERDKKBANKOrENVOYTRANSFEREURBANKOrENVOYTRANSFERGBPBANKOrENVOYTRANSFERHKDBANKOrENVOYTRANSFERHUFBANKOrENVOYTRANSFERJPYBANKOrENVOYTRANSFERNOKBANKOrENVOYTRANSFERNZDBANKOrENVOYTRANSFERPLNBANKOrENVOYTRANSFERRUBBANKOrENVOYTRANSFERSEKBANKOrENVOYTRANSFERSGDBANKOrENVOYTRANSFERTHBBANKOrENVOYTRANSFERTRYBANKOrENVOYTRANSFERUSDBANKOrENVOYTRANSFERZARBANKOrTRANSFERATBANKOrTRANSFERBEBANKOrTRANSFERCHBANKOrTRANSFERDEBANKOrTRANSFERDKBANKOrTRANSFERESBANKOrTRANSFERFIBANKOrTRANSFERFRBANKOrTRANSFERGBBANKOrTRANSFERGRBANKOrTRANSFERITBANKOrTRANSFERJPBANKOrTRANSFERLUBANKOrTRANSFERNLBANKOrTRANSFERNOBANKOrTRANSFERPLBANKOrTRANSFERSEBANKOrTRANSFERUSBANKOrEMVCOTOKENSSLOrAPPLEPAYSSLOrANDROIDPAYSSLOrSAMSUNGPAYSSLOrPAYWITHGOOGLESSLOrCLICKTOPAYSSLOrKLARNASSLOrKLARNAPAYLATERSSLOrKLARNAPAYNOWSSLOrKLARNASLICEITSSLOrKLARNAV2SSLOrWECHATPAYSSLOrBILLKEYSSLOrINIPAYSSLOrWEBPAYSSLOrPBBASSLOrAPMOrMERCADOPAGOSSLOrPAYPALSSLOrFPXSSLOrAFTERPAYSSLOrCLEARPAYSSLOrALIPAYHKSSLOrGRABPAYSSLOrMAESSLOrTRUEMONEYSSLOrBANKTRANSFERSSLOrTOUCHNGOSSLOrBOOSTSSLOrLINEPAYSSLOrPROMPTPAYSSLOrALIPAYCNSSLOrSVSGIFTCARDSSLOrKAKAOPAYSSLOrTROYSSLOrUPISSLOrUPEXSSLOrFFDISBURSESSLOrFFMONEYTRANSFERSSLOrFFHPPSSLOrEPSENVSSLOrMYBANKSSLOrSATISPAYSSLOrBANCOMATPAYSSLOrBLIKSSLOrPAYBYBANKAUSSLOrOXXOSSLOrCardNumberOrExpiryDateOrCardHolderNameOrCvcOrIssueNumberOrStartDateOrPOSRequestOrCardSwipeOrCSEDATA().get(0))
            .isEqualTo(intPaymentMock);
        assertThat(target.getSession()).isEqualTo(intSessionMock);
        assertThat(((PaResponse) target.getInfo3DSecure().getPaResponseOrMpiProviderOrMpiResponseOrAttemptedAuthenticationOrCompletedAuthenticationOrThreeDSVersionOrMerchantNameOrXidOrDsTransactionIdOrCavvOrEciOrThreeRIOrDelegatedAuthenticationOrTransactionStatusReasonOrChallengeCancelIndicatorOrNetworkScoreOrCardBrandOrCavvAlgorithm().get(0)).getvalue())
            .isEqualTo(PA_RESPONSE);
        assertThat(target.getStoredCredentials()).isEqualTo(intStoredCredentials);
        assertThat(target.getAction()).isEqualTo(PaymentAction.AUTHORISE.name());
    }
}
