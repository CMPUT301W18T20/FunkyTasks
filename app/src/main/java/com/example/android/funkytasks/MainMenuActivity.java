package com.example.android.funkytasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Main Menu");
        Intent intent = getIntent();

        userArrayList = ((GlobalVariables) this.getApplication()).getUserArrayList();
        Task task1= new Task("Funky","make ken happy",userArrayList.get(0));
        Bid bid1= new Bid(userArrayList.get(0),10.0);
        task1.addBid(bid1);
        tasksArrayList.add(task1);

        //********
        username = intent.getExtras().getString("username");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToDashboard(view);
            }
        });

    }

    public void sendToDashboard(View view){
        Intent intent = new Intent(this, TaskDashboardActivity.class);
        startActivity(intent);
    }
    public void sendToCreateTaskActivity(View view){
        Intent intent = new Intent(this, CreateTaskActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    public void sendToEditProfileActivity(View view){
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}
