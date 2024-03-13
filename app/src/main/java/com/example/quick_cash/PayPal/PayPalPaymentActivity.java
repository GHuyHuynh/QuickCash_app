package com.example.quick_cash.PayPal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.MainHomepages.BossActivity;
import com.example.quick_cash.R;
import com.example.quick_cash.MainHomepages.WorkerActivity;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

public class PayPalPaymentActivity extends AppCompatActivity {

    EditText usernameET;
    EditText amountET;
    Button submitPayBT;
    TextView redirectText;
    String redirectType;

    public PayPalConfiguration config;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Code for redirecting back to the correct activity
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                redirectType = null;
            } else {
                redirectType = extras.getString("activity");
            }
        }   else {
            redirectType = (String) savedInstanceState.getSerializable("activity");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payement);
        init();
        redirectToActivity();

        getSupportActionBar().hide();
    }

    /**
     * Back to the users activity they came from (Employee or Employer)
     */
    private void redirectToActivity() {
        redirectText = findViewById(R.id.redirectBack);
        redirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(redirectType.equals("WorkerActivity")) {
                    intent = new Intent(PayPalPaymentActivity.this, WorkerActivity.class);
                }else{
                    intent = new Intent(PayPalPaymentActivity.this, BossActivity.class);
                }
                startActivity(intent);
            }
        });
    }

    /**
     * initialize paypal configuration and submit pay button
     */
    private void init() {
        config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(PaymentConstants.PAYPAL_CLIENT_ID)
                .environment(PaymentConstants.PAYPAL_NO_NETWORK);
        amountET = findViewById(R.id.payement_amount);
        usernameET = findViewById(R.id.payement_username);
        submitPayBT = findViewById(R.id.payement_button);
        submitPayBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPayment();
            }
        });
    }

    /**
     * This will get the payment amount and send the user to the paypal activity
     */
    protected void getPayment() {
        String username = usernameET.getText().toString().trim();
        String amountString = amountET.getText().toString().trim();
        if(isNumber(amountString)){
            IPayPal payPal = new PayPal(username, amountString);
            PayPalPayment payment = payPal.pay(username, amountString);
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
            startActivityForResult(intent, PaymentConstants.PAYPAL_REQUEST_CODE);
        }else {
            Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks if the user correctly completed the payment
     * @param requestCode-> the code should be 123
     * @param resultCode -> the code that the paypal sends back
     * @param data-> Intent information
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PaymentConstants.PAYPAL_REQUEST_CODE){
            PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if(paymentConfirmation!= null){
                try {
                    String paymentDetails = paymentConfirmation.toJSONObject().toString();
                    JSONObject jsonObject = new JSONObject(paymentDetails);
                    Intent intent = new Intent(this, BossActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            else if(requestCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "Invalid payment", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNumber(String input){
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
