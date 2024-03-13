package com.example.quick_cash.Employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.Adapter.AdapterUserList;
import com.example.quick_cash.AppUser.AppUser;
import com.example.quick_cash.MainHomepages.BossActivity;
import com.example.quick_cash.Model.ModelJobPosition;
import com.example.quick_cash.R;
import com.example.quick_cash.MainHomepages.WorkerActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RecommendedEmployeesActivity extends AppCompatActivity {

    private ListView recommendedEmployeesListView;
    ArrayList<AppUser> userArrayList = new ArrayList<>();
    ArrayList<ModelJobPosition> preferredJobsList;
    String username;
    String employer;
    ModelJobPosition jobPost;
    Gson gson = new Gson();
    private FirebaseDatabase userDatabase;
    private DatabaseReference userDatabaseReference;
    private String userDatabaseURL = "https://group-1-quick-cash-default-rtdb.firebaseio.com/";
    String PREVIOUS_ACTIVITY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggested_employees);

        getSupportActionBar().hide();

        Intent data = getIntent();
        employer = data.getStringExtra("extractEmployer");
        jobPost = gson.fromJson(getIntent().getStringExtra("extractJob"), ModelJobPosition.class);
        PREVIOUS_ACTIVITY = data.getStringExtra("PREVIOUS_ACTIVITY");

        recommendedEmployeesListView = findViewById(R.id.userListing);

        Button btnReturnToEmployerActivity = findViewById(R.id.btnReturnToEmployerActivity);
        btnReturnToEmployerActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToEmployerActivity();
            }
        });

        // Retrieve and display recommended employees
        getRecommendedEmployees();
    }

    /**
     * Retrieves recommended employees from the database.
     */
    /**
     * Connect to user credentials database
     * Get all the user and add to userArrayList
     * Show all the user
     */
    protected void getRecommendedEmployees() {
        userDatabase = FirebaseDatabase.getInstance(userDatabaseURL);
        userDatabaseReference = userDatabase.getReference();

        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        AppUser userObject = new Employee(null, null);
                        username = dataSnapshot.getKey().toString();
                        if (!username.equalsIgnoreCase(employer)) {
                            userObject.setUsername(username);
                            String email = dataSnapshot.child("email").getValue().toString();
                            userObject.setEmail(email);
                            userArrayList.add(userObject);
                        }
                    }
                }
                showData(userArrayList, recommendedEmployeesListView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * Displays recommended employees in a ListView.
     */
    public void showData(ArrayList<AppUser> list, ListView listView){
        if (PREVIOUS_ACTIVITY.equals(WorkerActivity.class.getName().toString())) {
            AdapterUserList adapterUserListView = new AdapterUserList(RecommendedEmployeesActivity.this, list, "worker");
            listView.setAdapter(adapterUserListView);
            listView.setClickable(true);
        } else if (PREVIOUS_ACTIVITY.equals(BossActivity.class.getName().toString())) {
            AdapterUserList adapterUserListView = new AdapterUserList(RecommendedEmployeesActivity.this, list, "boss");
            listView.setAdapter(adapterUserListView);
            listView.setClickable(true);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RecommendedEmployeesActivity.this, EmployeeProfileActivity.class);
                intent.putExtra("username", list.get(i).getUsername());
                intent.putExtra("email", list.get(i).getEmail());
                startActivity(intent);
            }
        });

    }

    /**
     * Method to handle the button click and navigate back to EmployerActivity
     */
    private void returnToEmployerActivity() {
        Intent employerActivityIntent = new Intent(RecommendedEmployeesActivity.this, BossActivity.class);
        startActivity(employerActivityIntent);
    }
}
