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

import com.example.quick_cash.Adapter.AdapterWorkers;
import com.example.quick_cash.Adapter.AdapterResume;
import com.example.quick_cash.Credentials.LoginActivity;
import com.example.quick_cash.JobPost.AddJobActivity;
import com.example.quick_cash.Model.ModelResume;
import com.example.quick_cash.Model.ModelWorker;
import com.example.quick_cash.MyLocalArea.MyLocalAreaActivity;
import com.example.quick_cash.Notification.NotificationActivity;
import com.example.quick_cash.PayPal.PayPalPaymentActivity;
import com.example.quick_cash.Employee.EmployeeListActivity;
import com.example.quick_cash.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * When user logged in as Employer, this would be their homepage
 */
public class BossActivity extends AppCompatActivity {

    /**
     * Variables
     */
    private ListView listView;
    private ListView preferredEmployeesListView;
    private Switch switchPreferredEmployee;

    private List<ModelResume> rlist = new ArrayList<>();
    String username;
    static final String EXTRACTUSERNAME = "extractUsername";

    String resumeDatabaseURL = "https://group-1-job-database-1c791-default-rtdb.firebaseio.com/";

    FirebaseDatabase resumeDataBase;
    DatabaseReference resumeDatabaseReference;

    private FirebaseDatabase userDataBase;
    private DatabaseReference userDatabaseReference;
    private String userDatabaseURL = "https://group-1-quick-cash-default-rtdb.firebaseio.com/";
    static final String USER_INFO_STRING = "UserInfo";


