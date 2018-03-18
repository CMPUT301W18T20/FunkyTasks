/**
 * ProviderBiddedTaskAdapter
 *
 * Version 1.0.0
 *
 * Create by Funky Tasks on March 8th
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * This adapter allows a provider to view all the tasks they have bidded on
 */
public class ProviderBiddedTaskAdapter extends ArrayAdapter<Task>{

    private String username = LoginActivity.username;

    /**
     * This is a view adapter to properly display the tasks a provider has bidded on
     *
     * @param context the context of the app where the bids list will be displayed
     * @param resource a context instantiation parameter needed for the super class constructor
     * @param objects a list instantiation needed for the super class constructor
     */
    public ProviderBiddedTaskAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    /**
     * Overrides the original getView function. Returns the view where the list should be displayed.
     *
     * @param position an integer representing the position of the list
     * @param view a view object representing the view where the list is to be displayed
     * @param parent a view group object that holds the parent view for this activity
     * @return returns the desired view
     */
    @Override
    public View getView(int position, View view, ViewGroup parent){
        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (view  == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.providerbidtaskitem, parent, false);
        }

        // Check to see if task is null
        if (task == null) {
            return view;
        }

        ArrayList<Bid> bids = new ArrayList<>();
        ElasticSearchController.GetBidsByTaskID getBidsByTaskID = new ElasticSearchController.GetBidsByTaskID();
        getBidsByTaskID.execute(task.getId());
        try{
            bids = getBidsByTaskID.get();
        }
        catch(Exception e){
            Log.e("Error","Something wrong with getting bids in adapter");
        }

        // US 05.02.01 (revised 2018-02-14)
        // As a task provider, I want to view a list of tasks that I have bidded on, each with its task requester username,
        // title, status, lowest bid so far, and my bid.

        TextView requesterUsername = view.findViewById(R.id.requesterUsername);
        TextView title = view.findViewById(R.id.taskTitle);
        TextView status = view.findViewById(R.id.taskStatus);
        TextView lowestbid = view.findViewById(R.id.lowestBid);
        TextView myBid = view.findViewById(R.id.myBid);

        Bid lowest = getLowestBid(bids);
        Bid mybid;

        for (Bid bid: bids){
            if (bid.getBidder().equals(username)){
                mybid = bid;
                myBid.setText(String.valueOf(mybid.getAmount()));
                break;
            }
        }


        requesterUsername.setText(task.getRequester());
        title.setText(task.getTitle());
        status.setText(task.getStatus());
        lowestbid.setText(String.valueOf(lowest.getAmount()));
        return view;

    }


    /**
     * Returns the lowest bid that is currently placed on a task.
     *
     * @param bidsList an array list of all the bids currently placed on a task
     * @return returns a double containing the lowest bid
     */
    private static Bid getLowestBid(ArrayList<Bid> bidsList){
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
        return lowestBid;
    }

}
