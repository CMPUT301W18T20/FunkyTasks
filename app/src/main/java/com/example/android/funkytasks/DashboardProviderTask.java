/**
 * This activity displays the provider's tasks and gives the activity_dashboard_provider_task
 * view functionality.
 *
 * Version 1.0.0
 *
 * Created on March 8th by Funky Tasks
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */
package com.example.android.funkytasks;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.android.funkytasks.SearchListViewAdapter.getLowestBid;


public class DashboardProviderTask extends AppCompatActivity {

    private TextView titleValue;
    private TextView descriptionValue;
    private TextView lowestBidValue;
    private TextView myBidValue;
    private TextView requesterName;
    private TextView requesterEmail;
    private TextView requesterPhone;


    private TextView statusValue;
    private String id;
    private Button multiFunctionButton;

    private String username;
    private Task task;
    private int index;
    private int EDIT_CODE = 1;

    ListViewAdapter listViewAdapter;
    ArrayList<Bid> bidList = new ArrayList<Bid>();

    /**
     * Overrides the onCreate super class and instantiates the proper view for this class
     * 
     * @param savedInstanceState a bundle of the previous saved instance state that is used to
     *                           load a snapshot of the app in the state it was last in
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_provider_task);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.DashboardProviderTasktoolbar);
        //setSupportActionBar(myToolbar);

        // set bids listview

        descriptionValue = findViewById(R.id.textDescriptionprovider);
        titleValue = findViewById(R.id.taskNamerequester);
        statusValue = findViewById(R.id.taskStatustext);
        multiFunctionButton = findViewById(R.id.multiFunction);
        lowestBidValue = findViewById(R.id.lowestBidAmount);
        myBidValue = findViewById(R.id.myBidAmount);
        requesterName = (TextView) findViewById(R.id.taskRequesterUsername);
        requesterEmail = (TextView) findViewById(R.id.taskRequesterEmail);
        requesterPhone = (TextView) findViewById(R.id.taskRequesterPhone);

        final Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        username = LoginActivity.username;
        task = (Task) intent.getSerializableExtra("task");
        index = intent.getExtras().getInt("position");
        id = intent.getExtras().getString("id");


//         TODO implement each button function
        if(task.getStatus().equals("bidded")){
            Log.e("Provider task status",task.getStatus());
            multiFunctionButton.setText("UPDATE BID");
        }

        if(task.getStatus().equals("assigned")){
            Log.e("Provider task status",task.getStatus());
            multiFunctionButton.setText("UPDATE STATUS");
        }
        if(task.getStatus().equals("done")){

        }

        setTaskDetails();


        if(task.getStatus().equals("bidded")){
            multiFunctionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO implement this later
                }
            });
        }

    }


    public void setTaskDetails(){

        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();
        getTask.execute(id);
        try{
            task = getTask.get();
            Log.e("Return task title",task.getTitle());

        }
        catch(Exception e){
            Log.e("Task get","not working");
        }
        titleValue.setText(task.getTitle());
        descriptionValue.setText(task.getDescription());
        statusValue.setText(task.getStatus());
        ArrayList<Bid> bids = new ArrayList<Bid>();
        ElasticSearchController.GetBidsByTaskID getBidsByTaskID = new ElasticSearchController.GetBidsByTaskID();
        getBidsByTaskID.execute(task.getId());
        try{
            bids = getBidsByTaskID.get();
        }
        catch(Exception e){
            Log.e("Error","Something wrong with getting bids in adapter");
        }
        Double lowest = getLowestBid(bids).getAmount();
        lowestBidValue.setText(lowest.toString());
        for (Bid bid: bids){
            if (bid.getBidder().equals(username)){
                myBidValue.setText(String.valueOf(bid.getAmount()));
                break;
            }
        }
        requesterName.setText(task.getRequester());
        ElasticSearchController.GetUser getRequester= new ElasticSearchController.GetUser();
        getRequester.execute(task.getRequester());
        User requester=new User("","","");
        try{
            requester = getRequester.get();
            Log.e("Return requester",requester.getUsername());
        }
        catch(Exception e){
            Log.e("Requester name get","not workng");
        }
        requesterPhone.setText(requester.getPhonenumber());
        requesterEmail.setText(requester.getEmail());

    }
}
