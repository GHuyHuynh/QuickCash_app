package com.example.quick_cash.JobPost;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.Model.ModelJobPosition;
import com.example.quick_cash.R;
import com.example.quick_cash.MainHomepages.WorkerActivity;
import com.google.gson.Gson;

public class JobPostDetails extends AppCompatActivity {
    /**
     * Variables
     */
    ModelJobPosition jobPost;
    TextView jobName;
    TextView jobDes;
    TextView jobDate;
    TextView jobDuration;
    TextView jobUrgency;
    TextView jobSalary;
    TextView jobLocation;
    TextView jobEmployer;
    Button goToMapButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post_details);
        Gson gson = new Gson();

        jobPost = gson.fromJson(getIntent().getStringExtra("extractJob"), ModelJobPosition.class);
        initializeTextView();


        fillJobPost(jobPost);
        setUpMapButton(jobPost.getJobName(), jobPost.getJobLocation());
    }

    /**
     * get the back menu for second option of going back other than the Android native back button
     * @param menu -> back menu item
     * @return -> true for getting the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return true;
    }

    /**
     * Set up click option for menu
     * Set up back button in this menu
     * @param item -> item input for switch case
     * @return -> the option that the user
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.back_button:
                Intent worker = new Intent(JobPostDetails.this, WorkerActivity.class);
                startActivity(worker);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Connect all variables with their respective UI components
     */
    protected void initializeTextView() {
        this.jobName = findViewById(R.id.job_detail_name);
        this.jobDes = findViewById(R.id.job_detail_des);
        this.jobDate = findViewById(R.id.job_detail_date);
        this.jobDuration = findViewById(R.id.job_detail_duration);
        this.jobUrgency = findViewById(R.id.job_detail_urgency);
        this.jobSalary = findViewById(R.id.job_detail_salary);
        this.jobLocation = findViewById(R.id.job_detail_location);
        this.jobEmployer = findViewById(R.id.job_detail_employer);
    }

    /**
     * Fill all the UI components with their respective data
     * For example, Job name box get fill with job name
     * @param job -> the job position that the user clicked on
     */
    protected void fillJobPost(ModelJobPosition job) {

        this.jobDes.setText("Description: " + job.getJobDes());
        this.jobName.setText("Title: "+ job.getJobName());
        this.jobDate.setText("Date: " + job.getJobDate());
        this.jobDuration.setText("Duration: " + job.getJobDuration() + " hours");
        this.jobUrgency.setText("Urgency: " + job.getJobUrgency());
        this.jobSalary.setText("Salary: " + job.getJobSalary() + " CAD");
        this.jobLocation.setText("Location: " + job.getJobLocation());
        this.jobEmployer.setText("Employer: "+ job.getJobEmployer());
    }

    /**\
     * Switch to map activity that show the job location on Google Map
     * @param jobName -> job name from the job post
     * @param jobLocation -> job location from the job post
     */
    protected void setUpMapButton(String jobName, String jobLocation) {
        goToMapButton = findViewById(R.id.see_map_button);
        goToMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map = new Intent(JobPostDetails.this, JobLocationMapsActivity.class);
                map.putExtra("extractJobName", jobName);
                map.putExtra("extractJobLocation", jobLocation);
                startActivity(map);
            }
        });
    }
}