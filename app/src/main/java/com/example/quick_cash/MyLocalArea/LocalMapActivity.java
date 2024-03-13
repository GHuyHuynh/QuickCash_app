package com.example.quick_cash.MyLocalArea;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quick_cash.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This activity can be build on with local job pin points
 * On create, this activity get the user latitude and longitude and pin the location on Google Map
 */
public class LocalMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private double userLatitude;
    private double userLongitude;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.local_map_view);
        mapFragment.getMapAsync(this);

        setupBackButton();
    }

    /**
     * Extract user latitude and longitude my local activty
     * Pin down the user location with a pin
     * @param googleMap
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        //Extract user location latitude and longitude from My Local Area activity
        Intent data = getIntent();
        userLatitude = data.getDoubleExtra("extractLatitude", 0.0);
        userLongitude = data.getDoubleExtra("extractLongitude", 0.0);

        LatLng coordinate = new LatLng(userLatitude, userLongitude);

        mMap.addMarker(new MarkerOptions().position(coordinate).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 12));
    }

    /**
     * Back button to local area from the local map activity
     */
    public void setupBackButton() {
        backButton = findViewById(R.id.local_map_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LocalMapActivity.this, MyLocalAreaActivity.class);
                startActivity(intent);
            }
        });

    }
}