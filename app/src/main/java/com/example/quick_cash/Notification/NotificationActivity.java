package com.example.quick_cash.Notification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.Adapter.AdapterResumeResult;
import com.example.quick_cash.Model.ModelResume;

import com.example.quick_cash.R;
import com.example.quick_cash.MainHomepages.WorkerActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private List<ModelResume> rlist = new ArrayList<>();
    private ListView listView;
    String username;
    FirebaseDatabase resumeDataBase;
    DatabaseReference resumeDatabaseReference;
    String resumeDatabaseURL = "https://group-1-job-database-1c791-default-rtdb.firebaseio.com/";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Intent data = getIntent();
        username = data.getStringExtra("extractUsername");

        getDatabaseData();
        listView = findViewById(R.id.resume_result_willRead);

        Button btnReturnToWorkerActivity = findViewById(R.id.btnReturnToWorkerActivity);
        btnReturnToWorkerActivity.setBackgroundColor(Color.TRANSPARENT);
        btnReturnToWorkerActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToWorkerActivity();
            }
        });

        Button btnReadLocalJobAlerts = findViewById(R.id.btnReadLocalJobAlerts);
        btnReadLocalJobAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jobAlertIntent = new Intent(NotificationActivity.this, JobAlertActivity.class);
                jobAlertIntent.putExtra("extractUsername", username);
                startActivity(jobAlertIntent);
            }
        });

    }

    /**
     * Retrieves resume data from the database.
     */
    public void getDatabaseData(){
        resumeDataBase = FirebaseDatabase.getInstance(resumeDatabaseURL);
        resumeDatabaseReference = resumeDataBase.getReference().child("resumes");
        resumeDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot resumeSnapshot) {
                rlist.clear();
                for (DataSnapshot resumeDataSnap : resumeSnapshot.getChildren()){
                    if (resumeDataSnap.child("employee").getValue().toString().equals(username) &&!(resumeDataSnap.child("flag").getValue().toString().equals("NULL"))&&!(resumeDataSnap.child("status").getValue().toString().equals("Read"))){
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
                showData(rlist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // No message
            }
        });
    }

    /**
     * Displays resume data.
     * @param list The list of resumes to display.
     */
    public void showData(List<ModelResume> list){
        if (list != null ){
            AdapterResumeResult adapterResumesResult =new AdapterResumeResult(NotificationActivity.this,list);
            listView.setAdapter(adapterResumesResult);
        } else {
            Toast.makeText(NotificationActivity.this, "no content", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Submits the status of a resume.
     * @param status The status to submit.
     * @param resumeKey The key of the resume.
     */
    public void submitResumeStatus(String status,String resumeKey){
        FirebaseDatabase resumeDataBase=FirebaseDatabase.getInstance(resumeDatabaseURL);
        DatabaseReference resumeFlagRef = resumeDataBase.getReference().child("resumes");
        DatabaseReference flagRef = resumeFlagRef.child(resumeKey).child("status");
        if (status.equals("Ignore")) {
            flagRef.setValue("Ignore");
        } else {
            flagRef.setValue("Read");
        }
    }

    /**
     * Method to handle the button click and navigate back to WorkerActivity
     */
    private void returnToWorkerActivity() {
        Intent workerActivityIntent = new Intent(NotificationActivity.this, WorkerActivity.class);
        workerActivityIntent.putExtra("extractUsername", username);
        startActivity(workerActivityIntent);
    }
}
