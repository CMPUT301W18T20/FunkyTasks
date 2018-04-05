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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private EditText title;
    private EditText description;
    private ArrayList<Task> tasks;
    private Task taskTemp;
    ImageConverterController imageConvert;
    Uri photoURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        setTitle("Create a Task");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Intent intent = getIntent();
        imageConvert = new ImageConverterController();

        username = intent.getExtras().getString("username");
        username = LoginActivity.username;

        // defining our edit text views
        title = (EditText) findViewById(R.id.AddTitle);
        description = (EditText) findViewById(R.id.AddDescription);
        newImages = new ArrayList<String>();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() { // if user clicks on button, check if task input is validated
            @Override
            public void onClick(View view) {
                boolean input = checkInput();
                if (!input){
                    return;
                }

                task = new Task(titleValue, descriptionValue, username);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_task, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.camera:
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

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

        }
    }


    //https://stackoverflow.com/questions/30343011/how-to-check-if-an-android-device-is-online

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