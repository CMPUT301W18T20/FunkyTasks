/**
 * ViewRequesterTaskActivity
 *
 * Version 1.0.0
 *
 * Create by Funky Tasks on March 8th
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
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

/**
 * This activity allows a user to view the activities of a task that they have searched
 * for and selected.
 */
public class ViewRequestorTaskActivity extends BaseActivity {

    private String id;
    private String bidder;
    private String requester;
    private String phoneNumber;
    private String email;
    private Task task;
    private User user;
    private String cameFrom = "0";

    /**
     * Overrides the default onCreate function and prepares the app for interaction.
     * This function also loads all the task information and displays it on screen.
     *
     * @param savedInstanceState a bundle holding the most recent save state of the app
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requestor_task);


        TextView descriptionValue = findViewById(R.id.requestorTaskDescription);
        TextView titleValue = findViewById(R.id.requestorTaskName);
        TextView statusValue = findViewById(R.id.requestorTaskStatus);
        TextView usernameValue = findViewById(R.id.requestorTaskUsername);
        TextView lowestBidValue = findViewById(R.id.requestorTaskLowestBid);
        TextView phoneNumberValue = findViewById(R.id.requestorPhoneNumber);
        TextView emailValue = findViewById(R.id.requestorEmail);


        final Intent intent = getIntent();
        bidder = intent.getExtras().getString("username");
        bidder = LoginActivity.username;

        id = intent.getExtras().getString("id");

        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();

        getTask.execute(id);
        try {
            task = getTask.get();
            Log.e("Return task title", task.getTitle());
        } catch (Exception e) {
            Log.e("Task get", "not working");
        }

        requester = task.getRequester();
        Log.e("task.getRequester", requester);

        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(requester);

        try {
            user = getUser.get();
            Log.e("Return username", user.getUsername());
        } catch (Exception e) {
            Log.e("User get", "not working");
        }

        email = user.getEmail();
        phoneNumber = user.getPhonenumber();


        ElasticSearchController.GetBidsByTaskID idBids = new ElasticSearchController.GetBidsByTaskID();
        idBids.execute(task.getId()); // grab all current users in the system

        ArrayList<Bid> bidsList = new ArrayList<Bid>();
        try {
            bidsList = idBids.get();
        } catch (Exception e) {
            Log.e("Error", "Failed to get list of bidders");
        }

        String lowestBidString = "";

        titleValue.setText(task.getTitle());
        descriptionValue.setText(task.getDescription());
        statusValue.setText(task.getStatus());
        usernameValue.setText(requester);
        phoneNumberValue.setText(phoneNumber);
        emailValue.setText(email);


        if (task.getStatus().equals("requested")) {
            lowestBidString = "N/A";
        } else {
            lowestBidString = Double.toString(getLowestBid(bidsList).getAmount()) + "  by  " + getLowestBid(bidsList).getBidder();
        }

        lowestBidValue.setText(lowestBidString);


        Button bidButton =  findViewById(R.id.bidButton);

        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If user has never placed a bid on the task yet:
                ElasticSearchController.GetBidsByTaskID idBids = new ElasticSearchController.GetBidsByTaskID();
                idBids.execute(id); // grab all current users in the system

                ArrayList<Bid> bidsList = new ArrayList<>();
                try {
                    bidsList = idBids.get();
                } catch (Exception e) {
                    Log.e("Error", "Failed to get list of bidders");
                }

                int i = 0;
                int sizeBidsList = bidsList.size();

                DialogFragment placeBidFragment = new PlaceBidDialogFragment();
                placeBidFragment = newInstance(placeBidFragment, requester, bidder, id);

                DialogFragment updateBidFragment = new UpdateBidDialogFragment();
                updateBidFragment = newInstance(updateBidFragment, requester, bidder, id);

                if (bidsList.isEmpty()) {
                    placeBidFragment.show(getSupportFragmentManager(), "Bids");

                } else {
                    for (Bid eachBid : bidsList) {
                        if (eachBid.getBidder().equals(bidder)) {
                            updateBidFragment.show(getSupportFragmentManager(), "Bids");
                            break;
                        }
                        if (i == sizeBidsList - 1) {
                            placeBidFragment.show(getSupportFragmentManager(), "Bids");
                        }
                        i++;
                    }
                }



            }
        });
    }

    /**
     * States what happens when the back button is pressed
     */
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Creates a new dialogue fragment to be displayed on screen
     *
     * @param bidFragment a dialogue fragment that holds the bid information
     * @param requester a string that represents the task requester
     * @param bidder a string representing the task bidder
     * @param id a string representing the task ID
     * @return returns a dialogue fragment to be displayed on screen
     */
    public DialogFragment newInstance(DialogFragment bidFragment, String requester, String bidder, String id) {

        // Supply num input as an argument.
        Bundle bundle = new Bundle();
        bundle.putString("requester", requester);
        bundle.putString("bidder", bidder);
        bundle.putString("id", id);
        bundle.putString("cameFrom",cameFrom);


        Log.e("requester / newInstance", requester);
        Log.e("bidder in newInstance", bidder);
        Log.e("id in new instance", id);
        bidFragment.setArguments(bundle);

        return bidFragment;
    }

    /**
     * Returns the lowest bid that is currently placed on a task
     *
     * @param bidsList an array list that holds all the bids that are currently placed on a task
     * @return returns a double representing the lowest bid on a task
     */
    public static Bid getLowestBid(ArrayList<Bid> bidsList){
        int i = 0;
        Bid lowestBid = bidsList.get(i);
        Double lowestBidAmount = lowestBid.getAmount();

        if (bidsList.size() > 1) {
            for (i = 1; i < bidsList.size(); i++) {
                if (lowestBidAmount > bidsList.get(i).getAmount()) {
                    Log.e("Lowest bid", Double.toString(bidsList.get(i).getAmount()));
                    lowestBid = bidsList.get(i);
                }
            }
        }
        Log.e("Index", Integer.toString(i));

        return lowestBid;
    }

}
