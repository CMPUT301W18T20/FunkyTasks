package com.example.android.funkytasks;

import android.content.ClipData;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private String Title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_requested_task);

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
    public void setTaskDetails() {
        descriptionValue=(TextView)findViewById(R.id.textDescription);
        titleValue=(TextView) findViewById(R.id.taskName);


        final Intent intent= getIntent();
        Title = intent.getExtras().getString("title");                       //for testing
        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();
        getTask.execute(Title);

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


    public void sendToEditDashboardRequestedTask(View view){
        Intent intent = new Intent(this, EditDashboardRequestedTask.class);

        startActivityForResult(intent, 42);
    }
}
