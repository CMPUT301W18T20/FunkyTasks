package com.example.android.funkytasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {
    public static ArrayList<Task> tasksArrayList = new ArrayList<Task>();
    ArrayList<User> userArrayList = new ArrayList<User>();
    private String username;
    final int ADD_CODE = 1;
    final int EDIT_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Main Menu");
        Intent intent = getIntent();

        userArrayList = ((GlobalVariables) this.getApplication()).getUserArrayList();
        Task task1 = new Task("Funky", "make ken happy", userArrayList.get(0));
        Bid bid1 = new Bid(userArrayList.get(0), 10.0);
        task1.addBid(bid1);
        tasksArrayList.add(task1);

        username = intent.getExtras().getString("username");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToDashboard(view);
            }
        });

        Button profile = (Button) findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, EditProfileActivity.class);
                intent.putExtra("username", username);
                startActivityForResult(intent, EDIT_CODE);
            }
        });


    }

    public void sendToDashboard(View view) {
        // JUST TESTING TO SEE IF THERE ARE TASKS IN THE USER
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(username);
        ArrayList<Task> checking;
        User user;
        try {
            user = getUser.get();
            Log.e("username test ", user.getUsername());
            checking = user.getRequestedTasks();
            for (Task i: checking){
                Log.e("print pls task",i.getTitle());
            }

        } catch (Exception e) {
            Log.e("Error", "We arnt getting the user");
            return;
        }
        //****************************************************

        Intent intent = new Intent(this, TaskDashboardActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void sendToCreateTaskActivity(View view) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        intent.putExtra("username", username);
        startActivityForResult(intent, ADD_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == ADD_CODE && resultCode == RESULT_OK) {

            Task newTask = (Task) intent.getSerializableExtra("task");

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

            user.addRequestedTask(newTask);

            // update user since we added task to it
            ElasticSearchController.updateUser updateUser = new ElasticSearchController.updateUser();
            updateUser.execute(user);

            // add task to global list of all tasks
            ElasticSearchController.PostTask postTask = new ElasticSearchController.PostTask();
            postTask.execute(newTask);
        }
    }
}
