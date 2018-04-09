/**
 * DisplayMap
 *
 * Version 1.0.0
 *
 * Created by Funky Tasks on 2018-04-06.
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



/**
 * This activity loads the map and its functionality
 */

public class DisplayMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    MapView mapView;
    Task task;
    String passedName;
    String taskID;
    LatLng taskPoint = null;


    /**
     * This onCreate function loads the view and prepares all the buttons for clicking. It checks
     * which view called the activity so it can properly behave. Sometimes the map is interactive,
     * sometimes it isn't based on which activity called this one. This function also locates
     * and initializes my map.
     *
     * @param savedInstanceState a bundle holding the state of the screen when it was last loaded
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);

        Button saveBtn = this.findViewById(R.id.saveLocation);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng point;
                Intent intent = new Intent();
                Bundle b = new Bundle();
                try {
                    point = taskPoint;
                } catch (Exception e) {
                    point = new LatLng(53.68, -113.52);
                }
                b.putParcelable("location", point);
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();

            }
        });



        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        assert mapFragment != null;
        mapFragment.getMapAsync(this);


        Intent passedIntent = getIntent();
        Bundle passedItems = passedIntent.getExtras();
        taskID = passedItems.getString("task");
        passedName = passedItems.getString("name");
//        Log.e("Task ID", taskID.toString());

        if (passedName.equals("Request") || passedName.equals("Requestor Task")
                || passedName.equals("Provider")) {
            saveBtn.setVisibility(View.GONE);
        }


        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();
        getTask.execute(taskID);
        try {
            task = getTask.get();
            Log.e("Task location", task.getLocation().toString());
        } catch (Exception e) {
            Log.e("Unable to load", "task");
        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * @param googleMap a googleMap object that represents the map fragment
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.e("Passed name", passedName);
        final String createStr = "Create";
        final String editStr = "Edit";

        LatLng location = new LatLng(53.68, -113.52);  // default load to Edmonton except it's actually Calgary
        if (taskID != null) {
            Log.e("TaskID", taskID);
            try {
                location = task.getLocation();
                Log.e("Location", location.toString());
                mMap.addMarker(new MarkerOptions()
                        .position(location));
            } catch (Exception e) {
                location = new LatLng(53.68, -113.52);  // default load to Edmonton except it's actually Calgary
            }
        }

        if (taskID == null) {
            if (!passedName.equals("Create")) {
                Toast.makeText(DisplayMap.this,
                        "This task does not have a location", Toast.LENGTH_LONG).show();
            }
        }



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if (passedName.trim().equalsIgnoreCase(createStr) ||
                        passedName.trim().equalsIgnoreCase(editStr)) {
                    Log.e("Add click listener", "is executing");
                    taskPoint = point;
//                    task.setLocation(point);
//                    ElasticSearchController.updateTask update=new ElasticSearchController.updateTask();
//                    update.execute(task);
                    Log.e("Point", point.toString());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .draggable(true));

                } else {
                    Toast.makeText(DisplayMap.this,
                            "You can't edit the location", Toast.LENGTH_LONG).show();
                }
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));
        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);


    }

}
