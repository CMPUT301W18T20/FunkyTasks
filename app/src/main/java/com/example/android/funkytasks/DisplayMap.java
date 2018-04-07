package com.example.android.funkytasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by jpoulin on 2018-04-06.
 */

public class DisplayMap extends AppCompatActivity {

    private GoogleMap mMap;
    MapView mapView;
    Task task;
    String passedName;
    String taskName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        loadMap(savedInstanceState);
        Intent passedIntent = getIntent();
        Bundle passedItems = passedIntent.getExtras();
        taskName = passedItems.getString("task");
        passedName = passedItems.getString("name");

        if (taskName != null) {

            ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();
            getTask.execute(taskName);
            try {
                task = getTask.get();
            } catch (Exception e) {
                return;
            }
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        setContentView(R.layout.activity_maps);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * @param savedInstanceState a bundle holding the most recent state of this page of the app
     */
    public void loadMap(Bundle savedInstanceState) {
        mapView = this.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                LatLng location;
                if (taskName != null) {
                    try {
                        location = task.getLocation();
                        mMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(task.getTitle()));
                    } catch (Exception e) {
                        location = new LatLng(53.68, -113.52);  // default load to Edmonton except it's actually Calgary
                    }
                } else {
                    location = new LatLng(53.68, -113.52);  // default load to Edmonton except it's actually Calgary
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(8.0f));
                UiSettings mapUiSettings = mMap.getUiSettings();
                mapUiSettings.setZoomControlsEnabled(true);

            }
        });

        if (passedName.equals("Create")) {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {
                    task.setLocation(point);
                    ElasticSearchController.updateTask update=new ElasticSearchController.updateTask();
                    update.execute(task);
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .draggable(true));

                }
            });
        }
    }



    //https://stackoverflow.com/questions/16564550/in-version-2-map-view-does-not-show-map


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }




}
