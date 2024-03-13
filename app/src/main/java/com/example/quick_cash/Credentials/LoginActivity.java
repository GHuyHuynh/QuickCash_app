package com.example.quick_cash.Credentials;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.R;
import com.example.quick_cash.MainHomepages.SelectRoleActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * This class main it to prompt the user login credentials
 * Check the credentials if it match the database, if not prompt the appropriate error
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * Variables
     */
    DatabaseReference databaseReference;
    TextView signupRedirectText;
    private static final String CHECKBOX = "checkbox";
    private static final String REMEMBER = "remember";
    private static final String USERNAME = "userName";
    private static Switch rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setupLoginButton();
        this.setupRememberSwitch();
        redirectToSignup();
        getSupportActionBar().hide();
        /*
         * Remember login function
         */
        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            }
        });
    }

    /**
     * Set up redirect to signup button
     */
    public void redirectToSignup(){
        signupRedirectText = findViewById(R.id.signupRedirect);
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * return the user username input
     * @return -> username of the user
     */
    public String getUsername() {
        EditText username = findViewById(R.id.login_username);
        return username.getText().toString().trim();
    }

    /**
     * return the password of the user input
     * @return -> password of the user
     */
    public String getPassword() {
        EditText password = findViewById(R.id.login_password);
        return password.getText().toString().trim();
    }

    /**
     * check if username input is empty
     * @param username -> username form user
     * @return -> true if username if not empty and vice-versa
     */
    public Boolean emptyUsername(String username) {
        return !username.isEmpty();
    }

    /**
     * check if password is empty
     * @param password -> password from user
     * @return -> true if password is not empty and vice-versa
     */
    public Boolean emptyPassword(String password) {
        return !password.isEmpty();
    }

    /**
     * Set up login button
     */
    public void setupLoginButton() {
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
    }

    /**
     * Set up remember me switch
     */
    public void setupRememberSwitch(){
        rememberMe = findViewById(R.id.rememberMe);
    }

    /**
     * Set up button to redirect to signup
     */
    public void setupSignupRedirect() {
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Switch to select role activity on complete
     * @param username -> username from current log in user
     */
    public void switch2SelectRole (String username) {
        Intent intent = new Intent(LoginActivity.this, SelectRoleActivity.class);
        intent.putExtra("extractUsername", username);
        startActivity(intent);
    }

    /**
     * When the user click login, check with database for credential
     * If credential is good, log in
     * If credential is wrong, prompt the appropriate error
     * @param view -> current view
     */
    @Override
    public void onClick(View view) {
        String credUsername = getUsername();
        String credPassword = getPassword();

        if (areCredentialsValid(credUsername, credPassword)) {
            checkUserInDatabase(credUsername, credPassword);
        } else {
            if (!emptyUsername(credUsername)) {
                showToast("username is missing.");
            }
            if (!emptyPassword(credPassword)) {
                showToast("password is missing.");
            }
        }
    }

    private boolean areCredentialsValid(String username, String password) {
        return emptyUsername(username) && emptyPassword(password);
    }

    private void checkUserInDatabase(String username, String password) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query checkUserDatabase = databaseReference.orderByChild("username").equalTo(username);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                handleDataSnapshot(snapshot, username, password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Database Error, please try again in a few hours");
            }
        });
    }

    private void handleDataSnapshot(DataSnapshot snapshot, String username, String password) {
        if (snapshot.exists()) {
            String databasePassword = snapshot.child(username).child("password").getValue(String.class);

            if (databasePassword.equals(password)) {
                handleSuccessfulLoginActions(username);
            } else {
                showToast("password is invalid.");
            }
        } else {
            showToast("user does not exist.");
        }
    }

    private void handleSuccessfulLoginActions(String username) {
        if (rememberMe.isChecked()) {
            updateRememberPreference(true, username);
            showToast("Login stored!");
        } else {
            updateRememberPreference(false, null);
            showToast("Login not stored!");
        }

        inAppUsernamePreference(username);

        showToast("Login successfully!");
        switch2SelectRole(username);
    }

    private void updateRememberPreference(boolean shouldRemember, String username) {
        SharedPreferences preferences = getSharedPreferences(CHECKBOX, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(REMEMBER, String.valueOf(shouldRemember));
        if (username != null) {
            editor.putString(USERNAME, username);
        }
        editor.apply();
    }

    private void inAppUsernamePreference(String username) {
        SharedPreferences inAppUsername = getSharedPreferences("Pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = inAppUsername.edit();
        editor.putString("key_username", username);
        editor.commit();
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}
