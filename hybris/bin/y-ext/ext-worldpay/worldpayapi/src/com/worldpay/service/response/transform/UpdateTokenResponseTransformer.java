package com.worldpay.service.response.transform;

import com.worldpay.exception.WorldpayModelTransformationException;
import com.worldpay.internal.model.Ok;
import com.worldpay.internal.model.PaymentService;
import com.worldpay.internal.model.Reply;
import com.worldpay.internal.model.UpdateTokenReceived;
import com.worldpay.service.response.ServiceResponse;
import com.worldpay.service.response.UpdateTokenResponse;

public class UpdateTokenResponseTransformer extends AbstractServiceResponseTransformer {

    @Override
    public ServiceResponse transform(final PaymentService paymentService) throws WorldpayModelTransformationException {

        final Object responseType = paymentService.getSubmitOrModifyOrInquiryOrReplyOrNotifyOrVerify().get(0);
        if (responseType == null) {
            throw new WorldpayModelTransformationException("No reply message in Worldpay update token response");
        }
        if (!(responseType instanceof Reply)) {
            throw new WorldpayModelTransformationException("Reply type from Worldpay not the expected type");
        }

        final Reply intReply = (Reply) responseType;
        final ServiceResponseTransformerHelper responseTransformerHelper = ServiceResponseTransformerHelper.getInstance();

        final Object response = intReply.getOrderStatusOrBatchStatusOrErrorOrAddressCheckResponseOrRefundableAmountOrAccountBatchOrShopperOrOkOrFuturePayAgreementStatusOrShopperAuthenticationResultOrFuturePayPaymentResultOrPricePointOrPaymentOptionOrToken().get(0);

        final UpdateTokenResponse updateTokenResponse = new UpdateTokenResponse();
        if (responseTransformerHelper.checkForError(updateTokenResponse, intReply)) {
            return updateTokenResponse;
        }
        if (!(response instanceof Ok)) {
            throw new WorldpayModelTransformationException("UpdateTokenResponse did not contain an OK object");
        }
        final Ok okResponse = (Ok) response;
        final Object updateTokenReceived = okResponse.getCancelReceivedOrVoidReceivedOrCaptureReceivedOrRevokeReceivedOrRefundReceivedOrBackofficeCodeReceivedOrAuthorisationCodeReceivedOrDefenceReceivedOrUpdateTokenReceivedOrDeleteTokenReceivedOrExtendExpiryDateReceivedOrOrderReceived().get(0);
        if (updateTokenReceived instanceof UpdateTokenReceived) {
            updateTokenResponse.setUpdateTokenReply(responseTransformerHelper.buildUpdateTokenReply((UpdateTokenReceived) updateTokenReceived));
        }
        return updateTokenResponse;
    }
}
