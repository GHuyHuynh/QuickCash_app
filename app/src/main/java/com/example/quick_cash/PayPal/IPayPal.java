package com.example.quick_cash.PayPal;

import com.paypal.android.sdk.payments.PayPalPayment;

/**
 * Interface for the PayPal activity
 * current balance and number
 */
public interface IPayPal {
    public PayPalPayment pay(String username, String amount);

    public int getCurrentBalance();

    boolean isNumber(String balance);
}
