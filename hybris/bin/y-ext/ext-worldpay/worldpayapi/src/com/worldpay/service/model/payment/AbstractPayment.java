package com.worldpay.service.model.payment;

import com.worldpay.exception.WorldpayModelTransformationException;
import com.worldpay.internal.helper.InternalModelObject;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Abstract class that templates part of the transformToInternalModel method for {@link Payment} implementors. Obviously each type of payment will have different
 * fields so reflection is used to get the list of methods for an implementation and then it's up to the subclass to implement the correct logic for any setter
 * methods
 */
public abstract class AbstractPayment implements Payment, Serializable {

    private PaymentType paymentType;

    @Override
    public InternalModelObject transformToInternalModel() throws WorldpayModelTransformationException {
        try {
            Class<?> modelClass = paymentType.getModelClass();
            InternalModelObject instance = (InternalModelObject) modelClass.newInstance();
            Method[] declaredMethods = modelClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                invokeSetter(method, instance);
            }

            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new WorldpayModelTransformationException("Exception while attempting to transform Card", e);
        }
    }

    /**
     * Set the relevant field on the internal model object from the external model representation.
     * <p/>
     * The {@link #transformToInternalModel()} method uses reflection to find all the methods supported by the internal model object and then invokes this method to
     * correctly set the details of each field against the target object
     *
     * @param method       Method that can be invoked on the internal model object targetObject
     * @param targetObject internal model object that we are trying to transform to
     * @throws IllegalArgumentException  if the method is invoked with incorrect parameters
     * @throws IllegalAccessException    if the method is not accessible
     * @throws InvocationTargetException if method cannot be invoked against the supplied target object
     */
    public abstract void invokeSetter(Method method, Object targetObject) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;

    @Override
    public PaymentType getPaymentType() {
        return paymentType;
    }

    @Override
    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
}
