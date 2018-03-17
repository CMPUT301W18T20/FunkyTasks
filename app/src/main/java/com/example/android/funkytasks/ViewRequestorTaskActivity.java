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

public class ViewRequestorTaskActivity extends AppCompatActivity {

    private TextView titleValue;
    private TextView descriptionValue;
    private TextView statusValue;
    private TextView usernameValue;
    private TextView lowestBidValue;
    private TextView phoneNumberValue;
    private TextView emailValue;
    private Double bidAmount;
    private String id;
    private String bidder;
    private String requester;
    private String phoneNumber;
    private String email;
    private Task task;
    private User user;
    private int index;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requestor_task);


        descriptionValue = (TextView) findViewById(R.id.requestorTaskDescription);
        titleValue = (TextView) findViewById(R.id.requestorTaskName);
        statusValue = (TextView) findViewById(R.id.requestorTaskStatus);
        usernameValue = (TextView) findViewById(R.id.requestorTaskUsername);
        lowestBidValue = (TextView) findViewById(R.id.requestorTaskLowestBid);
        phoneNumberValue = (TextView) findViewById(R.id.requestorPhoneNumber);
        emailValue = (TextView) findViewById(R.id.requestorEmail);


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
            Log.e("Task get", "not workng");
        }

        requester = task.getRequester();
        Log.e("task.getRequester", requester);

        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(requester);

        try {
            user = getUser.get();
            Log.e("Return username", user.getUsername());
        } catch (Exception e) {
            Log.e("User get", "not workng");
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
                    placeBidFragment = newInstance(placeBidFragment, requester, bidder, id);
                    placeBidFragment.show(getSupportFragmentManager(), "Bids");

                } else {

                    for (Bid eachBid : bidsList) {
                        if (eachBid.getBidder().equals(bidder)) {
                            DialogFragment updateBidFragment = new UpdateBidDialogFragment();
                            updateBidFragment = newInstance(updateBidFragment, requester, bidder, id);
                            updateBidFragment.show(getSupportFragmentManager(), "Bids");
                            break;
                        }
                        if (i == sizeBidsList - 1) {
                            DialogFragment placeBidFragment = new PlaceBidDialogFragment();
                            placeBidFragment = newInstance(placeBidFragment, requester, bidder, id);
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
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
