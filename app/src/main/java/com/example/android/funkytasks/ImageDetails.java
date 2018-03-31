/**
 * Image Details
 *
 * Version 1.0.0
 *
 * Created on March 8th by Funky Tasks
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */
package com.example.android.funkytasks;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Displays the images the task may have
 */
public class ImageDetails extends BaseActivity {

    ViewPager viewPager;
    PicturePagerAdapter picturePagerAdapter;
    private ArrayList<String> images;
    private ArrayList<Bitmap> imageBitmaps;

    private String username; // username of the person who logged in
    private Task task;
    private String id;
    private String code;

    private ImageConverterController imageConverter;


    /**
     * Overrides the default onCreate class and starts the activity with the proper view
     *
     * @param savedInstanceState a bundle storing the last state the app was in before it
     *                           was last closed
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_details);
        setTitle("Funky Tasks");

        Intent intent = getIntent();
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        imageConverter = new ImageConverterController();

        username = LoginActivity.username;

        id = intent.getExtras().getString("id");

        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();
        getTask.execute(id);
        try {
            task = getTask.get();
            Log.e("Return task title", task.getTitle());
        } catch (Exception e) {
            Log.e("Error", "Task get not working");
        }

        imageBitmaps = imageConverter.stringToImageList(task.getImages());


        picturePagerAdapter = new PicturePagerAdapter(ImageDetails.this, imageBitmaps);
        viewPager.setAdapter(picturePagerAdapter);
    }


}
