/**
 * DisplayNearbyTasks
 *
 * Version 1.0.0
 *
 * Created by Funky Tasks on 2018-04-08.
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;


import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * This class displays all tasks within 5 kilometers of the user's current position
 */

public class DisplayNearbyTasks extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public LocationManager locationManager;
    LatLng point;

    private ArrayList<Task> taskList = null;

    int REQUEST_CODE;
    String username;


    /**
     * Sets up the activity for use, finds the map fragment, sets up the location manager, and calls
     * networkConnection to establish the network connection for the map.
     *
     * @param savedInstanceState a bundle representing the state of the app when it was last
     *                           on this screen
     */

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


    /**
     * This function calls distance() to calculate the distance between the user and all the placed
     * tasks. A task will not appear on the map if: the current user posted the task, the task
     * has been assigned or completed, the task does not have a location.
     */
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


        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, 12);
        mMap.animateCamera(cameraUpdate);

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

    /**
     * This function calculates the distance between two given map points and returns it as a double.
     * Taken from: https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude-what-am-i-doi
     *
     * @param lat1 the first latitude for the calculation
     * @param lat2 the second latitude for the calculation
     * @param lon1 the first longitude for the calculation
     * @param lon2 the second longitude for the calculation
     * @param el1 the first elevation (if applicable)
     * @param el2 the second elevation (if applicable)
     * @return a double representing the distance between the points
     */
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


    /**
     * This function tests the network connection and finds the current location based on
     * that connection.
     */
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

//        point = new LatLng(latitude, longitude);

        getCurrentLocation(locationManager);
    }


    /**
     * Gets the current location of the user/phone/emulator and sets the class variable to contain
     * that location
     *
     * @param locationManager a Location Manager object that helps manage the apps location
     */

    public void getCurrentLocation(LocationManager locationManager) {

        Criteria criteria = new Criteria();
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