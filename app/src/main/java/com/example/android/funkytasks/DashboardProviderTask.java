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
import android.support.v4.app.DialogFragment;
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

/**
 * This activity displays the tasks to the task provider.
 */
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

        multiFunctionButton = findViewById(R.id.multiFunction);


        if(task.getStatus().equals("bidded")){
            Log.e("Provider task status",task.getStatus());
            multiFunctionButton.setText("UPDATE BID");
        }
        else{
            multiFunctionButton.setVisibility(View.GONE);
        }


        setTaskDetails();


        if(task.getStatus().equals("bidded")){
            multiFunctionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO implement this later
                    // If user has never placed a bid on the task yet:
                    ElasticSearchController.GetBidsByTaskID idBids = new ElasticSearchController.GetBidsByTaskID();
                    idBids.execute(id); // grab all current users in the system

                    ArrayList<Bid> bidsList = new ArrayList<Bid>();
                    try {
                        bidsList = idBids.get();
                    } catch (Exception e) {
                        Log.e("Error", "Failed to get list of bidders");
                    }

                    int i = 0;
                    int sizeBidsList = bidsList.size();

                    if (bidsList.isEmpty()) {
                        DialogFragment placeBidFragment = new PlaceBidDialogFragment();
                        placeBidFragment = newInstance(placeBidFragment, task.getRequester(), username, id);
                        placeBidFragment.show(getSupportFragmentManager(), "Bids");

                    } else {

                        for (Bid eachBid : bidsList) {
                            if (eachBid.getBidder().equals(username)) {
                                DialogFragment updateBidFragment = new UpdateBidDialogFragment();
                                updateBidFragment = newInstance(updateBidFragment, task.getRequester(), username, id);
                                updateBidFragment.show(getSupportFragmentManager(), "Bids");
                                break;
                            }
                            if (i == sizeBidsList - 1) {
                                DialogFragment placeBidFragment = new PlaceBidDialogFragment();
                                placeBidFragment = newInstance(placeBidFragment, task.getRequester(), username, id);
                                placeBidFragment.show(getSupportFragmentManager(), "Bids");
                            }

                            i++;

                        }
                    }



                }
            });
        }

    }

    public DialogFragment newInstance(DialogFragment bidFragment, String requester, String bidder, String id) {

        // Supply num input as an argument.
        Bundle bundle = new Bundle();
        bundle.putString("requester", requester);
        bundle.putString("bidder", bidder);
        bundle.putString("id", id);


        Log.e("requester / newInstance", requester);
        Log.e("bidder in newInstance", bidder);
        Log.e("id in new instance", id);
        bidFragment.setArguments(bundle);

        return bidFragment;
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
