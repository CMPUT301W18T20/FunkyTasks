package com.example.android.funkytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class TaskDashboardActivity extends AppCompatActivity {
    ArrayList<User> userArrayList = new ArrayList<User>();
    private String username;

    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<Task> taskList = new ArrayList<Task>();

    final int DELETECODE = 1;
    ArrayList<Task> requestedTasks;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dashboard);
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        username = LoginActivity.username;

        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(username);
        try {
            user = getUser.get();
            Log.e("Got the username: ", user.getUsername());

        } catch (Exception e) {
            Log.e("Error", "We arnt getting the user");
            return;
        }

        // Getting the all the tasks associated with the user
        ElasticSearchController.GetAllTask getAllTask = new ElasticSearchController.GetAllTask();
        getAllTask.execute(username);
        try {
            taskList = getAllTask.get();
            Log.e("Got the tasks ", taskList.toString());

        } catch (Exception e) {
            Log.e("Error", "We arnt getting the list of tasks");
            return;
        }


        listView = (ListView) findViewById(R.id.myTasks);
        listViewAdapter = new ListViewAdapter(this, R.layout.listviewitem, taskList);

        listView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged(); //TODO fix the delete functionality (works backend wise) but is not reflected in front end after

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TaskDashboardActivity.this, DashboardRequestedTask.class);
                intent.putExtra("username", username);
                Task detailedTask = taskList.get(i);

                ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();

                getTask.execute(detailedTask.getId());
                try {
                    Task x = getTask.get();
                    Log.e("Return task title", x.getTitle());
                } catch (Exception e) {
                    Log.e("Error", "Task get not working");
                }

                intent.putExtra("task", detailedTask);
                intent.putExtra("position", i);
                intent.putExtra("id", detailedTask.getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


}
