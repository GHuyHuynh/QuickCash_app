package com.example.quick_cash;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.quick_cash.PayPal.IPayPal;
import com.example.quick_cash.PayPal.PayPal;
import com.example.quick_cash.PayPal.PayPalPaymentActivity;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class PayPalUnitTest {
    static PayPalPaymentActivity paypalActivity;

    @BeforeClass
    public static void setup(){
        paypalActivity = new PayPalPaymentActivity();
    }


    /**
     * Testing for not number value for balance.
     *
     * False if not number.
     */
    @Test
    public void testNotNumber(){
        String balance = "test";
        IPayPal payPal = new PayPal("test", balance);
        assertFalse(payPal.isNumber(balance));
    }

    /**
     * Testing for a number valule in balance.
     *
     * True if it is a number.
     */
    @Test
    public void testNumber(){
        String balance = "5000";
        IPayPal payPal = new PayPal(("test"), balance);
        assertTrue(payPal.isNumber(balance));
    }
}
