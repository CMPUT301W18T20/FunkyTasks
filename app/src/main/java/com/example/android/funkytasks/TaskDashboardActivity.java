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
    ArrayList <Task> taskList = new ArrayList<Task>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dashboard);
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(username);
        final User user;
        try{
            user = getUser.get();
            Log.e("Got the username: ", user.getUsername());
            Log.e("asdsdas",user.getRequestedTasks().toString());


        } catch (Exception e) {
            Log.e("Error", "We arnt getting the user");
            return;
        }
        // Setting up adapter to listen for the respective list

        listView = (ListView) findViewById(R.id.myTasks);
        taskList = user.getRequestedTasks();
        listViewAdapter = new ListViewAdapter(this, R.layout.listviewitem, taskList);

        listView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();

//        ListView dashboardView = (ListView) findViewById(R.id.myTasks);
//        // get data from the table by the ListAdapter
//        ListViewAdapter customAdapter = new ListViewAdapter(this, 1, user.getRequestedTasks());
//        dashboardView .setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TaskDashboardActivity.this,DashboardRequestedTask.class);
                intent.putExtra("username",username);
                Task detailedTask = user.getRequestedTasks().get(i);
                intent.putExtra("task",detailedTask);
                startActivity(intent);
            }
        });


    }
}
