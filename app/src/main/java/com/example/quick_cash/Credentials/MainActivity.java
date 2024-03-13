package com.example.quick_cash.Credentials;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.R;
import com.example.quick_cash.MainHomepages.SelectRoleActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Start up activity this class does 3 things
 * - Connect to user credential database
 * - Give signup option
 * - Give login option
 */
public class MainActivity extends AppCompatActivity {
    private Button signUp;
    private Button logIn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        signUp = (Button)findViewById(R.id.buttonTest);
        signUp.setBackgroundColor(Color.TRANSPARENT);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
        });


        logIn = (Button)findViewById(R.id.loginMain);
        logIn.setBackgroundColor(Color.TRANSPARENT);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        //check if rememberMe is true then skip homepage
        SharedPreferences preferences = getSharedPreferences("checkbox", Context.MODE_PRIVATE);
        String rememberMeState = preferences.getString("remember", "");

        if (rememberMeState.equals("true")){
            String  savedUsername = preferences.getString("username","");
            Intent intent = new Intent(MainActivity.this, SelectRoleActivity.class);
            intent.putExtra("savedUsername", savedUsername);
            startActivity(intent);
        }else{
            logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }

            });
        }

    }
}