package com.example.quick_cash.Employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.quick_cash.Adapter.AdapterUserList;
import com.example.quick_cash.MainHomepages.BossActivity;
import com.example.quick_cash.Credentials.LoginActivity;
import com.example.quick_cash.MyLocalArea.MyLocalAreaActivity;
import com.example.quick_cash.R;
import com.example.quick_cash.AppUser.AppUser;
import com.example.quick_cash.MainHomepages.WorkerActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class EmployeeListActivity extends AppCompatActivity {
    String username;
    String email;
    ArrayList<AppUser> userArrayList = new ArrayList<>();
    ListView userListView;
    String userDatabaseURL = "https://group-1-quick-cash-default-rtdb.firebaseio.com/";
    FirebaseDatabase userDatabase;
    DatabaseReference userDatabaseReference;

    String PREVIOUS_ACTIVITY = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        Intent intent = getIntent();
        PREVIOUS_ACTIVITY = intent.getStringExtra("PREVIOUS_ACTIVITY");

        getInAppUsername();

        initializeView();
        getDatabaseData();

    }

    /**
     * Get the in app current user username
     */
    protected void getInAppUsername() {
        SharedPreferences inAppUsername = getSharedPreferences("Pref", MODE_PRIVATE);
        username = inAppUsername.getString("key_username", null);
    }

    public String getUserName(){
        return username;
    }
    /**
     * Initialize view by connecting all java object to layout object
     */
    protected void initializeView() {

        userListView = findViewById(R.id.userListing);
    }

    /**
     * Connect to user credentials database
     * Get all the user and add to userArrayList
     * Show all the user
     */
    protected void getDatabaseData() {
        userDatabase = FirebaseDatabase.getInstance(userDatabaseURL);
        userDatabaseReference = userDatabase.getReference();

        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        AppUser userObject = new Employee(null, null);
                        String username = dataSnapshot.getKey().toString();
                        userObject.setUsername(username);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        userObject.setEmail(email);
                        userArrayList.add(userObject);
                    }
                }
                showData(userArrayList, userListView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Show all user in the userArrayList
     * Initiate clickable for each item -> go to UserDetails when click
     * @param list -> userArrayList: List of all app user
     * @param listView -> listView holder of ArrayList type
     */
    public void showData(ArrayList<AppUser> list, ListView listView){

        if (PREVIOUS_ACTIVITY.equals(WorkerActivity.class.getName().toString())) {
            AdapterUserList adapterUserListView = new AdapterUserList(EmployeeListActivity.this, list, "worker");
            listView.setAdapter(adapterUserListView);
            listView.setClickable(true);
        } else if (PREVIOUS_ACTIVITY.equals(BossActivity.class.getName().toString())) {
            AdapterUserList adapterUserListView = new AdapterUserList(EmployeeListActivity.this, list, "boss");
            listView.setAdapter(adapterUserListView);
            listView.setClickable(true);
        } else {
            AdapterUserList adapterUserListView = new AdapterUserList(EmployeeListActivity.this, list, "worker");
            listView.setAdapter(adapterUserListView);
            listView.setClickable(true);
        }
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
        MenuItem item5 = menu.findItem(R.id.user_list);
        item5.setVisible(false);
        MenuItem item6 = menu.findItem(R.id.requestPayment);
        item6.setVisible(false);

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
                myArea.putExtra("PREVIOUS_ACTIVITY",PREVIOUS_ACTIVITY);
                startActivity(myArea);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}