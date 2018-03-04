package com.example.android.funkytasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        setTitle("Create a Task");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();

        username = intent.getExtras().getString("username");

        final EditText title = (EditText)findViewById(R.id.AddTitle);
        final EditText description = (EditText)findViewById(R.id.AddDescription);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
                getUser.execute(username);

                User user;
                try {
                    user = getUser.get();
                    Log.e("Got the username: ", user.getUsername());

                } catch (Exception e) {
                    Log.e("Error", "We arnt getting the user");
                    return;
                }

                titleValue = title.getText().toString();            // grab title from edit text input
                if (titleValue.length() >= 30) {                    // validating name input length
                    Toast.makeText(getApplicationContext(), "Title must be at at max 30 characters long ", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                descriptionValue = description.getText().toString(); // grab description from edit text input
                if (descriptionValue.length() >= 300) {               // validating name input length
                    Toast.makeText(getApplicationContext(), "Description  must be at max 300 characters long ", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                Task task = new Task(titleValue,descriptionValue,user);

                intent.putExtra("username",username);
                intent.putExtra("task",task);
                setResult(RESULT_OK,intent);
                finish();
//
            }
        });

    }

}
