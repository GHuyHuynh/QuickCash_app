package com.example.quick_cash.JobPost;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.quick_cash.R;
import com.example.quick_cash.MainHomepages.WorkerActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class show the job location with use of Google Maps API
 */
public class JobLocationMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    /**
     * Variables
     */
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private String jobLocation;
    private String jobName;
    private Address jobAddress;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupBackButton();
    }

    /**
     * Set up back button to go to job post page and see more job
     */
    public void setupBackButton() {
        backButton = findViewById(R.id.map_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JobLocationMapsActivity.this, WorkerActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Use the GeoLocate API to find the closet address based on the job locaiton string input
     * @param jobLocation -> job post location
     * @return -> an Address class that have all the location information (Latitude, Country Code, etc.)
     */
    protected Address geoLocate(String jobLocation) {
        Address jobAddressHolder = null;
        Geocoder geocoder = new Geocoder(JobLocationMapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(jobLocation, 1);
        } catch (IOException e) {
            Toast.makeText(this, "Cannot get location", Toast.LENGTH_SHORT).show();
        }

        if (!list.isEmpty()) {
            Address address = list.get(0);
            jobAddressHolder = address;
        }

        return jobAddressHolder;
    }

    /**
     * Double check that job location is valid to avoid google map or app crashing
     * @param jobLocationIn -> job post location
     * @return -> true if location can be found be Geolocate, false if not
     */
    protected boolean twoStepJobLocationCheck(String jobLocationIn) {
        Geocoder geocoder = new Geocoder(JobLocationMapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(jobLocationIn, 1);
        } catch (IOException e) {
            Toast.makeText(this, "Cannot get location", Toast.LENGTH_SHORT).show();
        }

        return !list.isEmpty();
    }


    /**
     * Set up Google Maps and drop the pins of where the job is.
     * @param googleMap
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        Intent data = getIntent();
        this.jobLocation = data.getStringExtra("extractJobLocation");
        if (twoStepJobLocationCheck(jobLocation)) {
            this.jobAddress = geoLocate(jobLocation);
        }
        else {
            Toast.makeText(this, "This job do not have a valid location", Toast.LENGTH_SHORT).show();
        }
        this.jobName = data.getStringExtra("extractJobName");


        double latitude = this.jobAddress.getLatitude();
        double longitude = this.jobAddress.getLongitude();
        LatLng coordinate = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(coordinate).title(this.jobName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));
    }

}

