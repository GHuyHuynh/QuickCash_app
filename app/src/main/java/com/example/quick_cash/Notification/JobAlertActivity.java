package com.example.quick_cash.Notification;

import static com.example.quick_cash.MyLocalArea.MyLocalAreaActivity.geoLocate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.Adapter.AdapterPosition;
import com.example.quick_cash.JobPost.JobPostDetails;
import com.example.quick_cash.Model.ModelJobPosition;

import com.example.quick_cash.MyLocalArea.MyLocalAreaActivity;
import com.example.quick_cash.R;
import com.example.quick_cash.MainHomepages.WorkerActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class JobAlertActivity extends AppCompatActivity {

    private ListView preferredJobsListView;
    String username;
    FirebaseDatabase jobDataBase;
    DatabaseReference jobDatabaseReference;
    String jobDatabaseURL = "https://group-1-job-database-1c791-default-rtdb.firebaseio.com/";
    FirebaseDatabase userDataBase;
    DatabaseReference userDatabaseReference;
    String userDatabaseURL = "https://group-1-quick-cash-default-rtdb.firebaseio.com/";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_alert);

        Intent data = getIntent();
        username = data.getStringExtra("extractUsername");

        preferredJobsListView = findViewById(R.id.listView_preferredJobs);

        Button btnEnableLocationTracking = findViewById(R.id.btnEnableLocationTracking);
        btnEnableLocationTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enableLocationIntent = new Intent(JobAlertActivity.this, MyLocalAreaActivity.class);
                startActivity(enableLocationIntent);
            }
        });

        Button btnReturnToWorkerActivity = findViewById(R.id.btnReturnToWorkerActivity);
        btnReturnToWorkerActivity.setBackgroundColor(Color.TRANSPARENT);
        btnReturnToWorkerActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToWorkerActivity();
            }
        });

        // Retrieve and display preferred jobs
        getPreferredJobs();
    }

    /**
     * Callback interface for location-related operations.
     */
    private interface LocationCallback {
        /**
         * Invoked when the location is received.
         * @param distance The distance.
         */
        void onLocationReceived(double distance);
    }

    /**
     * Retrieves preferred jobs from the database.
     */
    public void getPreferredJobs() {
        jobDataBase = FirebaseDatabase.getInstance(jobDatabaseURL);
        jobDatabaseReference = jobDataBase.getReference().child("jobs");
        userDataBase = FirebaseDatabase.getInstance(userDatabaseURL);
        userDatabaseReference = userDataBase.getReference().child(username).child("PreferredJobs");
        fetchUserKeywords();
    }

    /**
     * Fetches user keywords from the database.
     */
    private void fetchUserKeywords() {
        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> userKeywords = extractUserKeywords(dataSnapshot);
                    fetchAllJobs(userKeywords);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });
    }

    /**
     * Extracts user keywords from the database snapshot.
     * @param dataSnapshot The data snapshot.
     * @return List of user keywords.
     */
    private List<String> extractUserKeywords(DataSnapshot dataSnapshot) {
        List<String> userKeywords = new ArrayList<>();
        for (DataSnapshot keywordSnapshot : dataSnapshot.getChildren()) {
            String keyword = keywordSnapshot.getKey();
            userKeywords.add(keyword.toLowerCase());
        }
        return userKeywords;
    }

    /**
     * Fetches all jobs from the database.
     * @param userKeywords The user keywords.
     */
    private void fetchAllJobs(List<String> userKeywords) {
        jobDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot jobSnapshot) {
                List<ModelJobPosition> allJobsList = new ArrayList<>();

                if (jobSnapshot.exists() && jobSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot jobDataSnap : jobSnapshot.getChildren()) {
                        ModelJobPosition jobPost = jobDataSnap.getValue(ModelJobPosition.class);
                        allJobsList.add(jobPost);
                    }

                    calculateDistancesAndFilter(allJobsList, userKeywords);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });
    }

    /**
     * Calculates distances and filters jobs based on keywords and distances.
     * @param allJobsList The list of all jobs.
     * @param userKeywords The user keywords.
     */
    private void calculateDistancesAndFilter(List<ModelJobPosition> allJobsList, List<String> userKeywords) {
        List<ModelJobPosition> preferredJobsList = new ArrayList<>();
        AtomicInteger totalAsyncCalls = new AtomicInteger(allJobsList.size());
        AtomicInteger completedAsyncCalls = new AtomicInteger(0);

        for (ModelJobPosition jobPost : allJobsList) {
            distanceSetup(jobPost.getJobLocation(), distance -> {
                for (String keyword : userKeywords) {
                    if (jobPostMatchesKeyword(jobPost, keyword) && distance <= 25) {
                        preferredJobsList.add(jobPost);
                        break;
                    }
                }

                completedAsyncCalls.incrementAndGet();

                if (completedAsyncCalls.get() == totalAsyncCalls.get()) {
                    showPreferredJobs(preferredJobsList);
                }
            });
        }
    }

    /**
     * Checks if a job post matches a keyword.
     * @param jobPost The job post.
     * @param keyword The keyword.
     * @return True if the job post matches the keyword, false otherwise.
     */
    private boolean jobPostMatchesKeyword(ModelJobPosition jobPost, String keyword) {
        return jobPost.getJobName().toLowerCase().contains(keyword)
                || jobPost.getJobDes().toLowerCase().contains(keyword)
                || jobPost.getJobEmployer().toLowerCase().contains(keyword)
                || jobPost.getJobLocation().toLowerCase().contains(keyword);
    }

    /**
     * Displays preferred jobs in a ListView.
     * @param preferredJobsList The list of preferred jobs to display.
     */
    private void showPreferredJobs(List<ModelJobPosition> preferredJobsList) {
        AdapterPosition adapterPositions = new AdapterPosition(JobAlertActivity.this, preferredJobsList);
        preferredJobsListView.setAdapter(adapterPositions);

        preferredJobsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ModelJobPosition clickedJob = preferredJobsList.get(position);
                openJobDetails(clickedJob);
            }
        });
    }

    /**
     * Opens the details of a job post.
     * @param job The job post to display.
     */
    private void openJobDetails(ModelJobPosition job) {
        Intent jobDetailsIntent = new Intent(JobAlertActivity.this, JobPostDetails.class);
        jobDetailsIntent.putExtra("extractJob", job);
        startActivity(jobDetailsIntent);
    }

    /**
     * Checks if a job is within a specified radius.
     * @param jobLocation The location of the job.
     * @param callback The callback to invoke with the result.
     */
    private void distanceSetup(String jobLocation, JobAlertActivity.LocationCallback callback) {
        Address jobAddress = geoLocate(jobLocation, getApplicationContext());

        DatabaseReference userLocationRef = userDataBase.getReference().child(username);

        if (jobAddress == null) {
            callback.onLocationReceived(Double.MAX_VALUE);
            return;
        }

        userLocationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double userLatitude = dataSnapshot.child("Latitude").getValue(Double.class);
                    double userLongitude = dataSnapshot.child("Longitude").getValue(Double.class);

                    double jobLatitude = jobAddress.getLatitude();
                    double jobLongitude = jobAddress.getLongitude();

                    double distance = calculateHaversineDistance(userLatitude, userLongitude, jobLatitude, jobLongitude);
                    callback.onLocationReceived(distance);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });
    }

    /**
     * Calculates the Haversine distance between two sets of latitude and longitude coordinates.
     * @param lat1 Latitude of the first point.
     * @param lon1 Longitude of the first point.
     * @param lat2 Latitude of the second point.
     * @param lon2 Longitude of the second point.
     * @return The distance in kilometers.
     */
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in kilometers
    }

    /**
     * Method to handle the button click and navigate back to WorkerActivity
     */
    private void returnToWorkerActivity() {
        Intent workerActivityIntent = new Intent(JobAlertActivity.this, WorkerActivity.class);
        workerActivityIntent.putExtra("extractUsername", username);
        startActivity(workerActivityIntent);
    }
}