    /**
     * Creates all the necessary functions for the activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss);

        SharedPreferences inAppUsername = getSharedPreferences("Pref", MODE_PRIVATE);
        username = inAppUsername.getString("key_username", null);


        initializeViews();

        loadData();
    }
    //Prevent pages from not refreshing
    @Override
    protected void onRestart() {
        super.onRestart();
        initializeViews();
        loadData();
    }

    /**
     * Initialize Views to their layout ids
     */
    private void initializeViews() {
        listView = findViewById(R.id.listView_workers); //identifies the list id
        preferredEmployeesListView = findViewById(R.id.listView_preferredEmployee);


        switchPreferredEmployee = findViewById(R.id.switch_preferredEmployee);

        // Set the initial visibility based on the switch state
        if (switchPreferredEmployee.isChecked()) {
            // If switch is checked, hide "All Jobs" ListView
            listView.setVisibility(View.GONE);
            preferredEmployeesListView.setVisibility(View.VISIBLE);
        } else {
            // If switch is not checked, show "All Jobs" ListView
            listView.setVisibility(View.VISIBLE);
            preferredEmployeesListView.setVisibility(View.GONE);
        }

        // Set up listener for switch changes
        switchPreferredEmployee.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // If switch is checked, hide "All Jobs" ListView
                listView.setVisibility(View.GONE);
                preferredEmployeesListView.setVisibility(View.VISIBLE);
            } else {
                // If switch is not checked, show "All Jobs" ListView
                listView.setVisibility(View.VISIBLE);
                preferredEmployeesListView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Loads the preference data and all job applications
     */
    private void loadData() {
        getDatabaseData();
        getPreferredResumes();
    }
    public void getPreferredResumes() {
        initializeDatabaseReferences();
        fetchUserKeywords();
    }

    /**
     * Gets the user keywords for their preferred employees
     */
    private void fetchUserKeywords() {
        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> userKeywords = extractUserKeywords(dataSnapshot);
                    fetchPreferredResumes(userKeywords);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(USER_INFO_STRING, "Fetch failed to get info");
            }
        });
    }

    /**
     * gets all the keywords from the db regarding preferred employees
     * @param dataSnapshot-> points to the users DB -> to their preferred employees
     * @return -> list of all their preferred employees
     */
    private List<String> extractUserKeywords(DataSnapshot dataSnapshot) {
        List<String> userKeywords = new ArrayList<>();
        for (DataSnapshot keywordSnapshot : dataSnapshot.getChildren()) {
            String keyword = keywordSnapshot.getValue().toString();
            userKeywords.add(keyword.toLowerCase());
        }
        return userKeywords;
    }

    /**
     * gets all the resumes that are matching the employees preferences
     * @param userKeywords-> all the preferred employees
     */
    private void fetchPreferredResumes(List<String> userKeywords) {
        resumeDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot resumeSnapshot) {
                List<ModelResume> preferredResumesList = new ArrayList<>();
                if (resumeSnapshot.exists() && resumeSnapshot.getChildrenCount() > 0) {
                    filterEmployeesByKeywords(resumeSnapshot, userKeywords, preferredResumesList);
                    showData(preferredResumesList,preferredEmployeesListView );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(USER_INFO_STRING, "Fetch failed to get required resumes");
            }
        });
    }

    /**
     * Filters all the applications and only displays the preferred ones
     * @param resumeSnapshot->points to job DB
     * @param userKeywords -> if the preferred list is hit, it will render all recommended lists.
     * @param preferredResumessList
     */
    private void filterEmployeesByKeywords(DataSnapshot resumeSnapshot, List<String> userKeywords, List<ModelResume> preferredResumessList) {
        preferredResumessList.clear();
        for (DataSnapshot resumeDataSnap : resumeSnapshot.getChildren()) {
            if (resumeDataSnap.child("employer").getValue().toString().equals(username) &&(resumeDataSnap.child("flag").getValue().toString().equals("NULL"))){

                String resumeEmployee = resumeDataSnap.child("employee").getValue().toString();
                String resumeEmployer = resumeDataSnap.child("employer").getValue().toString();
                String resumeJobName = resumeDataSnap.child("jobName").getValue().toString();
                String resumeJobKey  = resumeDataSnap.child("jobsKey").getValue().toString();
                String resumeFlag  = resumeDataSnap.child("flag").getValue().toString();
                String resumeSalary = resumeDataSnap.child("salary").getValue().toString();
                String resumeKey = resumeDataSnap.getKey();

                ModelResume resume =new ModelResume(resumeEmployer,resumeEmployee,resumeJobName,resumeFlag,resumeJobKey, "sent", resumeSalary);
                resume.setKey(resumeKey);

                for (String keyword : userKeywords) {

                    if (resumePostMatchesKeyword(resume, keyword)) {
                        preferredResumessList.add(resume);
                        break;
                    }
                }

            }
        }
    }

    /**
     * Checks if it matches the keyword
     * @param resumePost -> the job application
     * @param keyword-> the input for the preference
     * @return
     */
    private boolean resumePostMatchesKeyword(ModelResume resumePost, String keyword) {
        return resumePost.getEmployee().toLowerCase().contains(keyword);
    }

    /**
     * Initializes the DB pointers
     */
    private void initializeDatabaseReferences() {
        resumeDataBase = FirebaseDatabase.getInstance(resumeDatabaseURL);
        resumeDatabaseReference = resumeDataBase.getReference().child("resumes");
        userDataBase = FirebaseDatabase.getInstance(userDatabaseURL);
        userDatabaseReference = userDataBase.getReference().child(username).child("PreferredEmployees");
    }

    /**
     * renders the data for the list view
     */
    public void showData(List<?> list,ListView listView) {
        // Clear previous adapters
        listView.setAdapter(null);

        if (list != null && !list.isEmpty()) {
            if (list.get(0) instanceof ModelWorker) {
                AdapterWorkers adapterWorkers = new AdapterWorkers(BossActivity.this, (List<ModelWorker>) list);
                listView.setAdapter(adapterWorkers);
            } else if (list.get(0) instanceof ModelResume) {
                AdapterResume adapterResumes = new AdapterResume(BossActivity.this, (List<ModelResume>) list);
                listView.setAdapter(adapterResumes);
            }
        }
    }

    /**
     * Adds a navbar
     * @param menu-> search_bar.xml is used as navbar
     * @return -> true if it inflates correctly. Error handling is done by the super
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem search = menu.findItem(R.id.search_bar);
        search.setVisible(false);
        MenuItem preference = menu.findItem(R.id.addJobPreferences);
        preference.setVisible(false);
        MenuItem payment = menu.findItem(R.id.requestPayment);
        payment.setTitle("Send Payment");
        return true;
    }

    /**
     * Switches to the pages based on what is chosen.
     * @param item->each item in the menu
     * @return -> true if id is chosen, default is this activity
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            /**
                Switch to worker activity on click and carry username over
             */
            case R.id.Worker:
                Intent boss = new Intent(BossActivity.this, WorkerActivity.class);
                boss.putExtra(EXTRACTUSERNAME, username);
                startActivity(boss);
                return true;
            /**
                Switch add job activity on click and carry the username over
             */
            case R.id.addJobPosting:
                Intent addJob = new Intent(BossActivity.this, AddJobActivity.class);
                addJob.putExtra(EXTRACTUSERNAME, username);
                startActivity(addJob);
                return true;

            /**
                Switch to log out activity and logged out the user
             */
            case R.id.Logout:
                Intent logout = new Intent(BossActivity.this, LoginActivity.class);
                SharedPreferences preferences = getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.remove("username");
                editor.apply();

                startActivity(logout);
                return true;

            case R.id.my_local_activity:
                Intent myArea = new Intent(BossActivity.this, MyLocalAreaActivity.class);
                myArea.putExtra("PREVIOUS_ACTIVITY",BossActivity.class.getName());
                startActivity(myArea);
                return true;

            case R.id.user_list:
                Intent userList = new Intent(this, EmployeeListActivity.class);
                userList.putExtra("PREVIOUS_ACTIVITY",BossActivity.class.getName());
                startActivity(userList);
                return true;
            case R.id.requestPayment:
                Intent payment = new Intent(this, PayPalPaymentActivity.class);
                payment.putExtra("activity", "BossActivity");
                startActivity(payment);
                return true;

            case R.id.message:
                Intent dealResume =new Intent(BossActivity.this, NotificationActivity.class);
                dealResume.putExtra(EXTRACTUSERNAME, username);
                startActivity(dealResume);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Gets the DB info for all the applications
     */
    public void getDatabaseData(){
        resumeDataBase = FirebaseDatabase.getInstance(resumeDatabaseURL);
        resumeDatabaseReference = resumeDataBase.getReference().child("resumes");
        resumeDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot resumeSnapshot) {
                rlist.clear();
                for (DataSnapshot resumeDataSnap : resumeSnapshot.getChildren()){

                    if (resumeDataSnap.child("employer").getValue().toString().equals(username) &&(resumeDataSnap.child("flag").getValue().toString().equals("NULL"))){

                        String resumeEmployee = resumeDataSnap.child("employee").getValue().toString();
                        String resumeEmployer = resumeDataSnap.child("employer").getValue().toString();
                        String resumeJobName = resumeDataSnap.child("jobName").getValue().toString();
                        String resumeJobKey  = resumeDataSnap.child("jobsKey").getValue().toString();
                        String resumeFlag  = resumeDataSnap.child("flag").getValue().toString();
                        String resumeSalary = resumeDataSnap.child("salary").getValue().toString();
                        String resumeKey = resumeDataSnap.getKey();

                        ModelResume resume =new ModelResume(resumeEmployer,resumeEmployee,resumeJobName,resumeFlag,resumeJobKey, "sent", resumeSalary);
                        resume.setKey(resumeKey);

                        rlist.add(resume);
                    }

                }
                showData(rlist,listView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(USER_INFO_STRING, "Fetch failed to get resumes");
            }
        });

    }

    /**
     *
     * @param flag -> a switch for whether or not the job is approved or denied
     * @param resumeKey -> key of the application
     * @param employeeName -> employees user name
     * @param jobKey -> job key
     * @param jobSalary -> how much money the job is worth
     */
    public void submitResumeFlag(String flag, String resumeKey, String employeeName, String jobKey, String jobSalary){
        DatabaseReference resumeFlagRef = resumeDataBase.getReference().child("resumes");
        DatabaseReference flagRef = resumeFlagRef.child(resumeKey).child("flag");
        if (flag.equals("Approve")) { // on approve the user will be redirected to pay page
            flagRef.setValue("Approve");
            Intent intent = new Intent(this, PayPalPaymentActivity.class);
            intent.putExtra("activity", "BossActivity");
            DatabaseReference userDatabaseRef =
                    FirebaseDatabase.getInstance().getReference().child(employeeName).child("Approved");

            DatabaseReference userRef = userDatabaseRef.push();
            userRef.child("jobKey").setValue(jobKey);
            userRef.child("jobSalary").setValue(jobSalary);

            Date today = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(today);
            userRef.child("jobApprovalDate").setValue(strDate);

            startActivity(intent);
        } else { //otherwise they will just refuse the work
            flagRef.setValue("Refuse");
        }
    }
}
