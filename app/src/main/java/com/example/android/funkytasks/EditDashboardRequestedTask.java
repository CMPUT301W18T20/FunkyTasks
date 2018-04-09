/**
 * EditDashboardRequestedTask
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This activity allows a user to edit a task
 */
public class EditDashboardRequestedTask extends BaseActivity {
    private String username;
    private String id;
    private EditText editTitle;
    private EditText editDescription;
    private Button saveBT;
    private Task task;
    private int index;
    private String titleValue;
    private String descriptionValue;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int EDIT_LOCATION = 2;
    private ArrayList<String> newImages;
    private ArrayList<Task> tasks;

    private ImageConverterController imageConvert;
    private Uri photoURI;
    private LatLng taskLoc = null;


    /**
     * Overrides the onCreate function, loads the proper view, and sets up the app for interaction
     *
     * @param savedInstanceState a bundle that stores the state of the app the last time
     *                           it was open
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dashboard_requested_task);
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        saveBT = findViewById(R.id.buttonDone);

        final Intent intent = getIntent();
        imageConvert = new ImageConverterController();

        newImages = new ArrayList<>();

        index = intent.getExtras().getInt("index");
        Log.e("index", String.valueOf(index));
        id = intent.getExtras().getString("id");
        //task = (Task) intent.getSerializableExtra("task");
        username = intent.getExtras().getString("username");


        if (isNetworkAvailable()) {
            ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();
            getTask.execute(id);
            try{
                task = getTask.get();
                Log.e("Task title",task.getTitle());
            } catch (Exception e) {
                Log.e("ERROR","not working get task");
            }

        } else {
            //task = (Task) intent.getSerializableExtra("task");
            LocalRequestedTaskController localController = new LocalRequestedTaskController(getApplicationContext(),username);
            tasks = localController.loadRequestedTask();
            for (Task eachTask: tasks) {
                if (eachTask.getId().equals(id)){
                    task = eachTask;
                }
            }
        }

        editTitle.setText(task.getTitle());
        editDescription.setText(task.getDescription());

        Button loadMap = this.findViewById(R.id.editLocation);
        loadMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                task = new Task(titleValue, descriptionValue, username);


                Intent showMap = new Intent(EditDashboardRequestedTask.this, DisplayMap.class);
                String taskID = task.getId();
                String activityName = "Edit";
                showMap.putExtra("task", id);
                showMap.putExtra("name", activityName);
                startActivityForResult(showMap,EDIT_LOCATION);
            }
        });



        saveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean input = checkInput();
                if (!input){
                    return;
                }

                task.setDescription(descriptionValue);
                task.setTitle(titleValue);
                Bundle b = new Bundle();
                LatLng point;
                if (taskLoc == null) {
                    taskLoc = task.getLocation();
                }
                b.putParcelable("location", taskLoc);
                intent.putExtras(b);
                task.setLocation(taskLoc);

                if (newImages.size() > 0) {
                    boolean check = imageConvert.checkImages(newImages);
                    if (!check) {
                        Toast.makeText(getApplicationContext(), "Image size is too large", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    ArrayList<String> combined = task.getImages();
                    combined.addAll(newImages);
                    if (combined.size() >= 10){
                        Toast.makeText(getApplicationContext(), "Too many photos for task.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    task.setImagesList(combined);
                    Log.e("combined",String.valueOf(combined.size()));

                }



                new Thread(new Runnable() {
                    public void run() {
                        if (isNetworkAvailable()){
                            Log.d("Network", "available");
                            // a potentially  time consuming task
                            ElasticSearchController.updateTask updateTask = new ElasticSearchController.updateTask();
                            updateTask.execute(task);
                        }
                        else{
                            Log.d("Network", "unavailable");
                            if (task.getId()== null) {
                                OfflineController controller = new OfflineController(getApplicationContext(), username);
                                controller.updateInFile(task);
                            } else {
                                OfflineController controller = new OfflineController(getApplicationContext(), username);
                                controller.saveInFile(task);
                            }

                        }

                    }
                }).start();

                LocalRequestedTaskController requestedTaskController = new LocalRequestedTaskController(getApplicationContext(), username);
                requestedTaskController.updateRequestedTask(task, index);


                Log.e("tasktitle edited",task.getTitle());
                Log.e("new des",task.getDescription());
                Log.e("size",String.valueOf(task.getImages().size()));

                //Intent requestedIntent = new Intent(EditDashboardRequestedTask.this, DashboardRequestedTask.class);
                intent.putExtra("title",task.getTitle());
                intent.putExtra("des",task.getDescription());
                intent.putExtra("size",task.getImages().size());



                setResult(RESULT_OK,intent);
                finish();
                //requestedIntent.putExtra("username",username);
                //requestedIntent.putExtra("id",id);
                //intent.putExtra("updatedTask",task);
                //startActivity(requestedIntent);

            }
        });
    }

    /**
     * Checks if the title and description inputted is under the size constraint
     * @return a boolean if the user input for title and description is under the size length
     */

    public boolean checkInput(){
        titleValue = editTitle.getText().toString();            // grab title from edit text input
        if (titleValue.length() >= 30 || titleValue.length() <= 0) {  // validating name input length
            Toast.makeText(getApplicationContext(), "Title is invalid length. Must be between 1-29 characters. ", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        descriptionValue = editDescription.getText().toString(); // grab description from edit text input
        if (descriptionValue.length() >= 300 || descriptionValue.length() <= 0) {               // validating name input length
            Toast.makeText(getApplicationContext(), "Description is invalid length. Must be between 1-299 characters. ", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;
    }


    /**
     * Create the option toolbar menu
     * @param menu gets the menu xml file for the UI layout
     * @return an option menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_task, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Checks which menu icon has been clicked
     * @param item the icon that has been clicked
     * @return a boolean if the icon has been clicked or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.camera:
                ArrayList<String> combined = task.getImages();
                combined.addAll(newImages);
                if (combined.size() >= 10){
                    Toast.makeText(getApplicationContext(), "Too many photos for task.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
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
     * This function creates the image file to be stored by the task object.
     *
     * @return a file holding the image to be stored
     * @throws IOException throws an exception when the file stream can not be established
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",  /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    /**
     * Grabs result from called activity
     * @param requestCode the code which we started the activity
     * @param resultCode the result of the called activity
     * @param data the intent that called the new activity
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
        }
        if (requestCode == EDIT_LOCATION && resultCode == RESULT_OK){
            LatLng point = data.getParcelableExtra("location");
            taskLoc = point;

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
