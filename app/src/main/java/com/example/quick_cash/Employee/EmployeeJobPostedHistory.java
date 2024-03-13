package com.example.quick_cash.Employee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.Adapter.AdapterPosition;
import com.example.quick_cash.Credentials.LoginActivity;
import com.example.quick_cash.MainHomepages.BossActivity;
import com.example.quick_cash.Model.ModelJobPosition;
import com.example.quick_cash.MyLocalArea.MyLocalAreaActivity;
import com.example.quick_cash.Notification.NotificationActivity;
import com.example.quick_cash.PayPal.PayPalPaymentActivity;
import com.example.quick_cash.MainHomepages.PreferredJobActivity;
import com.example.quick_cash.R;
import com.example.quick_cash.Search.SearchActivity;
import com.example.quick_cash.MainHomepages.WorkerActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmployeeJobPostedHistory extends AppCompatActivity implements Serializable {
    private List<ModelJobPosition> allJobsList = new ArrayList<>();
    private ListView jobsListView;
    String username;
    String email;
    private FirebaseDatabase jobDataBase;
    private DatabaseReference jobDatabaseReference;
    private String jobDatabaseURL = "https://group-1-job-database-1c791-default-rtdb.firebaseio.com/";
    Button returnButton;
    Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_postings_list);

        getSupportActionBar().hide();

        initializeViews();
        loadData();
    }

    private void initializeViews() {
        Intent data = getIntent();
        username = data.getStringExtra("username");
        email = data.getStringExtra("email");
        jobsListView = findViewById(R.id.listView_JobsFromUser);
        returnButton = findViewById(R.id.btnReturnToEmployeeList);
        applyButton = findViewById(R.id.worker_sendResume);

        setReturnButton();
    }

    public void setReturnButton(){
        this.returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToUser = new Intent(EmployeeJobPostedHistory.this, EmployeeProfileActivity.class);
                backToUser.putExtra("username", username);
                backToUser.putExtra("email", email);
                startActivity(backToUser);
            }
        });
    }

    private void loadData() {
        initializeDatabaseReferences();
        getDatabaseData();
    }

    private void initializeDatabaseReferences() {
        jobDataBase = FirebaseDatabase.getInstance(jobDatabaseURL);
        jobDatabaseReference = jobDataBase.getReference().child("jobs");
    }

    /**
     * Get all data from job post data
     */
    public void getDatabaseData () {
        jobDataBase = FirebaseDatabase.getInstance(jobDatabaseURL);
        jobDatabaseReference = jobDataBase.getReference().child("jobs");
        jobDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot jobSnapshot) {
                allJobsList.clear();
                if (jobSnapshot.exists() && jobSnapshot.getChildrenCount() > 0) {
                    ModelJobPosition jobPost;
                    for (DataSnapshot jobDataSnap : jobSnapshot.getChildren()) {
                        jobPost = jobDataSnap.getValue(ModelJobPosition.class);
                        if (jobPost.getJobEmployer().equalsIgnoreCase(username)) {
                            String jobKey = jobDataSnap.getKey();
                            jobPost.setJobKey(jobKey);
                            allJobsList.add(jobPost);
                        }
                    }
                }
                showData(allJobsList, jobsListView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //No message
            }
        });
    }

    /**
     * Show the data of all the job list in their linked View UI
     * @param list -> list of all job post with Model_Job_Position type object
     */
    public void showData(List<ModelJobPosition> list, ListView listView){
        AdapterPosition adapterPositions = new AdapterPosition(EmployeeJobPostedHistory.this,list);
        listView.setAdapter(adapterPositions);
    }

    /**
     * Set up the value option for this worker activity
     * @param item -> id of menu item
     * @return -> true if the user clicked on one of the options
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            /**
             Switch to boss activity on click and carry username over
             */
            case R.id.Boss:
                Intent boss = new Intent(EmployeeJobPostedHistory.this, BossActivity.class);
                boss.putExtra("extractUsername", username);
                startActivity(boss);
                return true;
            /**
             Switch add job activity on click and carry the username over
             */
            case R.id.search_bar:
                Intent search = new Intent(EmployeeJobPostedHistory.this, SearchActivity.class);
                startActivity(search);
                return true;


            case R.id.message:
                Intent dealResume =new Intent(EmployeeJobPostedHistory.this, NotificationActivity.class);
                dealResume.putExtra("extractUsername", username);
                startActivity(dealResume);
                return true;

            /**
             Switch to log out activity and logged out the user
             */
            case R.id.Logout:
                Intent logout = new Intent(EmployeeJobPostedHistory.this, LoginActivity.class);
                SharedPreferences preferences = getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.remove("username");
                editor.apply();

                startActivity(logout);
                return true;

            case R.id.my_local_activity:
                Intent myArea = new Intent(EmployeeJobPostedHistory.this, MyLocalAreaActivity.class);
                myArea.putExtra("extractUsername", username);
                startActivity(myArea);
                return true;

            /**
             * Switch to preferences
             */
            case R.id.addJobPreferences:
                Intent preferredJob = new Intent(EmployeeJobPostedHistory.this, PreferredJobActivity.class);
                preferredJob.putExtra("extractUsername", username);
                startActivity(preferredJob);
                return true;

            case R.id.user_list:
                Intent userList = new Intent(EmployeeJobPostedHistory.this, EmployeeListActivity.class);
                startActivity(userList);
                return true;
            case R.id.requestPayment:
                Intent payment = new Intent(EmployeeJobPostedHistory.this, PayPalPaymentActivity.class);
                payment.putExtra("activity", "WorkerActivity");
                startActivity(payment);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
