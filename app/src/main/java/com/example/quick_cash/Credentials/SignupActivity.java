package com.example.quick_cash.Credentials;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import com.example.quick_cash.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class let the signup to QuickCash and create the credentials on the Firebase database
 */
public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Variables
     */
    EditText signUpUserName;
    EditText signUpEmail;
    EditText signUpPassword;
    TextView loginRedirectText;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.setUpRegisterButton();
        redirectToLogin();
        getSupportActionBar().hide();
    }

    /**
     * sets up the register button, by finding the ID.
     * automatically creates an onClickListener, since in the view the user must click it to proceed
     */
    public void setUpRegisterButton(){
        Button registerButton = findViewById(R.id.signup_button);
        registerButton.setOnClickListener(this);
    }

    /**
     * adds the input by the user to the databse
     * @param username -> unique Username must be input by each user
     * @param email -> a correct email address
     * @param password -> a password that matches app requirements
     */
    public void initializeDatabase(String username, String email, String password){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        HelperClass helperClass = new HelperClass(username, email, password);
        reference.child(username).setValue(helperClass);
        reference.child(username).child("userRating").child("rate").setValue(0);
        reference.child(username).child("userRating").child("count").setValue(0);
        reference.child(username).child("userRating").child("totalRate").setValue(0);
    }

    /**
     * Simple get functions
     * @return username inputted by the user, it is casted to string
     */
    public String getUserName(){
        signUpUserName = findViewById(R.id.signup_username);
        return signUpUserName.getText().toString().trim();
    }

    /**
     * get function for email
     * @return the email address the user inputs, it is casted to string
     */
    public String getEmail(){
        signUpEmail = findViewById(R.id.signup_email);
        return signUpEmail.getText().toString().trim();
    }

    /**
     * get function for password
     * @return the password the user inputs, it is casted to string
     */
    public String getPassword(){
        signUpPassword = findViewById(R.id.signup_password);
        return signUpPassword.getText().toString().trim();
    }

    /**
     * checks if the password inputs has a special character using the
     * format below
     * @param password-> the password user inputs (must be string)
     * @return true if it does contain, false otehrwise.
     */
    public boolean isValidPasswordChar(String password){
        Pattern specialCharacter = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]+");
        Matcher matcher = specialCharacter.matcher(password);
        return matcher.find();
    }

    /**
     * checks if the password is between 8-20 characters.
     * @param password->the password user inputs(must be string)
     * @return true if it is within the range, false otherwise
     */
    public boolean isValidPasswordLen(String password){
        int minLength = 8;
        int maxLength = 20;
        int passwordLength = password.length();
        return passwordLength >= minLength && passwordLength <= maxLength ;
    }

    /**
     * Uses PattersCompat (Compat allows for unit testing without Mockito)
     * to check if the email is of correct format.
     * @param email->String format email user inputs
     * @return true if it is, false otherwise
     */
    public boolean isValidEmail(String email){
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Checks if a string is empty
     * @param email->String format email user inputs
     * @return -> true if it is, false otherwise
     */
    public boolean emptyEmail(String email){
        return email.isEmpty();
    }

    /**
     * Checks if a string is empty
     * @param username->String format email user inputs
     * @return -> true if it is, false otherwise
     */
    public boolean emptyUsername(String username){
        return username.isEmpty();
    }

    /**
     * Checks if a string is empty
     * @param password->String format email user inputs
     * @return -> true if it is, false otherwise
     */
    public boolean emptyPassword(String password){
        return password.isEmpty();
    }

    /**
     * Switches to login on Successful account creation
     * @param username->username of the user, for account creation
     * @param email->email address of the user, for account creation
     * @param password-> password of the user, for account creation
     */
    public void switch2Login(String username, String email, String password){
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        intent.putExtra("extractUsername", username);
        intent.putExtra("extractEmail", email);
        intent.putExtra("extractPassword", password);
        startActivity(intent);
    }

    /**
     * If the user already has an account this can be used to redirect to login page
     */
    public void redirectToLogin(){
        loginRedirectText = findViewById(R.id.loginRedirect);
        loginRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    /**
     * This function first gets the user input (email, username, password)
     * Then utilizes above defined functions for checking if it matches requriements
     * and does error handling/redirecting accordingly
     * @param view -> Used to switch to the correct View
     */
    @Override
    public void onClick(View view){
        String username = getUserName();
        String email = getEmail();
        String password = getPassword();


        if(isValidEmail(email) && isValidPasswordLen(password) && isValidPasswordChar(password)){
            //if email is valid and password is correct
            Toast.makeText(SignupActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
            initializeDatabase(username, email, password);
            switch2Login(username, email, password);

        }else if(emptyEmail(email) || emptyPassword(password) || emptyUsername(username)){
            Toast.makeText(SignupActivity.this, "One of the fields is blank.", Toast.LENGTH_SHORT).show();
        }else if (!isValidPasswordChar(password)){
            //if password is missing special character
            Toast.makeText(SignupActivity.this, "password is missing special Character.", Toast.LENGTH_SHORT).show();
        }else if(!isValidPasswordLen(password)){
            //if password is not the right length
            Toast.makeText(SignupActivity.this, "password is not between 8-20 characters", Toast.LENGTH_SHORT).show();

        }else if (!isValidEmail(email)){
            //if email is invalid
            Toast.makeText(SignupActivity.this, "Email is invalid.", Toast.LENGTH_SHORT).show();
        }
    }

}

