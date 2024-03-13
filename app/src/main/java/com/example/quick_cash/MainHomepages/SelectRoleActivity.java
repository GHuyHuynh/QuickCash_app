package com.example.quick_cash.MainHomepages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_cash.R;

/**
 * This class allows logged in user to choose between an employer or employee
 */
public class SelectRoleActivity extends AppCompatActivity {
    //Used to parse the username from prev activity
    static final String EXTRACTUSERNAME = "extractUsername";
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);
        Intent data = getIntent();
        username = data.getStringExtra(EXTRACTUSERNAME);
        getSupportActionBar().hide();

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember", "");
        if(checkbox.equals("true")){
            username = preferences.getString("userName", "");
        }
    }

    /**
     * Functions below will switch for either Employer/Employee depending on what the user chooses
     * @param view-> required to switch to the next activity
     */
    public void roleWorker(View view){
       // Goto the worker page
        finish();
        Intent worker = new Intent(SelectRoleActivity.this, WorkerActivity.class);
        worker.putExtra(EXTRACTUSERNAME, username); //this is used for identifying the user

        startActivity(worker);
    }

    public void roleBoss(View view){
        //Goto the boss page
        finish();
        Intent boss = new Intent(SelectRoleActivity.this, BossActivity.class);
        boss.putExtra(EXTRACTUSERNAME, username);
        startActivity(boss);

    }
}


