package com.example.quick_cash.JobPost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.MainHomepages.BossActivity;
import com.example.quick_cash.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is extended from Boss activity and let the user add a new job to the database
 */
public class AddJobActivity extends AppCompatActivity {
    /**
     * Variables
     */
    EditText jobName;
    EditText jobDes;
    EditText jobDate;
    EditText jobDuration;
    EditText jobUrgency;
    EditText jobSalary;
    EditText jobLocation;
    Button jobSubmitButton;
    String username;
    FirebaseDatabase jobDataBase;
    DatabaseReference jobDatabaseRef;

    FirebaseDatabase userDataBase;
    DatabaseReference userDatabaseRef;

    //Separate database
    String jobDataBaseURL = "https://group-1-job-database-1c791-default-rtdb.firebaseio.com/";
    private String userDatabaseURL = "https://group-1-quick-cash-default-rtdb.firebaseio.com/";
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        getSupportActionBar().hide();

        SharedPreferences inAppUsername = getSharedPreferences("Pref", MODE_PRIVATE);
        username = inAppUsername.getString("key_username", null);

        setupSubmitJobButton(username);
        setupBackBtn();
    }

    public void setupBackBtn() {
        backBtn = findViewById(R.id.user_profile_back_btn2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * get the job name from user input
     * @return -> job name input by user
     */
    public String getJobName(){
        jobName = findViewById(R.id.add_job_name);
        return jobName.getText().toString().trim();
    }

    /**
     * get the job description from user input
     * @return -> job description form user input
     */
    public String getJobDes(){
        jobDes = findViewById(R.id.add_job_des);
        return jobDes.getText().toString().trim();
    }

    /**
     * get job date from the user input
     * @return -> a string from integer date input from user
     */
    public String getJobDate(){
        jobDate = findViewById(R.id.add_job_date);
        return jobDate.getText().toString().trim();
    }

    /**
     * validate if the date is valid or not. Return false if the date is already passed.
     * @param date -> string date input from user
     * @return -> true if date is valid, false if date is not valid.
     */
    public boolean isValidJobDate (String date) {
        if (date.length() != 10) {
            return false;
        }
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7));
        int day = Integer.parseInt(date.substring(8,10));

        Date today = new Date();
        Date jobDateInsert = new Date(year-1900, month-1, day);

        return !jobDateInsert.before(today);
    }

    /**
     * get the job duration from user input
     * @return -> job duration integer from user input
     */
    public int getJobDuration(){
        jobDuration = findViewById(R.id.add_job_duration);
        return Integer.parseInt(jobDuration.getText().toString().trim());
    }

    /**
     * get the job urgency from user input
     * @return -> job urgency form user.
     */
    public String getJobUrgency(){
        jobUrgency = findViewById(R.id.add_job_urgency);
        return jobUrgency.getText().toString().trim();
    }

    /**
     * get the job salary from user input
     * @return -> job salary as integer from the user input
     */
    public int getJobSalary(){
        jobSalary = findViewById(R.id.add_job_salary);
        return Integer.parseInt(jobSalary.getText().toString().trim());
    }

    /**
     * get the job location from user input
     * @return -> job location from user
     */
    public String getJobLocation(){
        jobLocation = findViewById(R.id.add_job_location);
        return jobLocation.getText().toString().trim();
    }

    public Map getJob(String usernameIn) {
        Map<String, Object> map = new HashMap<>();

        map.put("jobName", this.getJobName());
        map.put("jobDes", this.getJobDes());
        map.put("jobDate", this.getJobDate());
        map.put("jobDuration", this.getJobDuration());
        map.put("jobUrgency", this.getJobUrgency());
        map.put("jobSalary", this.getJobSalary());
        map.put("jobLocation", this.getJobLocation());
        map.put("jobEmployer", usernameIn);

        return map;

    }

    /**
     * Check if job location entered is valid or not based on GeoLocate
     * @param jobLocationIn -> job location entered by employer
     * @return -> true if location can be pull from GeoCoder library, false if otherwise
     */
    public boolean isValidJobLocation(String jobLocationIn) {
        Geocoder geocoder = new Geocoder(AddJobActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(jobLocationIn, 1);
        } catch (IOException e) {
            Toast.makeText(this, "Cannot get location", Toast.LENGTH_SHORT).show();
        }

        return !list.isEmpty();
    }

    /**
     * submit job post to database with different database to hold job post
     * submitting the job post would also created an unique ID for each job post
     * @param jobPosition HashMap object of the job post that hold details about the job
     */
    public void submitJobToDatabase(String name, Map jobPosition) {
        //Adding to JobDatabase
        jobDataBase = FirebaseDatabase.getInstance(jobDataBaseURL); //Currently using separate database
        jobDatabaseRef = jobDataBase.getReference("jobs");
        DatabaseReference jobRef = jobDatabaseRef.push();
        jobRef.setValue(jobPosition);

        //Adding to User Database
        userDataBase = FirebaseDatabase.getInstance(userDatabaseURL);
        userDatabaseRef = userDataBase.getReference().child(username).child("JobsPosted");
        DatabaseReference userRef = userDatabaseRef.push();
        userRef.child(name).setValue(jobRef.getKey());
    }

    /**
     * set up job submit button with onclick listener
     * when user click submit job, all date would be compress into Hashmap object and send to the database
     * @param usernameIn -> username of the account that logged in
     */
    public void setupSubmitJobButton(String usernameIn) {
        jobSubmitButton = findViewById(R.id.add_job_button);
        jobSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jobDateRetrieve = getJobDate();
                String jobLocationRetrieve = getJobLocation();
                if (isValidJobDate(jobDateRetrieve) && isValidJobLocation(jobLocationRetrieve)) {
                    Map jobSubmit = getJob(usernameIn);
                    submitJobToDatabase(getJobName(),jobSubmit);
                    Intent intent = new Intent(AddJobActivity.this, BossActivity.class);
                    intent.putExtra("extractUsername", username);
                    startActivity(intent);
                }
                else if (!isValidJobDate(jobDateRetrieve)){
                    Toast.makeText(AddJobActivity.this, "Please enter a valid date", Toast.LENGTH_SHORT).show();
                } else if (!isValidJobLocation(jobLocationRetrieve)) {
                    Toast.makeText(AddJobActivity.this, "Please enter a more detailed job location", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}