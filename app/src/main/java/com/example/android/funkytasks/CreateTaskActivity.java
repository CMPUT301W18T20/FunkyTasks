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
//                int returnTitle = checkTitle();
//                if (returnTitle == 0){
//                    return;
//                }
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

                titleValue = title.getText().toString();                       // grab name from edit text input
                Log.e("title",titleValue);
                if (titleValue.length() >= 30) {                    // validating name input length
                    Toast.makeText(getApplicationContext(), "Title must be at least 30 characters long ", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
//                int returnDescription = checkDescription();
//                if (returnDescription == 0){
//                    return;
//                }

                descriptionValue = description.getText().toString(); // grab name from edit text input
                Log.e("decription",descriptionValue);
                if (descriptionValue.length() >= 300) {                    // validating name input length
                    Toast.makeText(getApplicationContext(), "Description  must be at least 300 characters long ", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                Task task = new Task(titleValue,descriptionValue,user);
                user.addRequestedTask(task);

                //TODO make elastic search work for updating tasks and user

                ElasticSearchController.updateUser updateUser = new ElasticSearchController.updateUser();
                updateUser.execute(user);

                ElasticSearchController.PostTask postTask = new ElasticSearchController.PostTask();
                postTask.execute(task);


//                // checking these updates
//                getUser.execute(username);
//                try{
//                    User y = getUser.get();
//                    if (y.getRequestedTasks().size() > 0) {
//                        for (Task task : y.getRequestedTasks()) {
//                            Log.e("task", task.getTitle());
//                        }
//                    }
//                }
//                catch (Exception e){
//                    Log.e("Error","ughhh");
//                }
//
                Intent intent = new Intent(CreateTaskActivity.this, MainMenuActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
//
            }
        });

    }

    private int checkTitle(){
        EditText title = findViewById(R.id.AddTitle);
        titleValue = title.getText().toString().trim();                       // grab name from edit text input
        if (titleValue.length() >= 30){                    // validating name input length
            Toast.makeText(getApplicationContext(),"Title must be at least 30 characters long ",Toast.LENGTH_SHORT)
                    .show();
            return 1;
        }
        return 0;
    }

    private int checkDescription(){
        EditText description = findViewById(R.id.AddDescription);
        descriptionValue = description.getText().toString().trim();                       // grab name from edit text input
        if (descriptionValue.length() >= 300){                    // validating name input length
            Toast.makeText(getApplicationContext(),"Description  must be at least 300 characters long ",Toast.LENGTH_SHORT)
                    .show();
            return 1;
        }
        return 0;
    }




}
