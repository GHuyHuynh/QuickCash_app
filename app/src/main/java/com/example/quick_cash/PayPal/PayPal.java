package com.example.quick_cash.PayPal;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalPayment;

import java.math.BigDecimal;

public class PayPal implements IPayPal{
    private String balanceString;
    private String username;
    private int balanceInt;

    /**
     * Constructor
     * @param username->to who are we transferring money to
     * @param balance-> how much money are we transferring to them
     */
    public PayPal(String username, String balance){
        this.username = username;
        this.balanceString = balance;
    }

    /**
     * checks if the amount is a number
     * @param username->who
     * @param amount->how much
     * @return -> it will return a confirmation code
     */
    @Override
    public PayPalPayment pay(String username, String amount){
        if(isNumber(amount)){
            balanceInt = Integer.parseInt(balanceString);
            PayPalPayment payment =
                    new PayPalPayment(new BigDecimal(balanceInt), "CAD", username, PayPalPayment.PAYMENT_INTENT_SALE);
            return payment;
        }else {
            return null;
        }
    }

    @Override
    public int getCurrentBalance() {
        return balanceInt;
    }

    /**
     * Checks if the input is a number
     * @param input-> user input
     * @return-> true if it is, false otherwise
     */
    public boolean isNumber(String input){
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}
