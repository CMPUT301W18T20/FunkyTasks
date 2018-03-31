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

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

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
    private ArrayList<String> newImages;
    private ArrayList<Task> tasks;

    private ImageConverterController imageConvert;


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

        newImages = new ArrayList<String>();

        index = intent.getExtras().getInt("index");
        Log.e("index", String.valueOf(index));
        id = intent.getExtras().getString("id");
        task = (Task) intent.getSerializableExtra("task");
        username = intent.getExtras().getString("username");



        editTitle.setText(task.getTitle());
        editDescription.setText(task.getDescription());


        saveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean input = checkInput();
                if (!input){
                    return;
                }

                task.setDescription(descriptionValue);
                task.setTitle(titleValue);

                if (newImages.size() > 0) {
                    boolean check = imageConvert.checkImages(newImages);
                    if (!check) {
                        Toast.makeText(getApplicationContext(), "Image size is too large", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    ArrayList<String> combined = task.getImages();
                    combined.addAll(newImages);
                    task.setImagesList(combined);
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

                setResult(RESULT_OK,intent);
                intent.putExtra("id",id);
                intent.putExtra("updatedTask",task);
                finish();

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
        if (descriptionValue.length() >= 300) {               // validating name input length
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
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap newImage = (Bitmap) extras.get("data");
            Log.e("newimage",newImage.toString());
            newImages.add(imageConvert.convertToString(newImage));

        }
    }

    //https://stackoverflow.com/questions/30343011/how-to-check-if-an-android-device-is-online
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
