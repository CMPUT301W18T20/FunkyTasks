/**
 * UpdateBidDialogueFragment
 *
 * Version 1.0.0
 *
 * Create by Funky Tasks on March 8th
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.funkytasks.R;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * This fragment allows a task provider to update a bid they have already made on a task.
 */
public class UpdateBidDialogFragment extends DialogFragment {

    private Double bidAmount;
    private String requester;
    private String bidder;
    private String taskID;
    private TextView providerBidValue;
    private String cameFrom;

    Bid bid;

    /**
     * This function creates the dialogue pop-up that allows the user to update their bid
     * information. This function also updates the bid information.
     *
     * @param savedInstanceState a bundle representing the most recent state of the view
     * @return returns the dialogue window
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater

        builder.setTitle("Update Bid");
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_updatebid, null);

        requester = getArguments().getString("requester");
        bidder = getArguments().getString("bidder");
        taskID = getArguments().getString("id");
        cameFrom = getArguments().getString("cameFrom");



        ElasticSearchController.GetBidsByBidder bidderBids = new ElasticSearchController.GetBidsByBidder();
        bidderBids.execute(bidder); // grab all current users in the system

        ArrayList<Bid> bidList = new ArrayList<>();
        try {
            bidList = bidderBids.get();
        } catch (Exception e) {
            Log.e("Error", "Failed to get list of bidders");
        }

        int i = 0;

        for(Bid eachBid : bidList) {
            if (eachBid.getTaskID().equals(taskID)) {
                Log.e("TaskID", eachBid.getTaskID());
                Log.e("Amount", Double.toString(eachBid.getAmount()));
                break;
            }
            i++;
        }


        Log.e("Index", Integer.toString(i));
        Log.e("Bid list size", Integer.toString(bidList.size()));

        bid = bidList.get(i);

        providerBidValue = view.findViewById(R.id.taskProviderBid);
        String bidAmountString = Double.toString(bid.getAmount());
        providerBidValue.setText(bidAmountString);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Update Bid", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        EditText moneyPlaced = view.findViewById(R.id.bidMoney);
                        bidAmount = Double.valueOf(moneyPlaced.getText().toString());

                        Log.e("bidder in fragment", bidder);
                        Log.e("id in fragment", taskID);
                        Log.e("amount in fragment", moneyPlaced.getText().toString());

                        bid.setAmount(bidAmount);

                        Log.e("New amount", Double.toString(bid.getAmount()));

                        ElasticSearchController.updateBid updateBid = new ElasticSearchController.updateBid();
                        updateBid.execute(bid);

                        Log.e("New amount", Double.toString(bid.getAmount()));

                        Toast.makeText(getActivity(), "Successfully updated a bid", Toast.LENGTH_SHORT).show();


                        sendToSolveTaskActivity(bidder);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UpdateBidDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    /**
     * Loads a new view after editing the bid information
     *
     * @param username a string representing the task provider's username
     */
    public void sendToSolveTaskActivity(String username) {
        // otherwise go back to search screen
        if (cameFrom.equals("0")){
            Intent intent = new Intent(getActivity(), SolveTaskActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
        else if (cameFrom.equals("1")){
            Intent intent = new Intent(getActivity(), MyTasksActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }

}