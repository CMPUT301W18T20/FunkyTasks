package com.example.android.funkytasks;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jpoulin on 2018-04-08.
 */

public class DisplayNearbyTasks extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public LocationManager locationManager;
    LatLng point;

    private ArrayList<Task> taskList = null;

    int REQUEST_CODE;
    String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_nearby);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        networkConnection();

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrag);

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert mapFragment != null;
        mapFragment.getMapAsync(this);



    }


    public void calculateDistance() {
        Log.e("Calculate distance", "is executing");

        // if the task is posted by the user, don't display it


        ElasticSearchController.GetAllTaskList getAllTaskList = new ElasticSearchController.GetAllTaskList();
        getAllTaskList.execute();
        try {
            taskList = getAllTaskList.get();    // get all the tasks in the server
        } catch (Exception e) {
            Log.e("ERROR", "can't get the tasks");
        }

        if (taskList != null) {
            for (Task task : taskList) {
                String taskRequester = task.getRequester();
                if (!taskRequester.equals(username)) {
                    if (task.getStatus().equalsIgnoreCase("Requested")
                            || task.getStatus().equalsIgnoreCase("Bidded")) {
                        Log.d("Iterating", "in the nearby for loop");
                        LatLng taskLoc = task.getLocation();
                        if (taskLoc != null) {
                            double taskLat = taskLoc.latitude;
                            double taskLon = taskLoc.longitude;
                            double myLat = point.latitude;
                            double myLon = point.longitude;

                            double dist = distance(taskLat, myLat, taskLon, myLon, 0, 0);

                            if (dist <= 5000) {
                                mMap.addMarker(new MarkerOptions()
                                        .position(taskLoc)
                                        .title(task.getTitle()));

                            }
                        }
                    }
                }

            }
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            Log.e("ERROR", "no security permissions");
        }
        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);
        calculateDistance();

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String taskTitle = marker.getTitle();
                for (Task task: taskList) {
                    if (task.getTitle().equals(taskTitle)) {
                        String taskId = task.getId();
                        Intent intent = new Intent(DisplayNearbyTasks.this, ViewRequestorTaskActivity.class);
                        intent.putExtra("id", taskId);
                        startActivity(intent);
                    }
                }

                }
            });

    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }


    public void networkConnection() {
        LocationProvider provider = locationManager.getProvider("network");
        double latitude = 0.0;
        double longitude = 0.0;

        // Register the listener with the Location Manager to receive location updates
        try {
            Location place = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            latitude = place.getLatitude();
            longitude = place.getLongitude();
        } catch (SecurityException e) {
            Log.e("ERROR", "no location manager");
        } catch (Exception e) {
            Log.e("ERROR", "unknown location error");
        }

        point = new LatLng(latitude, longitude);

        getCurrentLocation(locationManager);
    }



    public void getCurrentLocation(LocationManager locationManager) {

        Criteria criteria = new Criteria();
//        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, true);
        try {
            Location location = locationManager.getLastKnownLocation(provider);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            point = new LatLng(latitude, longitude);
        } catch (SecurityException e) {
            Log.e("ERROR", "security permissions 254");
        }

    }

}