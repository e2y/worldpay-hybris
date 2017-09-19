package com.worldpay.forms.validation;

import com.worldpay.forms.CSEPaymentForm;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validate CSE form
 */
@Component ("cseFormValidator")
public class CseFormValidator implements Validator {
    protected static final String GLOBAL_MISSING_CSE_TOKEN = "checkout.multi.paymentMethod.cse.invalid";

    @Override
    public boolean supports(final Class<?> aClass) {
        return CSEPaymentForm.class.equals(aClass);
    }

    @Override
    public void validate(final Object object, final Errors errors) {
        final CSEPaymentForm form = (CSEPaymentForm) object;

        if (StringUtils.isBlank(form.getCseToken())) {
            errors.reject(GLOBAL_MISSING_CSE_TOKEN);
        }
    }
}
