package com.example.quick_cash.Employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quick_cash.Adapter.AdapterFeedbackList;
import com.example.quick_cash.MainHomepages.BossActivity;
import com.example.quick_cash.Credentials.LoginActivity;
import com.example.quick_cash.Model.ModelFeedback;
import com.example.quick_cash.MyLocalArea.MyLocalAreaActivity;
import com.example.quick_cash.R;
import com.example.quick_cash.AppUser.AppUser;
import com.example.quick_cash.MainHomepages.WorkerActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeProfileActivity extends AppCompatActivity {
    /*Global Variables*/
    String username;
    String email;
    Employee employee;
    TextView usernameTextView;
    TextView emailTextView;
    TextView userTotalSalary;
    Button jobHistoryButton;
    Button jobPostingsButton;
    Button userRatingButton;
    RatingBar userRating;
    DatabaseReference userDBRef;
    Button backBtn;
    float userRatingFloat;

    ListView feedbackListView;
    ArrayList<ModelFeedback> completedFeedbackList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        this.employee = getData();

        initializeTextView();
        getUserData();

        fillInfo(this.employee);
        setJobHistoryButton();
        setJobPostingsButton();
        setRateUserButton();
        setIncomeHistoryButton();

        getSupportActionBar().hide();
        setupBackBtn();
    }

    /**
     * Button to go to user income history by clicking the user total income
     */
    private void setIncomeHistoryButton() {
        userTotalSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeProfileActivity.this, EmployeeIncomeChartActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }

    /**
     * Button to go back to user list activity
     */
    public void setupBackBtn() {
        backBtn = findViewById(R.id.user_profile_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back2UserList = new Intent(EmployeeProfileActivity.this, EmployeeListActivity.class);
                back2UserList.putExtra("PREVIOUS_ACTIVITY", "");
                startActivity(back2UserList);
            }
        });
    }

    /**
     * 1. This function will gather information such as
     *      1. User Rate
     *      2. User Feedback
     *      3. User Income
     * 2. Each of those is handled by a different handler
     */
    private void getUserData() {
        // Assuming userDBRef is a DatabaseReference pointing to the "rate" node under a specific user
        userDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                handleRatingSnapshot(snapshot.child("userRating").child("rate"));
                handleFeedbackSnapshot(snapshot.child("userRating").child("feedback"));
                handleDataSnapshot(snapshot.child("Approved"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("UserInfo", "onCancelled: Error", error.toException());
            }
        });
    }

    /**
     * This computes the total salary earned by the user and displays it on the text view
     * @param snapshot -> this is the approved snapshot that includes the salary + date of each job
     * Text view is also clickable for chart
     */
    private void handleDataSnapshot(DataSnapshot snapshot) {
        int totalSum = 0;
        if(snapshot.exists() && snapshot.getChildrenCount() > 0){
            for(DataSnapshot job : snapshot.getChildren()){
                String jobSalaryString = job.child("jobSalary").getValue(String.class);
                int jobSalaryInt = Integer.parseInt(jobSalaryString);
                totalSum += jobSalaryInt;
            }
        }
        userTotalSalary.setText("Total Income " + String.valueOf(totalSum) + " CAD");
    }

    /**
     * Gathers all the strings inputted by the users and displays them using showData()
     * @param snapshot->This snapshot includes all the user feedbacks + the person who wrote them
     */
    private void handleFeedbackSnapshot(DataSnapshot snapshot){
        if(snapshot.exists() && snapshot.getChildrenCount()>0){
            ModelFeedback feedback;
            for(DataSnapshot temp : snapshot.getChildren()){
                String username = temp.getKey().toString();
                String comment = temp.getValue(String.class);
                if(comment.isEmpty()){
                    break;
                }
                feedback = new ModelFeedback(username, comment);
                completedFeedbackList.add(feedback);
                showData(completedFeedbackList, feedbackListView);
            }

        }

    }

    /**
     * Computes the average rating of the user based on all the rating they have received.
     * @param snapshot -> this snapshot includes all the ratings and how many ratings the user includes
     */
    private void handleRatingSnapshot(DataSnapshot snapshot) {
        if (snapshot.exists()) {
            try{
                userRatingFloat = snapshot.getValue(Float.class); //gets the user total rating
                if(userRatingFloat != 0.0)
                {
                    this.userRating.setRating((float)userRatingFloat);
                }
            }catch (DatabaseException e) {
                Log.e("UserInfo", "Error converting to float", e);
            }
        } else {
            Log.e("UserInfo", "Rate value does not exist in snapshot");
        }
    }

    /**
     *
     * @param list -> the array list that will include all the user feedback
     * @param listView -> the ViewList that will display all the user feedback
     */
    public void showData(ArrayList<ModelFeedback> list, ListView listView){
        AdapterFeedbackList adapterFeedbackList = new AdapterFeedbackList(EmployeeProfileActivity.this, list);
        listView.setAdapter(adapterFeedbackList);
    }

    /**
     * Sets up the rate user button so that users can rate each other
     */
    private void setRateUserButton() {
        this.userRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rateUser = new Intent(EmployeeProfileActivity.this, EmployeeRatingActivity.class);
                rateUser.putExtra("username", username);
                rateUser.putExtra("email", email);
                startActivity(rateUser);
            }
        });
    }

    /**
     * Get data from user listing
     * @return -> AppUser object with all the data
     */
    public Employee getData() {
        Intent data = this.getIntent();
        if (data == null) {
            Toast.makeText(this, "No User Details Received Error", Toast.LENGTH_SHORT).show();
        } else {
            this.username = data.getStringExtra("username");
            this.email = data.getStringExtra("email");
        }
        Employee userData = new Employee(this.username, this.email);

        return userData;
    }

    /**
     * Link all Views with correspond layout ids
     */
    public void initializeTextView() {
        this.feedbackListView = findViewById(R.id.feedbackList);
        this.usernameTextView = findViewById(R.id.profile_username);
        this.emailTextView = findViewById(R.id.profile_email);
        this.jobHistoryButton = findViewById(R.id.job_history_button);
        this.jobPostingsButton = findViewById(R.id.job_postings_button);
        this.userRatingButton = findViewById(R.id.rateUser);
        this.userRating = (RatingBar)findViewById(R.id.userRating);
        this.userTotalSalary = findViewById(R.id.user_total_salary);
        this.userDBRef = FirebaseDatabase.getInstance(EmployeeConstants.userDBLink).
                getReference().child(username);
    }

    /**
     * Fill information into text view
     * @param user -> AppUser object of this class
     */
    public void fillInfo(AppUser user) {
        this.usernameTextView.setText(user.getUsername());
        this.emailTextView.setText(user.getEmail());
    }

    /**
     * Sets up the total user job history that displays all their completed jobs
     */
    public void setJobHistoryButton() {
        this.jobHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent completedJobHistory = new Intent(EmployeeProfileActivity.this, EmployeeJobHistory.class);
                completedJobHistory.putExtra("username", username);
                completedJobHistory.putExtra("email", email);
                startActivity(completedJobHistory);
            }
        });

    }

    /**
     *
     */
    public void setJobPostingsButton(){
        this.jobPostingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jobPostings = new Intent(EmployeeProfileActivity.this, EmployeeJobPostedHistory.class);
                jobPostings.putExtra("username", employee.getUsername());
                jobPostings.putExtra("email", employee.getEmail());
                startActivity(jobPostings);
            }
        });
    }

    /**
     * Set up button menu for EmployeeListActivity
     * @param menu -> menu of the app
     * @return -> app wide menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_bar, menu);

        MenuItem item = menu.findItem(R.id.addJobPosting);
        item.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.message);
        item2.setVisible(false);
        MenuItem item3 = menu.findItem(R.id.addJobPreferences);
        item3.setVisible(false);
        MenuItem item4 = menu.findItem(R.id.search_bar);
        item4.setVisible(false);

        return true;
    }

    /**
     * Set up intent for all menu options
     * @param item -> menu item: the app wide menu
     * @return -> true for successful menu options click, recursive if non selected
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            /**
             Switch to boss activity on click and carry username over
             */
            case R.id.Boss:
                Intent boss = new Intent(this, BossActivity.class);
                startActivity(boss);
                return true;

            /**
             Switch to log out activity and logged out the user
             */
            case R.id.Logout:
                Intent logout = new Intent(this, LoginActivity.class);
                SharedPreferences preferences = getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.remove("username");
                editor.apply();

                startActivity(logout);
                return true;

            case R.id.Worker:
                Intent worker = new Intent(this, WorkerActivity.class);
                startActivity(worker);
                return true;

            case R.id.my_local_activity:
                Intent myArea = new Intent(this, MyLocalAreaActivity.class);
                startActivity(myArea);
                return true;

            case R.id.user_list:
                Intent userList = new Intent(this, EmployeeListActivity.class);
                startActivity(userList);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}