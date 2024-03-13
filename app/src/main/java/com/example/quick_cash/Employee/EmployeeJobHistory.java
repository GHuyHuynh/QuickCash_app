package com.example.quick_cash.Employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quick_cash.Adapter.AdapterJobList;
import com.example.quick_cash.Model.ModelJobPosition;
import com.example.quick_cash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeJobHistory extends AppCompatActivity {
    /*Global variables*/
    String username;
    String email;
    ListView completedJobListView;
    ArrayList<ModelJobPosition> completedJobList = new ArrayList<>();
    DatabaseReference userDBRef;
    DatabaseReference jobDBRef;
    TextView noJobTextView;
    TextView backToUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_job_history);
        initializeView();
        getUserData();
        setUpBackButton();
        getSupportActionBar().hide();
    }

    /**
     *  Back button from job history to user account
     */
    private void setUpBackButton() {
        backToUserList = findViewById(R.id.backToUserList);
        backToUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToUser = new Intent(EmployeeJobHistory.this, EmployeeProfileActivity.class);
                backToUser.putExtra("username", username);
                backToUser.putExtra("email", email);
                startActivity(backToUser);
            }
        });
    }

    /**
     * Initialize UI elements
     * Get username from user profile list
     * Initialize database and connect
     */
    private void initializeView() {
        completedJobListView = findViewById(R.id.completedJobListing);
        noJobTextView = findViewById(R.id.no_job_post);
        this.username = getIntent().getStringExtra("username");
        this.email = getIntent().getStringExtra("email");
        userDBRef = FirebaseDatabase.getInstance(EmployeeConstants.userDBLink).getReference();
        jobDBRef = FirebaseDatabase.getInstance(EmployeeConstants.jobDatabaseURL).getReference().child("jobs");
    }

    /**
     * Get data from the database
     */
    public void getUserData(){
        Query userQuery = userDBRef.orderByChild("username").equalTo(username);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                handleDataSnapshot(snapshot.child(username), username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Info", "onCancelled: Error", error.toException());
            }
        });
    }

    /**
     * Get the data and reference the job database
     * @param snapshot -> the database snapshot
     * @param username -> username to query
     */
    private void handleDataSnapshot(DataSnapshot snapshot, String username) {
        if(snapshot.exists()){
            ArrayList<String> jobPostID = new ArrayList<>();
            for (DataSnapshot jobIDSnapShot : snapshot.child("Approved").getChildren()) {
                jobPostID.add(jobIDSnapShot.child("jobKey").getValue().toString().trim());
            }

            /*
            If no job id retrieved
            Display that no job was found
             */
            if (jobPostID.size() == 0) {
                noJobDisplay();
            }

            for (String jobID : jobPostID) {
                Query jobPostQuery = jobDBRef.orderByKey().equalTo(jobID);
                if (jobPostQuery != null) {
                    jobPostQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ModelJobPosition jobPosition;
                            if(snapshot.exists()){
                               for(DataSnapshot jobSnapshot : snapshot.getChildren()){
                                   jobPosition = jobSnapshot.getValue(ModelJobPosition.class);
                                   completedJobList.add(jobPosition);
                                   showData(completedJobList, completedJobListView);
                               }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("JobInfo", "onCancelled: Error", error.toException());
                        }
                    });
                }
            }
        }
    }

    /**
     * Show the complete job list on to the corresponded UI
     * @param list -> completed job list
     * @param listView -> listview UI element
     */
    public void showData(ArrayList<ModelJobPosition> list, ListView listView) {
        noJobTextView.setVisibility(View.GONE);
        AdapterJobList adapterJobList = new AdapterJobList(EmployeeJobHistory.this, list);
        listView.setAdapter(adapterJobList);

    }

    public void noJobDisplay() {
        noJobTextView.setVisibility(View.VISIBLE);
    }

    /**
     * Show toast method for testing
     * @param message
     */
    private void showToast(String message){
        Toast.makeText(EmployeeJobHistory.this, message, Toast.LENGTH_SHORT).show();
    }
}