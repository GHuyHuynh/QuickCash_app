package com.example.quick_cash.Search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.Adapter.AdapterPosition;
import com.example.quick_cash.MainHomepages.BossActivity;
import com.example.quick_cash.Credentials.LoginActivity;
import com.example.quick_cash.JobPost.JobPostDetails;
import com.example.quick_cash.Model.ModelJobPosition;
import com.example.quick_cash.R;
import com.example.quick_cash.MainHomepages.WorkerActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class pull all job listing on the database and search by parameters
 * After that, display all job posts
 */
public class JobQueryActivity extends AppCompatActivity {

    /**
     * Variables
     */
    FirebaseDatabase database;
    DatabaseReference dataRef;
    String jobDatabaseURL = "https://group-1-job-database-1c791-default-rtdb.firebaseio.com/";
    private List<ModelJobPosition> plist = new ArrayList<>();
    private ListView listView;
    String username;
    int emptyStringCounter = 0;//this is for checking how many of the inputs are empty

    /**
     * On create we need to initialize database, reset the list in which we store queries.
     * We need to also grab the extra parsed in from SearchActivity.java
     * gson is used for non serializable parses
     * this will also generate the desired jobs based on input
     * @param savedInstanceState ->
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        listView = findViewById(R.id.listView_positions);
        databaseInit();
        plist.clear(); //clear the list
        //grab the extra from the previous intent
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(getIntent().getStringExtra("map"), Map.class);
        //compute the desired jobs
        getDesiredJob(map);
    }

    /**
     * display all the objects store in the list
     * @param list->List that stores Model_Job_Position
     */
    public void showData(List<ModelJobPosition> list){
        AdapterPosition adapterPositions =new AdapterPosition(JobQueryActivity.this,list);
        listView .setAdapter(adapterPositions);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ModelJobPosition item = (ModelJobPosition) adapterView.getItemAtPosition(i);
                Intent jobDetails = new Intent(JobQueryActivity.this, JobPostDetails.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(item);
                jobDetails.putExtra("extractJob", myJson);
                startActivity(jobDetails);
            }
        });
    }

    /**
     * initializes the database for searching
     */
    protected void databaseInit() {
        database = FirebaseDatabase.getInstance(jobDatabaseURL);
        dataRef = database.getReference();
    }

    /**
     * This function is a bit complex: here is the detail explanation
     * It will go through each key(5) in the Map that we mapped in previous activity
     * Check if it is empty or not, if it is not empty we are gonna query the database for it
     * if it exists in the database, the function will add a value listener for it
     * this listener will grab all the children associated with this value, and store it
     * in a Model_Job_Position (format in which we store our jobs). This will be added to the list
     * that we use to display. Which will also be displayed. If the job is not found we gonna use
     * log cat for logging it
     * otherwise we also handle cancelling of the search
     * and we also check if all 5 keys were mapped to empty strings, if that is the case we
     * reroute the user back to the worker page, where they can see all jobs, and a toast
     * displays that there are no inputs.
     * @param map-> grabs the Hashmap parsed in from the previous activity
     */
    public void getDesiredJob(Map<String, Object> map) {
        String item;
        for(Map.Entry<String, Object> set: map.entrySet()){
            if(!set.getValue().toString().trim().equals("")){
                item = set.getValue().toString().trim();
                Query jobQuery = dataRef.child("jobs")
                        .orderByChild(set.getKey().trim())
                        .equalTo(item);

                jobQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot jobSnapshot) {
                        if (jobSnapshot.exists() && jobSnapshot.getChildrenCount() > 0) {
                            ModelJobPosition jobPost;
                            for (DataSnapshot jobDataSnap : jobSnapshot.getChildren()) {
                                jobPost = jobDataSnap.getValue(ModelJobPosition.class);
                                plist.add(jobPost);
                                showData(plist);
                            }
                        } else {
                            Log.e("Job", "NotFound");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("JobQueryActivity", error.getMessage());
                    }
                });
            }else{
                emptyStringCounter++;
                Log.e("JobQueryActivity", set.getValue().toString().trim() + "is empty");
            }
        }
        //checks if there are no user inputs
        checkForEmpty(emptyStringCounter);
    }

    /**
     *
     * @param emptyStringCounter -> int counter that is incrememented everytime a user inputs an empty string
     *                           if it its 5 we change activity back to worker.
     */
    public void checkForEmpty(int emptyStringCounter){
        if(emptyStringCounter==5){
            Toast.makeText(JobQueryActivity.this, "No Inputs", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(JobQueryActivity.this, WorkerActivity.class);
            startActivity(intent);
        }
    }

    /**
     *
     * @param menu->
     * @return True if its populated correctly.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_bar, menu);

        //disable AddJob button
        MenuItem item = menu.findItem(R.id.addJobPosting);
        MenuItem search = menu.findItem(R.id.search_bar);
        item.setVisible(false); //addJobPosting is hidden
        search.setVisible(false); //searchBar is also hidden since there is no point
        return true;
    }

    /**
     * @param item->checks which item is selected and does the intent
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.Boss){
            Intent boss = new Intent(JobQueryActivity.this, BossActivity.class);
            boss.putExtra("extractUsername", username);
            startActivity(boss);
            return true;
        } else if(item.getItemId()==R.id.Worker) {
            Intent worker = new Intent(JobQueryActivity.this, WorkerActivity.class);
            worker.putExtra("extractUsername", username);
            startActivity(worker);
            return true;
        } else if(item.getItemId()==R.id.Logout){
            Intent logout = new Intent(JobQueryActivity.this, LoginActivity.class);
            SharedPreferences preferences = getSharedPreferences("checkbox", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("remember", "false");
            editor.remove("username");
            editor.apply();
            startActivity(logout);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
