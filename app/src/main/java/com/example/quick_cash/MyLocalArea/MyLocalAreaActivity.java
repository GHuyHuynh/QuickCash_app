package com.example.quick_cash.MyLocalArea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quick_cash.MainHomepages.BossActivity;
import com.example.quick_cash.Credentials.LoginActivity;
import com.example.quick_cash.Employee.EmployeeListActivity;
import com.example.quick_cash.R;
import com.example.quick_cash.MainHomepages.WorkerActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This activity is the home page for my local area page
 * This activity ask user to input location or use device location detection
 * On both click, user will be transfer to a map activity with the pin as their location
 */
public class MyLocalAreaActivity extends AppCompatActivity {
    /**
     * Variables
     */
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient client;
    EditText typedLocation;
    Button enableLocationButton, typedLocationButton;
    Address userAddress;
    private static final String CANNOT_GET_LOCATION = "Cannot Get Location!";
    String username;
    private FirebaseDatabase userDataBase;
    private DatabaseReference userDatabaseReference;
    private TextView backToWorkerActivity;
    private String userDatabaseURL = "https://group-1-quick-cash-default-rtdb.firebaseio.com/";

    String PREVIOUS_ACTIVITY = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_local_area);

        getSupportActionBar().hide();

        enableLocationService();
        setupTypedLocationButton();

        Intent intent = getIntent();
        PREVIOUS_ACTIVITY = intent.getStringExtra("PREVIOUS_ACTIVITY");

        SharedPreferences inAppUsername = getSharedPreferences("Pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = inAppUsername.edit();
        username = inAppUsername.getString("key_username", null);
        userDataBase = FirebaseDatabase.getInstance(userDatabaseURL);
        userDatabaseReference = userDataBase.getReference().child(username);
        setUpBackButton();
        getSupportActionBar().hide();
    }

    /**
     * Back button from local area to worker activity
     */
    private void setUpBackButton() {
        backToWorkerActivity = findViewById(R.id.backToUserList);
        backToWorkerActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToUser = new Intent(MyLocalAreaActivity.this, WorkerActivity.class);
                backToUser.putExtra("username", username);
                startActivity(backToUser);
            }
        });
    }

    /**
     * Get the user location input
     * @return -> user location input String
     */
    public String getTypedLocation() {
        typedLocation = findViewById(R.id.local_location);
        return typedLocation.getText().toString().trim();
    }

    /**
     * Check if input have special char
     * @param location -> user location input
     * @return true if there is no special char, false if there is special char
     */
    public boolean noSpecialChar(String location) {
        if(location.isEmpty()) {
            return false;
        }

        for (int i=0; i<location.length(); i++) {
            char temp = location.charAt(i);
            if (!Character.isDigit(temp) && !Character.isLetter(temp) && !Character.isWhitespace(temp)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Check and see if a string location is valid or not
     * @param jobLocationIn -> location input string of the user
     * @return -> true if location is valid, false otherwise
     */
    public boolean isValidLocation(String jobLocationIn) {
        if (!noSpecialChar(jobLocationIn)) {
            return false;
        }
        Geocoder geocoder = new Geocoder(MyLocalAreaActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(jobLocationIn, 1);
        } catch (IOException e) {
            Toast.makeText(this, CANNOT_GET_LOCATION, Toast.LENGTH_SHORT).show();
        }

        if (!list.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Set up button to retrieve user location input
     * Switch to local map activity if input is valid
     * Display an error message if the location input is not detail enough
     */
    public void setupTypedLocationButton() {
        typedLocationButton = findViewById(R.id.submit_location_button);
        typedLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userLocation = getTypedLocation();
                if (isValidLocation(userLocation)) {
                    userAddress = geoLocate(userLocation, getApplicationContext());
                    Intent localMapUserIn = new Intent(MyLocalAreaActivity.this, LocalMapActivity.class);
                    double typedLatitude = userAddress.getLatitude();
                    double typedLongitude = userAddress.getLongitude();

                    // Check if the "Location" child exists, if not, create it
                    DatabaseReference locationReference = userDatabaseReference.child("Location");
                    locationReference.setValue(true);

                    // Update latitude and longitude
                    userDatabaseReference.child("Latitude").setValue(userAddress.getLatitude());
                    userDatabaseReference.child("Longitude").setValue(userAddress.getLongitude());


                    localMapUserIn.putExtra("extractLatitude", typedLatitude);
                    localMapUserIn.putExtra("extractLongitude", typedLongitude);
                    startActivity(localMapUserIn);
                }
                else {
                    Toast.makeText(MyLocalAreaActivity.this, "Please enter a more detail location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Use the GeoLocate API to find the closet address based on the job location string input
     * @param userTypedLocation -> job post location
     * @return -> an Address class that have all the location information (Latitude, Country Code, etc.)
     */
    public static Address geoLocate(String userTypedLocation, Context context) {
        Address jobAddressHolder = null;
        Geocoder geocoder = new Geocoder(context);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(userTypedLocation, 1);
        } catch (IOException e) {
            Toast.makeText(context, CANNOT_GET_LOCATION, Toast.LENGTH_SHORT).show();
        }

        if (!list.isEmpty()) {
            Address address = list.get(0);
            jobAddressHolder = address;
        }

        return jobAddressHolder;
    }

    /**
     * Check for device location permission
     * @param requestCode The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "You must allow precise location!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Get user current location
     * If user permission is true, switch to my local map activity
     */
    public void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION
            );
        } else {
            client = LocationServices.getFusedLocationProviderClient(MyLocalAreaActivity.this);
            client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        userAddress = convertLocation2Address(location);

                        // Check if the "Location" child exists, if not, create it
                        DatabaseReference locationReference = userDatabaseReference.child("Location");
                        locationReference.setValue(true);

                        // Update latitude and longitude
                        userDatabaseReference.child("Latitude").setValue(userAddress.getLatitude());
                        userDatabaseReference.child("Longitude").setValue(userAddress.getLongitude());


                        startLocalMap(location);
                    }
                }
            });
        }
    }

    /**
     * Switch to local map activity using Location object
     * Put extract the converted Location to Address
     * @param location
     */
    public void startLocalMap(Location location) {
        Intent intent = new Intent(this, LocalMapActivity.class);
        userAddress = convertLocation2Address(location);
        double typedLatitude = userAddress.getLatitude();
        double typedLongitude = userAddress.getLongitude();
        intent.putExtra("extractLatitude", typedLatitude);
        intent.putExtra("extractLongitude", typedLongitude);
        startActivity(intent);
    }

    /**
     * Convert a Location object to Address object
     * @param location -> location object to be converted
     * @return -> Address object that correspond to the Location object
     */
    public Address convertLocation2Address(Location location) {
        Address returnAddress = null;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Geocoder geocoder = new Geocoder(MyLocalAreaActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            Toast.makeText(this, CANNOT_GET_LOCATION, Toast.LENGTH_SHORT).show();
        }

        if (!list.isEmpty()) {
            Address address = list.get(0);
            returnAddress = address;
        }

        return returnAddress;
    }

    /**
     * Check for permission and allow location service
     * After that proceed to my local map activity
     */
    public void enableLocationService() {
        enableLocationButton = findViewById(R.id.enable_location_service_button);
        enableLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_bar, menu);
        //disable AddJob button
        MenuItem item = menu.findItem(R.id.addJobPosting);
        item.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.message);
        item2.setVisible(false);
        MenuItem item3 = menu.findItem(R.id.addJobPreferences);
        item3.setVisible(false);
        MenuItem item4 = menu.findItem(R.id.search_bar);
        item4.setVisible(false);
        MenuItem item5 = menu.findItem(R.id.my_local_activity);
        item5.setVisible(false);
        MenuItem item6 = menu.findItem(R.id.requestPayment);
        item6.setVisible(false);

        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            /**
             Switch to boss activity on click and carry username over
             */
            case R.id.Boss:
                Intent boss = new Intent(MyLocalAreaActivity.this, BossActivity.class);
                startActivity(boss);
                return true;

            /**
             Switch to log out activity and logged out the user
             */
            case R.id.Logout:
                Intent logout = new Intent(MyLocalAreaActivity.this, LoginActivity.class);
                SharedPreferences preferences = getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.remove("username");
                editor.apply();

                startActivity(logout);
                return true;

            case R.id.Worker:
                Intent worker = new Intent(MyLocalAreaActivity.this, WorkerActivity.class);
                startActivity(worker);
                return true;

            case R.id.user_list:
                Intent userList = new Intent(this, EmployeeListActivity.class);
                userList.putExtra("PREVIOUS_ACTIVITY",PREVIOUS_ACTIVITY);
                startActivity(userList);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}