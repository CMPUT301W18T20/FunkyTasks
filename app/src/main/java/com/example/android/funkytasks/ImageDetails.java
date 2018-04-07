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
import android.graphics.Matrix;
import android.os.Build;
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
        if (isEmulator()){
            picturePagerAdapter = new PicturePagerAdapter(ImageDetails.this, imageBitmaps);
        }
        else{
            ArrayList<Bitmap> rotatedBitmaps = new ArrayList<Bitmap>();
            for (Bitmap image:imageBitmaps) {
                Bitmap changed = rotateBitmap(image, 90);
                rotatedBitmaps.add(changed);
            }
            picturePagerAdapter = new PicturePagerAdapter(ImageDetails.this, rotatedBitmaps);
        }

        viewPager.setAdapter(picturePagerAdapter);
    }

    /**
     * Rotate the bitmap by specified angle degree
     * @param source the bitmap we want to rotate
     * @param angle the degrees for which we want to rotate the bitmap by
     * @return a Bitmap that has been rotated
     */

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * Checks if app running is on emulator or not
     * @return a boolean checking if we are running an emulator or not
     */
    // https://stackoverflow.com/a/21505193

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }


}
