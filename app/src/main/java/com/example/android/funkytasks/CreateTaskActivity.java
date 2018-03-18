/**
 * This activity allows the user to create a task.
 *
 * Version 1.0.0
 *
 * Created on March 8th by Funky Tasks
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateTaskActivity extends AppCompatActivity {

//    EditText title = (EditText)findViewById(R.id.AddTitle);
//    EditText description = (EditText) findViewById(R.id.AddDescription);
    private String titleValue;
    private String descriptionValue;
    private String username;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        setTitle("Create a Task");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Create a Task");

        final Intent intent = getIntent();

        username = intent.getExtras().getString("username");
        username = LoginActivity.username;

        final EditText title = (EditText)findViewById(R.id.AddTitle);
        final EditText description = (EditText)findViewById(R.id.AddDescription);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                titleValue = title.getText().toString();            // grab title from edit text input
                if (titleValue.length() >= 30 || titleValue.length() <= 0) {                    // validating name input length
                    Toast.makeText(getApplicationContext(), "Title is invalid length. Must be between 1-29 characters. ", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                descriptionValue = description.getText().toString(); // grab description from edit text input
                if (descriptionValue.length() >= 300) {               // validating name input length
                    Toast.makeText(getApplicationContext(), "Description is invalid length. Must be between 1-299 characters. ", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                final Task task = new Task(titleValue,descriptionValue,username);

                intent.putExtra("username",username);
                intent.putExtra("task",task);
                setResult(RESULT_OK,intent);
                finish();

            }
        });

    }
}
