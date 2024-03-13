package com.example.quick_cash.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quick_cash.AppUser.AppUser;
import com.example.quick_cash.Employee.EmployeeListActivity;
import com.example.quick_cash.Employee.EmployeeProfileActivity;
import com.example.quick_cash.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Adpater for user list in user list activity
 */
public class AdapterUserList extends ArrayAdapter<AppUser> {

    private String  PREVIOUS_ACTIVITY;
    private  Context context;

    String userDatabaseURL = "https://group-1-quick-cash-default-rtdb.firebaseio.com/";

    FirebaseDatabase userDatabase;
    DatabaseReference userDatabaseReference;
    public AdapterUserList(@NonNull Context context, ArrayList<AppUser> userArrayList,String PREVIOUS_ACTIVITY) {
        super(context, R.layout.user_list, userArrayList);
        this.PREVIOUS_ACTIVITY = PREVIOUS_ACTIVITY;
        this.context = context;
        userDatabase = FirebaseDatabase.getInstance(userDatabaseURL);

    }

    /**
     * Fill all user list view user data from user database
     * @param position -> current list view item
     * @param view -> view of the user list activity
     * @param parent -> list view layout parent
     * @return -> listview with all user and it user data
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        AppUser appUser = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.user_list, parent, false);
        }
        TextView listUsername = view.findViewById(R.id.user_list_username);
        TextView listEmail = view.findViewById(R.id.user_list_email);
        Button preferd = view.findViewById(R.id.preferd);
        Button cancelPrefer = view.findViewById(R.id.cancel_preferd);
        listUsername.setText(appUser.getUsername());
        listEmail.setText(appUser.getEmail());


        /**
         *add
         *Firstly, determine whether the previous context is a boss or a worker. If it is a worker, add a prefer job.
         * If it is not, add a prefer worker:
         *Firstly, you cannot add yourself, and secondly, the added ones cannot be repeated
         */
        preferd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                EmployeeListActivity employeeListActivity = (EmployeeListActivity) context;
                final String userName = employeeListActivity.getUserName();
                if (PREVIOUS_ACTIVITY.equals("boss")){
                    if (!userName.equals(appUser.getUsername().toString().trim())){
                        userDatabaseReference = userDatabase.getReference();
                        DatabaseReference personReference = userDatabaseReference.child(userName).child("PreferredEmployees");
                        final AtomicBoolean flag = new AtomicBoolean(true);
                        personReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()&&snapshot.getChildrenCount()>0){
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                        String username = dataSnapshot.getValue().toString();
                                        if(username.equals(appUser.getUsername().toString().trim())){
                                            flag.set(false);
                                            Toast.makeText(employeeListActivity, "The employee has added", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                }
                                if (flag.get()){
                                    DatabaseReference newResumeRef = personReference.push();
                                    newResumeRef.setValue(appUser.getUsername());
                                    Toast.makeText(employeeListActivity, "The employee add success!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }else {
                        Toast.makeText(employeeListActivity, "You can't choose yourself！", Toast.LENGTH_SHORT).show();
                    }
                }
                /**
                 * Set user preferences
                 */
                else {

                    if (!userName.equals(appUser.getUsername().toString().trim())){
                        userDatabaseReference = userDatabase.getReference();
                        DatabaseReference personReference = userDatabaseReference.child(userName).child("PreferredJobs");
                        final AtomicBoolean flag = new AtomicBoolean(true);
                        personReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()&&snapshot.getChildrenCount()>0){
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                        String username = dataSnapshot.getValue().toString();
                                        if(username.equals(appUser.getUsername().toString().trim())){
                                            flag.set(false);
                                            Toast.makeText(employeeListActivity, "Added successfully!", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                }
                                if (flag.get()){
                                    DatabaseReference newResumeRef = personReference.push();
                                    newResumeRef.setValue(appUser.getUsername());
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }else {
                        Toast.makeText(employeeListActivity, "You can't choose yourself！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /**
         * cancel preferences on user
         * */
        cancelPrefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                EmployeeListActivity employeeListActivity = (EmployeeListActivity) context;
                final String userName = employeeListActivity.getUserName();
                if (PREVIOUS_ACTIVITY.equals("boss")){
                    if (!userName.equals(appUser.getUsername().toString().trim())){
                        userDatabaseReference = userDatabase.getReference();
                        DatabaseReference personReference = userDatabaseReference.child(userName).child("PreferredEmployees");
                        Query query = personReference.orderByValue().equalTo(appUser.getUsername());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getChildrenCount()>0 && snapshot.exists()){
                                    for(DataSnapshot childSnapshot : snapshot.getChildren()){

                                        String nodeValue = childSnapshot.getValue(String.class);
                                        if (nodeValue.equals(appUser.getUsername())){

                                            childSnapshot.getRef().removeValue()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(employeeListActivity, "Canceled Successfully!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(employeeListActivity, "cancel Failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else {
                        Toast.makeText(employeeListActivity, "You can't choose yourself！", Toast.LENGTH_SHORT).show();
                    }
                }


                else {

                    if (!userName.equals(appUser.getUsername().toString().trim())){
                        userDatabaseReference = userDatabase.getReference();
                        DatabaseReference personReference = userDatabaseReference.child(userName).child("PreferredJobs");
                        Query query = personReference.orderByValue().equalTo(appUser.getUsername());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getChildrenCount()>0 && snapshot.exists()){
                                    for(DataSnapshot childSnapshot : snapshot.getChildren()){

                                        String nodeValue = childSnapshot.getValue(String.class);
                                        if (nodeValue.equals(appUser.getUsername())){
                                            //get the result
                                            childSnapshot.getRef().removeValue()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(employeeListActivity, "Canceled Successfully!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(employeeListActivity, "cancel Failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else {
                        Toast.makeText(employeeListActivity, "You can't choose yourself！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /**
         * Go to user profile activity on click
         */
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EmployeeProfileActivity.class);
                intent.putExtra("username", appUser.getUsername());
                intent.putExtra("email", appUser.getEmail());
                context.startActivity(intent);
            }
        });



        return view;
    }
}
