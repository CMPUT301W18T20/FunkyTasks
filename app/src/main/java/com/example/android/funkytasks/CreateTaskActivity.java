package com.example.android.funkytasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateTaskActivity extends AppCompatActivity {

    EditText title = (EditText)findViewById(R.id.AddTitle);
    EditText description = (EditText) findViewById(R.id.AddDescription);
    private String titleValue;
    private String descriptionValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int returnTitle = checkTitle();
                if (returnTitle == 0){
                    return;
                }
                int returnDescription = checkDescription();
                if (returnDescription == 0){
                    return;
                }

                //Task task = new Task(titleValue,descriptionValue,//USER WHO CREATED THE TASK HERE);

                //TODO ADD TASK TO ELASTIC SEARCH BUT FOR NOW ADD IT TO OUR PUBLIC ARRAYLIST OF TASKS

            }
        });

        Intent intent = getIntent();
    }

    private int checkTitle(){
        titleValue = title.getText().toString().trim();                       // grab name from edit text input
        if (titleValue.length() >= 30){                    // validating name input length
            Toast.makeText(getApplicationContext(),"Title must be at least 30 characters long ",Toast.LENGTH_SHORT)
                    .show();
            return 1;
        }
        return 0;
    }

    private int checkDescription(){
        descriptionValue = description.getText().toString().trim();                       // grab name from edit text input
        if (descriptionValue.length() >= 300){                    // validating name input length
            Toast.makeText(getApplicationContext(),"Description  must be at least 300 characters long ",Toast.LENGTH_SHORT)
                    .show();
            return 1;
        }
        return 0;
    }




}
