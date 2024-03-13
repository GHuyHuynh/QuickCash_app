package com.example.quick_cash.MainHomepages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.Adapter.AdapterPosition;
import com.example.quick_cash.Credentials.LoginActivity;

import com.example.quick_cash.Model.ModelResume;
import com.example.quick_cash.Model.ModelJobPosition;
import com.example.quick_cash.MyLocalArea.MyLocalAreaActivity;
import com.example.quick_cash.Notification.NotificationActivity;
import com.example.quick_cash.PayPal.PayPalPaymentActivity;
import com.example.quick_cash.Employee.EmployeeListActivity;
import com.example.quick_cash.R;
import com.example.quick_cash.Search.SearchActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * When user chooses to be an employee, this is the homepage where they will see all job posts
 */
public class WorkerActivity extends AppCompatActivity implements Serializable {

    private List<ModelJobPosition> allJobsList = new ArrayList<>();
    private List<String> allBossList = new ArrayList<>();
    private ListView allJobsListView;
    private ListView preferredJobsListView;
    private Switch switchPreferredJobs;
    private String username;
    private FirebaseDatabase jobDataBase;
    private DatabaseReference jobDatabaseReference;
    private String jobDatabaseURL = "https://group-1-job-database-1c791-default-rtdb.firebaseio.com/";
    private FirebaseDatabase userDataBase;
    private DatabaseReference userDatabaseReference;
    private String userDatabaseURL = "https://group-1-quick-cash-default-rtdb.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        initializeViews();
        loadData();
    }

    /**
     * Prevents the pages from not refreshing
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        initializeViews();
        loadData();
    }

    private void initializeViews() {

        SharedPreferences inAppUsername = getSharedPreferences("Pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = inAppUsername.edit();
        username = inAppUsername.getString("key_username", null);

        preferredJobsListView = findViewById(R.id.listView_preferredJobs);
        allJobsListView = findViewById(R.id.listView_positions);
        switchPreferredJobs = findViewById(R.id.switch_preferredJobs);

        // Set the initial visibility based on the switch state
        if (switchPreferredJobs.isChecked()) {
            // If switch is checked, hide "All Jobs" ListView
            allJobsListView.setVisibility(View.GONE);
            preferredJobsListView.setVisibility(View.VISIBLE);
        } else {
            // If switch is not checked, show "All Jobs" ListView
            allJobsListView.setVisibility(View.VISIBLE);
            preferredJobsListView.setVisibility(View.GONE);
        }

        // Set up listener for switch changes
        switchPreferredJobs.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // If switch is checked, hide "All Jobs" ListView
                allJobsListView.setVisibility(View.GONE);
                preferredJobsListView.setVisibility(View.VISIBLE);
            } else {
                // If switch is not checked, show "All Jobs" ListView
                allJobsListView.setVisibility(View.VISIBLE);
                preferredJobsListView.setVisibility(View.GONE);
            }
        });
    }

    private void loadData() {
        getDatabaseData();
        getPreferredJobs();
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
                        if (!jobPost.getJobEmployer().equalsIgnoreCase(username)) {
                            String jobKey = jobDataSnap.getKey();
                            jobPost.setJobKey(jobKey);
                            allJobsList.add(jobPost);
                        }
                    }
                }
                showData(allJobsList, allJobsListView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //No message
            }
        });
    }

    /**
     * Will gather all jobs that contain a keyword the user has chosen to describe favourite jobs
     */
    public void getPreferredJobs() {
        initializeDatabaseReferences();
        fetchUserKeywords();
    }

    private void initializeDatabaseReferences() {
        jobDataBase = FirebaseDatabase.getInstance(jobDatabaseURL);
        jobDatabaseReference = jobDataBase.getReference().child("jobs");
        userDataBase = FirebaseDatabase.getInstance(userDatabaseURL);
        userDatabaseReference = userDataBase.getReference().child(username).child("PreferredJobs");
    }

    /**
     * Gets the new preferences
     */
    private void fetchUserKeywords() {
        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> userKeywords = extractUserKeywords(dataSnapshot);
                    fetchPreferredJobs(userKeywords);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });
    }

    /**
     * Returns the list of all the preferences the user has stored in DB
     * @param dataSnapshot->all the preferences the user has stored in DB
     * @return-> list of all of the preferences
     */
    private List<String> extractUserKeywords(DataSnapshot dataSnapshot) {
        List<String> userKeywords = new ArrayList<>();
        for (DataSnapshot keywordSnapshot : dataSnapshot.getChildren()) {
            String keyword = keywordSnapshot.getKey();
            String keyword_boss = keywordSnapshot.getValue().toString();
            userKeywords.add(keyword.toLowerCase());
            userKeywords.add(keyword_boss);
        }
        return userKeywords;
    }

    /**
     * Retrieves the job keywords
     * @param userKeywords-> the keywords user inputs (list)
     */
    private void fetchPreferredJobs(List<String> userKeywords) {
        jobDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot jobSnapshot) {
                List<ModelJobPosition> preferredJobsList = new ArrayList<>();
                if (jobSnapshot.exists() && jobSnapshot.getChildrenCount() > 0) {
                    filterJobsByKeywords(jobSnapshot, userKeywords, preferredJobsList);
                    showData(preferredJobsList, preferredJobsListView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("UserInfo", "Fetch failed to get preferred job keywords");
            }
        });
    }

    /**
     * Filters the jobposting by their preference
     * @param jobSnapshot-> all the job posts
     * @param userKeywords -> users preferences
     * @param preferredJobsList -> the list of all preferred jobs
     */
    private void filterJobsByKeywords(DataSnapshot jobSnapshot, List<String> userKeywords, List<ModelJobPosition> preferredJobsList) {
        preferredJobsList.clear();
        for (DataSnapshot jobDataSnap : jobSnapshot.getChildren()) {
            ModelJobPosition jobPost = jobDataSnap.getValue(ModelJobPosition.class);


            for (String keyword : userKeywords) {
                if (jobPostMatchesKeyword(jobPost, keyword) && !jobPost.getJobEmployer().equalsIgnoreCase(username)) {
                    String jobKey = jobDataSnap.getKey();
                    jobPost.setJobKey(jobKey);
                    preferredJobsList.add(jobPost);
                    break;
                }
            }
        }

    }

    /**
     *
     * @param jobPost -> job posted
     * @param keyword -> the keywords the user has as preference
     * @return -> if it matches a requirement it will return true else false
     */
    private boolean jobPostMatchesKeyword(ModelJobPosition jobPost, String keyword) {
        return jobPost.getJobName().toLowerCase().contains(keyword)
                || jobPost.getJobDes().toLowerCase().contains(keyword)
                || jobPost.getJobEmployer().toLowerCase().contains(keyword)
                || jobPost.getJobLocation().toLowerCase().contains(keyword);
    }


    /**
     * Show the data of all the job list in their linked View UI
     * @param list -> list of all job post with Model_Job_Position type object
     */
    public void showData(List<ModelJobPosition> list, ListView listView){
        AdapterPosition adapterPositions =new AdapterPosition(WorkerActivity.this,list);
        listView.setAdapter(adapterPositions);
    }

    /**
     * Set up the appropriate menu for worker activity
     * @param menu -> id of menu from object
     * @return -> true when the menu is visible, false if not visible
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_bar, menu);

        //disable AddJob button
        MenuItem item = menu.findItem(R.id.addJobPosting);
        item.setVisible(false);
        return true;
    }

    //send username to other page
    public String getUsername(){
        return username;
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
                Intent boss = new Intent(WorkerActivity.this, BossActivity.class);
                boss.putExtra("extractUsername", username);
                startActivity(boss);
                return true;
            /**
             Switch add job activity on click and carry the username over
             */
            case R.id.search_bar:
                Intent search = new Intent(WorkerActivity.this, SearchActivity.class);
                startActivity(search);
                return true;


            case R.id.message:
                Intent dealResume =new Intent(WorkerActivity.this, NotificationActivity.class);
                dealResume.putExtra("extractUsername", username);
                startActivity(dealResume);
                return true;


            /**
             Switch to log out activity and logged out the user
             */
            case R.id.Logout:
                Intent logout = new Intent(WorkerActivity.this, LoginActivity.class);
                SharedPreferences preferences = getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.remove("username");
                editor.apply();

                startActivity(logout);
                return true;

            case R.id.my_local_activity:
                Intent myArea = new Intent(WorkerActivity.this, MyLocalAreaActivity.class);
                myArea.putExtra("extractUsername", username);
                myArea.putExtra("PREVIOUS_ACTIVITY",WorkerActivity.class.getName());
                startActivity(myArea);
                return true;

            /**
             * Switch to preferences
             */
            case R.id.addJobPreferences:
                Intent preferredJob = new Intent(WorkerActivity.this, PreferredJobActivity.class);
                preferredJob.putExtra("extractUsername", username);
                startActivity(preferredJob);
                return true;

            case R.id.user_list:
                Intent userList = new Intent(WorkerActivity.this, EmployeeListActivity.class);
                userList.putExtra("PREVIOUS_ACTIVITY",WorkerActivity.class.getName());
                startActivity(userList);
                return true;
            case R.id.requestPayment:
                Intent payment = new Intent(WorkerActivity.this, PayPalPaymentActivity.class);
                payment.putExtra("activity", "WorkerActivity");
                startActivity(payment);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void submitResumeToDatabase(ModelResume modelResume){
        FirebaseDatabase resumeDatabase =FirebaseDatabase.getInstance(jobDatabaseURL);
        DatabaseReference resumeDatabaseRef = resumeDatabase.getReference().child("resumes");
        DatabaseReference newResumeRef = resumeDatabaseRef.push();
        newResumeRef.setValue(modelResume);
    }
}
