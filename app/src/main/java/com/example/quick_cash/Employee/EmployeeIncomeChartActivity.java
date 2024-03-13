package com.example.quick_cash.Employee;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EmployeeIncomeChartActivity extends AppCompatActivity {
    /*Global Variables*/
    DatabaseReference userDBRef;
    String username;
    String email;
    Button backBtn;
    GraphView graphView;
    ArrayList<DataPoint> alist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_income_chart);
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        initialize();
        getUserData();
        setupBackBtn();
        getSupportActionBar().hide();
    }

    /**
     * Set Up the back button from Income Chart to User's page
     * Parses the email and username as extra for compilation on other end
     */
    public void setupBackBtn() {
        backBtn = findViewById(R.id.user_income_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeIncomeChartActivity.this, EmployeeProfileActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }

    /**
     * Initialization of views
     */
    private void initialize() {
        this.userDBRef = FirebaseDatabase.getInstance(EmployeeConstants.userDBLink).
                getReference().child(username);
        graphView = findViewById(R.id.idGraphView);
    }

    /**
     * Draws the chart utilizing a library.
     * the array list includes the data points of date + income amount
     * The function casts them into data points
     */
    private void drawChart() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for(DataPoint point : alist){
            series.appendData(point,false, alist.size(),false);
        }
        graphView.setTitle("User Income");
        graphView.setTitleColor(R.color.accentBlue);
        graphView.addSeries(series);
        // custom label formatter to show currency "EUR"
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return "Y" + super.formatLabel(value, isValueX); //this is the x axy which is the year
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + " CAD"; //the amount made that day
                }
            }
        });
    }

    /**
     * Gets the user data from the User DB
     * Amount made per day
     */
    private void getUserData() {
        userDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                handleDataSnapshot(snapshot.child("Approved")); //searches all the approved jobs
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("UserInfo", "onCancelled: Error", error.toException());
            }
        });
    }

    /**
     * gets the job salary plus the approval date
     * @param snapshot -> the user's approved jobs db snapshot
     */
    private void handleDataSnapshot(DataSnapshot snapshot) {
        if(snapshot.exists() && snapshot.getChildrenCount() > 0){
            for(DataSnapshot job : snapshot.getChildren()){
                String jobSalaryString = job.child("jobSalary").getValue(String.class);
                int jobSalaryInt = Integer.parseInt(jobSalaryString);
                String jobSalaryDateString = job.child("jobApprovalDate").getValue(String.class).substring(0,4);
                DataPoint temp = new DataPoint(Double.parseDouble(jobSalaryDateString), jobSalaryInt);
                alist.add(temp);
            }
        }
        drawChart(); //after all data is gathered, it will draw the chart
    }
}