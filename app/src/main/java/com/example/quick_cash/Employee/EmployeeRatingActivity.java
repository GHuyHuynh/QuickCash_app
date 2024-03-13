package com.example.quick_cash.Employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quick_cash.Adapter.AdapterFeedbackList;
import com.example.quick_cash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmployeeRatingActivity extends AppCompatActivity {
    String username;
    String currUsername;
    String email;
    EditText feedBack;
    DatabaseReference userDBRef;
    RatingBar userRatingBar;
    Button userFeedbackSubmitBT;
    Float userRatingFloat;
    Float userIntCount;
    Float userTotalRatingFloat;
    Button userRedirect;
    String feedBackString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_rating);
        getSupportActionBar().hide();
        this.email = this.getIntent().getStringExtra("email");
        this.username = this.getIntent().getStringExtra("username");
        init();
        getUserData();
        setupSubmitButton();
        setupReturnButton();

    }

    /**
     * Back to user profile activity button
     */
    public void setupReturnButton() {

       userRedirect = findViewById(R.id.userRedirect);

       userRedirect.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(EmployeeRatingActivity.this, EmployeeProfileActivity.class);
               intent.putExtra("username", username);
               intent.putExtra("email", email);
               startActivity(intent);
           }
       });
    }

    /**
     * Submit user rating plus feedback
     */
    public void setupSubmitButton() {
        this.userFeedbackSubmitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateRating();
                submitFeedback();
                if(emptyFeedback(feedBackString)){
                    Toast.makeText(EmployeeRatingActivity.this, "Please enter a feedback", Toast.LENGTH_SHORT).show();

                }else {
                    Intent userProfile = new Intent(EmployeeRatingActivity.this, EmployeeProfileActivity.class);
                    userProfile.putExtra("email", email);
                    userProfile.putExtra("username", username);
                    startActivity(userProfile);
                }
            }
        });
    }

    /**
     * Checks if the user input is empty
     * @param feedBackString->feedback the user has wrote
     * @return -> if it is empty it will return true, otherwise false
     */
    public boolean emptyFeedback(String feedBackString){
        return feedBackString.isEmpty();
    }

    /**
     * Checks if the user input meets the requirements of a feedback
     * @param feedBackString->feedback the user has wrote
     * @return if it is valid it will return true, otherwise false
     */
    public boolean isValidFeedback(String feedBackString){
        String regex = "^[A-Za-z\\d\\s!@#$%^&*(),.?\":{}|<>]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(feedBackString);
        return matcher.find();

    }

    /**
     * Submits the the feedback to the DB, and sets the child name as the user's username who wrote it
     */
    private void submitFeedback() {
        feedBackString = feedBack.getText().toString().trim();
        SharedPreferences inAppUsername = getSharedPreferences("Pref", MODE_PRIVATE);
        currUsername = inAppUsername.getString("key_username", null);
        userDBRef.child("feedback").child(currUsername).setValue(feedBackString);
    }

    /**
     * Calculates the average rating the user inputs, total rating is the sum of all rating together
     * Count is how many ratings in total
     * and rate is the average
     */
    private void calculateRating() {
        this.userTotalRatingFloat += this.userRatingBar.getRating();
        this.userIntCount++;
        this.userRatingFloat = (this.userTotalRatingFloat/this.userIntCount);

        //Set in the database
        userDBRef.child("totalRate").setValue(userTotalRatingFloat);
        userDBRef.child("rate").setValue(userRatingFloat);
        userDBRef.child("count").setValue(userIntCount);
    }

    /**
     * Fetches the user data from the DB, of total rate plus how many rates
     */
    private void getUserData() {
        userDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                handleRatingSnapshot(snapshot.child("totalRate"), snapshot.child("count"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("UserInfo", "onCancelled: Error", error.toException());
            }
        });
    }

    /**
     *
     * @param rate-> total rate of the user (Sum of all rating from all time)
     * @param count-> count of how many rating the user has in total
     */
    private void handleRatingSnapshot(DataSnapshot rate, DataSnapshot count) {
        //if they are not null, it will try to cast into a float
        if(rate.exists() && count.exists()){
            try{
                userTotalRatingFloat = rate.getValue(Float.class);
                userIntCount = count.getValue(Float.class);
            }catch (DatabaseException e){
                Log.e("UserInfo", "Error converting", e);
            }
        }else {
            Log.e("UserInfo", "Rate value does not exist in snapshot");
        }
    }

    /**
     * Initializes all the Views into layout ids
     */
    private void init() {
        this.userDBRef = FirebaseDatabase.getInstance(EmployeeConstants.userDBLink).
                getReference().child(username).child("userRating");
        feedBack =findViewById(R.id.user_feedback);
        userFeedbackSubmitBT = findViewById(R.id.user_submit_feedback);
        userRatingBar = findViewById(R.id.user_rateBar);
    }

}