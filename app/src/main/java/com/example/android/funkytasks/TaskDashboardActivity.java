package com.example.android.funkytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;


public class TaskDashboardActivity extends AppCompatActivity {
    ArrayList<User> userArrayList = new ArrayList<User>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dashboard);
        userArrayList = ((GlobalVariables) this.getApplication()).getUserArrayList();
        User user1 = userArrayList.get(0);
        Task task = new Task("TITLE task","description",user1);
        bid bidz = new bid(user1,1.00);
        task.addBid(bidz);

        user1.addTask(task);

        ArrayList<Task> tasks = user1.getTasks();

        ListView dashboardView = (ListView) findViewById(R.id.myTasks);

// get data from the table by the ListAdapter
        ListViewAdapter customAdapter = new ListViewAdapter(this, 1, tasks);

        dashboardView .setAdapter(customAdapter);
        Intent intent = getIntent();



    }


}
