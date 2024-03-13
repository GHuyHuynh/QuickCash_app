package com.example.quick_cash.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.R;
import com.example.quick_cash.MainHomepages.WorkerActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * This activity is for searching by parameters. it implements View.OnClickListener for
 * code efficiency, since no matter what a user has to click a button.
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Variables
     */
    private EditText jobName;
    private EditText jobDate;
    private EditText jobDescription;
    private EditText jobLocation;
    private EditText jobSalary;

    /**
     * This is a default onCreate override
     * Ensures that the buttons are set-up, and all the variables above
     * are correctly set up.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_engine);
        initialize();
        redirectToJob();
        setUpButton();
        getSupportActionBar().hide();
    }

    /**
     * initializes variables by finding their id in the XML file
     */

    public void initialize() {
        jobName = findViewById(R.id.searchByName);
        jobDate = findViewById(R.id.searchByDate);
        jobDescription = findViewById(R.id.searchByDescription);
        jobLocation = findViewById(R.id.searchByLocation);
        jobSalary = findViewById(R.id.searchBySalary);
    }

    /**
     * Setups the search button that will allow users to find the jobs
     */
    public void setUpButton(){
        Button searchBtn = findViewById(R.id.search_button);
        searchBtn.setOnClickListener(this);
    }

    /**
     * Just a simple return button in case the user doesn't want to search
     */
    public void redirectToJob() {
        TextView jobRedirectText = findViewById(R.id.redirectBack);

        jobRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(SearchActivity.this, WorkerActivity.class);
            startActivity(intent);
        });
    }

    /**
     * toString() method to avoid constantly recasting
     * @param field -> Total of 5 EditText fields the user has to fill.
     * @return a string of what is typed in by the user in the field
     */
    private String toString(EditText field){
        return field.getText().toString().trim();
    }

    /**
     * When the user clicks the button, all the user input will be
     * converted to a string, and set into a map. Map is then
     * converted to a gson file (Since can't set Map as serializable)
     * this is then set as an extra for the next activity.
     * On-click it will switch to JobQueueActivity
     * @param view -> this is just the default view for the activity
     */
    @Override
    public void onClick(View view) {
        Map<String, Object> map = new HashMap<>();
        //The map will take either an actual string or an empty string ""
        map.put("jobName", toString(jobName));
        map.put("jobDescription", toString(jobDescription));
        map.put("jobDate", toString(jobDate));
        map.put("jobLocation", toString(jobLocation));
        map.put("jobSalary", toString(jobSalary));

        Intent intent = new Intent(SearchActivity.this, JobQueryActivity.class);
        Gson gson = new Gson();
        String js = gson.toJson(map);
        //a way to parse an object into .putExtra() if it can't be
        //made a serializable
        intent.putExtra("map", js);
        startActivity(intent);
    }
}
