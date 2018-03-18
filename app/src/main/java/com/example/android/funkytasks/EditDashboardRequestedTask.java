/**
 * This activity allows a user to edit a task that has status "requested"
 *
 * Version 1.0.0
 *
 * Created on March 8th by Funky Tasks
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity allows a user to edit a task
 */
public class EditDashboardRequestedTask extends AppCompatActivity {
    private String username;
    private String id;
    private EditText editTitle;
    private EditText editDescription;
    private Button saveBT;
    private Task task;
    private int index;
    private String titleValue;
    private String descriptionValue;


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

        index = intent.getExtras().getInt("index");
        id = intent.getExtras().getString("id");


        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();
        getTask.execute(id);
        try {
            task = getTask.get();
            Log.e("Got the task",task.getTitle());

        } catch (Exception e) {
            Log.e("Error", "We aren't getting the task");
            return;
        }

        editTitle.setText(task.getTitle());
        editDescription.setText(task.getDescription());

        saveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleValue = editTitle.getText().toString();            // grab title from edit text input
                if (titleValue.length() >= 30) {                    // validating name input length
                    Toast.makeText(getApplicationContext(),
                            "Title must be at least 30 characters long ", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                descriptionValue = editDescription.getText().toString(); // grab description from edit text input
                if (descriptionValue.length() >= 300) {               // validating name input length
                    Toast.makeText(getApplicationContext(),
                            "Description must be at least 300 characters long ", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                task.setDescription(descriptionValue);
                task.setTitle(titleValue);

                ElasticSearchController.updateTask updateTask = new ElasticSearchController.updateTask();
                updateTask.execute(task);

                Log.e("tasktitle edited",task.getTitle());

                setResult(RESULT_OK,intent);
                intent.putExtra("id",id);
                intent.putExtra("updatedTask",task);
                finish();

            }
        });

    }

}
