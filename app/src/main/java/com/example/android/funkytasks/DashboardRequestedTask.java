package com.example.android.funkytasks;

import android.content.ClipData;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class DashboardRequestedTask extends AppCompatActivity {

    private TextView titleValue;
    private TextView descriptionValue;
    private ListView bidLV;
    private String Id;
    private Button deleteBT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_requested_task);

        deleteBT=(Button)findViewById(R.id.deleteButton);

        // set bids listview
        bidLV=(ListView)findViewById(R.id.bidlistView);

        /*ArrayList<Bid> bids = setTaskDetails();
        final ArrayAdapter bidAdapter = new ArrayAdapter<Bid>(DashboardRequestedTask.this, android.R.layout.simple_list_item_1,bids);
        bidLV.setAdapter(bidAdapter);

        //
        bidLV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO display bid details;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        setTaskDetails();



        // delete a task
        deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteTask();
                Toast.makeText(getApplicationContext(), "deleted ", Toast.LENGTH_SHORT)
                        .show();
                sendToTaskDashboard(view);
            }
        });


    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editmenu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    // handle button activities
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.editButton) {
            Button edit = (Button) findViewById(R.id.editButton);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendToEditDashboardRequestedTask(view);
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    //set title,description and  get bid arraylist
    //TODO E.S get a Task by id.
    public void setTaskDetails() {
        descriptionValue=(TextView)findViewById(R.id.textDescription);
        titleValue=(TextView) findViewById(R.id.taskName);
        Intent intent= getIntent();
        Id = intent.getExtras().getString("id");                       //for testing

        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();
        getTask.execute(Id);

        Task task;
        String taskTitle;
        String taskDescription;
        ArrayList<Bid> taskBids =  new ArrayList<Bid>();


        //  get Title, description and bids;
        try{
            task = getTask.get();
            Log.e("Got the Title ", task.getTitle());
            Log.e("Got the Description ", task.getDescription());
            taskTitle= task.getTitle();
            taskDescription=task.getDescription();
            taskBids = task.getBids();

        }catch (Exception e) {
            Log.e("Error", "We arnt getting the task");
            return;

        }
        // display task Title and Description
        titleValue.setText(taskTitle);
        descriptionValue.setText(taskDescription);
    }


    public void onDeleteTask(){

        Task task;

        Intent intent= getIntent();
        Id = intent.getExtras().getString("id");
        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();
        getTask.execute(Id);

        try{
            task = getTask.get();
            Log.e("Got the Title ", task.getTitle());
        }catch (Exception e) {
            Log.e("Error", "We arnt getting the task");
            return;
        }

        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(task.getRequester().getUsername());

        User user;
        try {
            user = getUser.get();
            Log.e("Got the username: ", user.getUsername());

        } catch (Exception e) {
            Log.e("Error", "We arnt getting the user");
            return;
        }


        user.deleteRequestedTask(task);

        // delete task in global list of all tasks
        ElasticSearchController.deleteTask deleteTask = new ElasticSearchController.deleteTask();
        deleteTask.execute(task);

        ElasticSearchController.updateUser updateUser = new ElasticSearchController.updateUser();
        updateUser.execute(user);

    }

    public void sendToTaskDashboard(View view){
        Intent intent = new Intent(this, TaskDashboardActivity.class);
        startActivity(intent);
    }

    public void sendToEditDashboardRequestedTask(View view){
        Intent intent = new Intent(this, EditDashboardRequestedTask.class);
        startActivity(intent);
    }
}
