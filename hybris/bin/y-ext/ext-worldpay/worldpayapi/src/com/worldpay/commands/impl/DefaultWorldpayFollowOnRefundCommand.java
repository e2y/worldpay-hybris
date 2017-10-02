
package com.worldpay.commands.impl;

import com.worldpay.exception.WorldpayException;
import com.worldpay.service.model.Amount;
import com.worldpay.service.model.MerchantInfo;
import com.worldpay.service.request.RefundServiceRequest;
import com.worldpay.service.response.RefundServiceResponse;
import de.hybris.platform.payment.commands.FollowOnRefundCommand;
import de.hybris.platform.payment.commands.request.FollowOnRefundRequest;
import de.hybris.platform.payment.commands.result.RefundResult;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.text.MessageFormat;
import java.util.Currency;

import static de.hybris.platform.payment.dto.TransactionStatus.ERROR;
import static de.hybris.platform.payment.dto.TransactionStatusDetails.COMMUNICATION_PROBLEM;


/**
 * Default Worldpay Refund Follow On command.
 * <p>
 * Communicates through the WorldpayServiceGateway to make the refund call to Worldpay
 * </p>
 */
public class DefaultWorldpayFollowOnRefundCommand extends WorldpayCommand implements FollowOnRefundCommand<FollowOnRefundRequest> {

    private static final Logger LOG = Logger.getLogger(DefaultWorldpayFollowOnRefundCommand.class);

    private Converter<RefundServiceResponse, RefundResult> refundServiceResponseConverter;

    /**
     * {@inheritDoc}
     *
     * @see de.hybris.platform.payment.commands.Command#perform(java.lang.Object)
     */
    @Override
    public RefundResult perform(final FollowOnRefundRequest request) {
        final String orderCode = request.getRequestId();
        final Currency currency = request.getCurrency();
        try {
            final Amount amount = getWorldpayOrderService().createAmount(currency, request.getTotalAmount().doubleValue());
            final MerchantInfo merchantInfo = getMerchantInfo(orderCode);
            final RefundServiceRequest refundServiceRequest = buildRefundRequest(request.getMerchantTransactionCode(), orderCode, amount, merchantInfo);
            return refund(refundServiceRequest);
        } catch (WorldpayException e) {
            LOG.error(MessageFormat.format("Exception raised while issuing a refundRequest: [{0}]", e.getMessage()), e);
            return createErrorRefundResult();
        }
    }

    /**
     * Build the refund service request
     *
     * @param refundReference
     * @param worldpayOrderCode
     * @param amount
     * @param merchantInfo
     * @return RefundServiceRequest object
     */
    protected RefundServiceRequest buildRefundRequest(final String refundReference, final String worldpayOrderCode, final Amount amount, final MerchantInfo merchantInfo) {
        return RefundServiceRequest.createRefundRequest(merchantInfo, worldpayOrderCode, amount, refundReference, Boolean.FALSE);
    }

    private RefundResult createErrorRefundResult() {
        final RefundResult refundResult = new RefundResult();
        refundResult.setTransactionStatus(ERROR);
        refundResult.setTransactionStatusDetails(COMMUNICATION_PROBLEM);
        return refundResult;
    }

    /**
     * Make the call through to Worldpay to refund the customer
     *
     * @param refundRequest service request object
     * @return RefundResult translated from the service response
     */
    private RefundResult refund(final RefundServiceRequest refundRequest) throws WorldpayException {
        final RefundServiceResponse refundResponse = getWorldpayServiceGateway().refund(refundRequest);
        if (refundResponse == null) {
            throw new WorldpayException("Response from worldpay is empty");
        }
        final RefundResult refundResult = refundServiceResponseConverter.convert(refundResponse);
        refundResult.setRequestToken(refundRequest.getMerchantInfo().getMerchantCode());
        return refundResult;
    }

    @Required
    public void setRefundServiceResponseConverter(Converter<RefundServiceResponse, RefundResult> refundServiceResponseConverter) {
        this.refundServiceResponseConverter = refundServiceResponseConverter;
    }
}
