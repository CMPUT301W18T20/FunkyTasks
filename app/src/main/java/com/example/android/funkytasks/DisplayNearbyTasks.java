package com.example.android.funkytasks;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jpoulin on 2018-04-08.
 */

public class DisplayNearbyTasks extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Timer t = new Timer();
    LatLng point;

    int REQUEST_CODE;
    String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_nearby);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

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

        ArrayList<Task> taskList = null;
        ElasticSearchController.GetAllTaskList getAllTaskList = new ElasticSearchController.GetAllTaskList();
        getAllTaskList.execute();
        try {
            taskList = getAllTaskList.get();    // get all the tasks in the server
        } catch (Exception e) {
            Log.e("ERROR", "can't get the tasks");
        }

        if (taskList != null) {
            for (Task eachTask : taskList) {
                if (!eachTask.getProvider().equals(username));
                Log.d("Iterating", "in the nearby for loop");
                LatLng taskLoc = eachTask.getLocation();
                if (taskLoc != null) {
                    mMap.addMarker(new MarkerOptions()
                            .position(taskLoc)
                            .title(eachTask.getTitle()));
                    double taskLat = taskLoc.latitude;
                    double taskLon = taskLoc.longitude;
                    double myLat = point.latitude;
                    double myLon = point.longitude;

                    double dist = distance(taskLat, myLat, taskLon, myLon, 0,0);

                    if (dist < 5000) {
                        mMap.addMarker(new MarkerOptions()
                                .position(taskLoc));

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

        Location location;

//        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException e) {
            Log.e("ERROR", "no network access");
        }


        Criteria criteria = new Criteria();

        LocationProvider provider = locationManager.getProvider("network");
        double latitude = 0.0;
        double longitude = 0.0;

        try {
            Location place = locationManager.getLastKnownLocation("network");
            latitude = place.getLatitude();
            longitude = place.getLongitude();
        } catch (SecurityException e) {
            Log.e("ERROR", "no location manager");
        } catch (Exception e) {
            Log.e("ERROR", "unknown location error");
        }

        calculateDistance();

        point = new LatLng(latitude, longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));
        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);

//
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//            @Override
//            public void onMapClick(LatLng point) {
//                // TODO Auto-generated method stub
//
//                mMap.clear();
//
//
//            }
//        });

        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.scheduleAtFixedRate(new Runnable() {
            public void run() {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
            }
        }, 0, 1, TimeUnit.SECONDS); // execute every 60 seconds


    }

    public void makeUseOfNewLocation(Location location) {
        double pointLat = location.getLatitude();
        double pointLon = location.getLongitude();
        LatLng place = new LatLng(pointLat, pointLon);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));
        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);
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



}

