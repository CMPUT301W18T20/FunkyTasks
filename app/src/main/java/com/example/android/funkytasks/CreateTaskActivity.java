/**
 * CreateTaskActivity
 *
 * Version 1.0.0
 *
 * Created on March 8th by Funky Tasks
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * This activity allows a task requester to create a new task and post it to the server.
 */
public class CreateTaskActivity extends AppCompatActivity {


    private String titleValue; // value of the task title
    private String descriptionValue; // value of the task description
    private String username; // username of user who logged in
    private ArrayList<String> newImages;
    private Task task;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int MAP_RESULT = 2;
    private EditText title;
    private EditText description;
    private ArrayList<Task> tasks;
    private Task taskTemp;
    private GoogleMap mMap;
    ImageConverterController imageConvert;
    Uri photoURI;
    MapView mapView;
    public LatLng taskLoc = null;
    final GlobalVariables globals = new GlobalVariables();


    /**
     * Overrides the onCreate super class and instantiates the proper view for this class
     *
     * @param savedInstanceState a bundle of the previous saved instance state that is used to
     *                           load a snapshot of the app in the state it was last in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);


        setTitle("Create a Task");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Intent intent = getIntent();
        imageConvert = new ImageConverterController();

        username = intent.getExtras().getString("username");
        username = LoginActivity.username;

        // defining our edit text views
        title = findViewById(R.id.AddTitle);
        description = findViewById(R.id.AddDescription);
        newImages = new ArrayList<>();

        Button loadMap = this.findViewById(R.id.addLocation);

        loadMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = new Task(titleValue, descriptionValue, username);
                Intent showMap = new Intent(CreateTaskActivity.this, DisplayMap.class);
                String taskID = task.getId();
                String activityName = "Create";
                showMap.putExtra("task", taskID);
                showMap.putExtra("name", activityName);
                startActivityForResult(showMap, MAP_RESULT);
            }
        });



        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() { // if user clicks on button, check if task input is validated
            @Override
            public void onClick(View view) {
                boolean input = checkInput();
                if (!input) {
                    return;
                }

                task = new Task(titleValue, descriptionValue, username);
                task.setLocation(taskLoc);


                if (newImages.size() != 0) {
                    boolean check = imageConvert.checkImages(newImages);
                    if (!check) {
                        Toast.makeText(getApplicationContext(), "Image size is too large", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    task.setImagesList(newImages);
                }

                new Thread(new Runnable() {
                    public void run() {
                        if (isNetworkAvailable()){
                            Log.d("Network", "available");
                            // a potentially  time consuming task
                            ElasticSearchController.PostTask postTask = new ElasticSearchController.PostTask();
                            postTask.execute(task);
                            LocalRequestedTaskController requestedController = new LocalRequestedTaskController(getApplicationContext(), username);
                            requestedController.addOfflineTask(task);
                        }
                        else{
                            Log.d("Network", "unavailable");
                            String offlineID = UUID.randomUUID().toString();
                            task.setOfflineId(offlineID);
                            OfflineController controller = new OfflineController(getApplicationContext(), username);
                            controller.saveInFile(task);
                            LocalRequestedTaskController requestedController = new LocalRequestedTaskController(getApplicationContext(), username);
                            requestedController.addOfflineTask(task);

                        }

                    }
                }).start();

                intent.putExtra("username", username);
                //intent.putExtra("task", task); // send task our to main activity to post to server
                setResult(RESULT_OK, intent);
                finish();

            }
        });


    }


    /**
     * Checks if the title and description inputted is under the size constraint
     * @return a boolean if the user input for title and description is under the size length
     */

    public boolean checkInput(){
        titleValue = title.getText().toString();            // grab title from edit text input
        if (titleValue.length() >= 30 || titleValue.length() <= 0) {  // validating name input length
            Toast.makeText(getApplicationContext(), "Title is invalid length. Must be between 1-29 characters. ", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        descriptionValue = description.getText().toString(); // grab description from edit text input
        if (descriptionValue.length() >= 300 || descriptionValue.length() <= 0) {               // validating name input length
            Toast.makeText(getApplicationContext(), "Description is invalid length. Must be between 1-299 characters. ", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;
    }


    /**
     * Create option menu in the toolbar
     * @param menu grabs the menu object the layout is placed in
     * @return an options menu containing the toolbar
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_task, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Checks which menu icon has been clicked on
     * @param item the toolbar where the button icons are placed in
     * @return a boolean if the icon is placed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.camera:
                if (newImages.size() >= 10){
                    Toast.makeText(this, "Too many photos for task.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        //photoFile = createTemporaryFile("picture", ".jpg");
                    } catch (Exception e) {
                        // Error occurred while creating the File
                        Log.e("ugh","ugh");
                        return false;
                    }
                    // Continue only if the File was successfully created
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    photoURI = FileProvider.getUriForFile(this,
                            "com.example.android.funkytasks",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    String mCurrentPhotoPath;

    /**
     * Create file for the image
     * @return a file for the image
     * @throws IOException if we can't create the file
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpeg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Call back from the activity we have called previously
     * @param requestCode which code we passed to the activity we called
     * @param resultCode the code we get from the called activity
     * @param data the intent
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            this.getContentResolver().notifyChange(photoURI, null);
            ContentResolver cr = this.getContentResolver();

            Bitmap bitmap;
            try {
                bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, photoURI);
            }
            catch (Exception e) {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                return;
            }

            newImages.add(imageConvert.convertToString(bitmap));
            Log.e("bitmap string",imageConvert.convertToString(bitmap));

        }

        if (requestCode == MAP_RESULT && resultCode == RESULT_OK) {
            LatLng point = data.getParcelableExtra("location");
            taskLoc = point;

        }
    }


    /**
     * This fucntion checks for connectivity, returns true if the device is connected to the internet, false if the device is not.
     * @return a boolean indicating connectivity to the internet
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }



}