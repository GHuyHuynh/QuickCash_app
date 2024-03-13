package com.example.quick_cash.MainHomepages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PreferredJobActivity extends AppCompatActivity {
    // Define Firebase references and variables here

    EditText editTextKeyword;
    ListView listViewPreferredJobs;
    String username;
    FirebaseDatabase userDataBase;
    DatabaseReference userDatabaseReference;
    String jobDatabaseURL = "https://group-1-job-database-1c791-default-rtdb.firebaseio.com/";
    String userDatabaseURL = "https://group-1-quick-cash-default-rtdb.firebaseio.com/";
    List<String> preferredJobsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_jobs);
        getSupportActionBar().hide();

        // Retrieve the username from the intent
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("extractUsername");
        }

        // Initialize Firebase Database
        userDataBase = FirebaseDatabase.getInstance(userDatabaseURL);
        userDatabaseReference = userDataBase.getReference().child(username).child("PreferredJobs");

        // Bind views from layout file
        editTextKeyword = findViewById(R.id.editTextNewKeyword);
        listViewPreferredJobs = findViewById(R.id.listViewExistingKeywords);

        getPreferredJobsFromFirebase();
    }

    /**
     * Will get the list of Jobs the user has marked as preferred
     */
    private void getPreferredJobsFromFirebase() {
        DatabaseReference userPreferredJobsRef = FirebaseDatabase.getInstance().getReference().child(username).child("PreferredJobs");
        userPreferredJobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                preferredJobsList.clear(); // Clear the list before adding updated keywords
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    preferredJobsList.add(childSnapshot.getKey() + ":true");
                }
                displayPreferredJobs(preferredJobsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

    /**
     * Will display the list of preferred jobs the user created
     */
    private void displayPreferredJobs(List<String> preferredJobsList) {
        List<String> keywords = new ArrayList<>();
        for (String entry : preferredJobsList) {
            String[] parts = entry.split(":");
            if (parts.length == 2 && parts[1].equals("true")) {
                keywords.add(parts[0]);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, keywords);
        listViewPreferredJobs.setAdapter(adapter);
    }


    /**
     * Will read a new keyword from the user and add it to the database
     */
    public void addKeyword(View view) {
        String keyword = editTextKeyword.getText().toString().trim();
        if (!keyword.isEmpty()) {
            // Update the Firebase database with the new keyword
            userDatabaseReference.child(keyword).setValue("true");

            editTextKeyword.setText(""); // Clear the EditText after adding the keyword
        }
    }


    /**
     * Will delete a keyword the user has selected
     */
    public void deleteKeyword(View view) {
        // Get the selected item position from the ListView
        int selectedItemPosition = listViewPreferredJobs.getCheckedItemPosition();

        // Check if an item is selected
        if (selectedItemPosition != ListView.INVALID_POSITION) {
            // Retrieve the selected keyword from the adapter
            String selectedKeyword = ((ArrayAdapter<String>) listViewPreferredJobs.getAdapter()).getItem(selectedItemPosition);

            // Update the Firebase database to remove the keyword
            userDatabaseReference.child(selectedKeyword).removeValue();

            // Notify the adapter that the data set has changed
            ((ArrayAdapter<String>) listViewPreferredJobs.getAdapter()).notifyDataSetChanged();

            // Optionally, you can display a message or log the deletion
            Toast.makeText(this, "Deleted keyword: " + selectedKeyword, Toast.LENGTH_SHORT).show();
        } else {
            // Display a message or dialog to inform the user that no item is selected
            Toast.makeText(this, "Please select a keyword to delete", Toast.LENGTH_SHORT).show();
        }
    }



    public void returnToJobPostings(View view) {
        Intent jobPostings = new Intent(PreferredJobActivity.this, WorkerActivity.class);
        jobPostings.putExtra("extractUsername", username);
        startActivity(jobPostings);
    }
}
