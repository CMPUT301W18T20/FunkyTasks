package com.example.android.funkytasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {
    public static ArrayList<Task> tasksArrayList = new ArrayList<Task>();
    ArrayList<User> userArrayList = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userArrayList = ((GlobalVariables) this.getApplication()).getUserArrayList();
        Task task1= new Task("Funky","make ken happy",userArrayList.get(0));
        tasksArrayList.add(task1);



        Intent intent = getIntent();

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
        startActivity(intent);
    }
}
