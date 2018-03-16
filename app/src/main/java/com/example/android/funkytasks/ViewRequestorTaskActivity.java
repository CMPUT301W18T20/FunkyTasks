package com.example.android.funkytasks;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewRequestorTaskActivity extends AppCompatActivity {

    private TextView titleValue;
    private TextView descriptionValue;
    private TextView statusValue;
    private TextView usernameValue;
    private Double bidAmount;
    private String id;
    private String username;
    private Task task;
    private int index;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requestor_task);


        descriptionValue = (TextView) findViewById(R.id.requestorTaskDescription);
        titleValue = (TextView) findViewById(R.id.requestorTaskName);
        statusValue = (TextView) findViewById(R.id.requestorTaskStatus);
        usernameValue = (TextView) findViewById(R.id.requestorTaskUsername);

        final Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        username = LoginActivity.username;

        id = intent.getExtras().getString("id");

        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();

        getTask.execute(id);
        try {
            task = getTask.get();
            Log.e("Return task title", task.getTitle());
        } catch (Exception e) {
            Log.e("Task get", "not workng");
        }

        //TODO SET REQUESTER'S CONTACT INFO TO BE SHOWN ON SCREEN WITH PHONE AND EMAIL (use case 3.3)

        titleValue.setText(task.getTitle());
        descriptionValue.setText(task.getDescription());
        statusValue.setText(task.getStatus());
        usernameValue.setText(task.getRequester());


        Button bidButton = (Button) findViewById(R.id.bidButton);

        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    placeBidFragment = newInstance(placeBidFragment, username, id);
                    placeBidFragment.show(getSupportFragmentManager(), "Bids");

                } else {

                    for (Bid eachBid : bidsList) {
                        if (eachBid.getBidder().equals(username)) {
                            DialogFragment updateBidFragment = new UpdateBidDialogFragment();
                            updateBidFragment = newInstance(updateBidFragment, username, id);
                            updateBidFragment.show(getSupportFragmentManager(), "Bids");
                            break;
                        }
                        if (i == sizeBidsList - 1) {
                            DialogFragment placeBidFragment = new PlaceBidDialogFragment();
                            placeBidFragment = newInstance(placeBidFragment, username, id);
                            placeBidFragment.show(getSupportFragmentManager(), "Bids");
                        }
                        i++;

                    }
                }




                //Integer numBids = task.getNumberOfBids();
                //numBids += 1;
                //task.setNumberOfBids(numBids);

                //Log.e("Current number of bids ", numBids.toString());

                //Intent intent = new Intent(ViewRequestorTaskActivity.this, MainMenuActivity.class);
                //startActivity(intent);

            }
        });
    }

    public DialogFragment newInstance(DialogFragment bidFragment, String username, String id) {

        // Supply num input as an argument.
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("id", id);

        Log.e("username in newInstance", username);
        Log.e("id in new instance", id);
        bidFragment.setArguments(bundle);

        return bidFragment;
    }


}
