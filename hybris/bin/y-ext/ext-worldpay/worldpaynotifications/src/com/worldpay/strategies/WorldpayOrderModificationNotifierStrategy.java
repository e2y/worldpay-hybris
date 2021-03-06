package com.worldpay.strategies;

/**
 * Worldpay Order Modification Notifier Strategy interface.
 * The strategy is responsible for notifying about orders that have not been processed for a longer period of time.
 */
public interface WorldpayOrderModificationNotifierStrategy {

    /**
     * Notifies about order that have not been processed for a longer period of time.
     *
     * @param days number of days to wait until sending the notification
     */
    void notifyThatOrdersHaveNotBeenProcessed(int days);
    
}
