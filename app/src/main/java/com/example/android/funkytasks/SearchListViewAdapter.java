/**
 * SearchListViewAdapter
 *
 * Version 1.0.0
 *
 * Created by eivenlour on 2018-03-15.
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
 * This adapter displays the tasks provided by the search function.
 */
public class SearchListViewAdapter extends ArrayAdapter<Task> {

    /**
     * This is a view adapter to properly display the tasks a provider has searched for
     *
     * @param context the context of the app where the bids list will be displayed
     * @param resource a context instantiation parameter needed for the super class constructor
     * @param objects a list instantiation needed for the super class constructor
     */
    public SearchListViewAdapter(@NonNull Context context, int resource, @NonNull List objects) {
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
            view = LayoutInflater.from(getContext()).inflate(R.layout.search_listviewitem,
                    parent, false);
        }
        // Lookup view for data population
        TextView title = view.findViewById(R.id.taskTitle);
        //TextView description = view.findViewById(R.id.taskDescription);
        TextView status = view.findViewById(R.id.taskStatus);
        //TextView bid = view.findViewById(R.id.taskBid);
        TextView username = view.findViewById(R.id.taskUsername);
        TextView lowestBid = view.findViewById(R.id.taskLowestBid);


        ElasticSearchController.GetBidsByTaskID idBids = new ElasticSearchController.GetBidsByTaskID();
        idBids.execute(task.getId()); // grab all current users in the system

        ArrayList<Bid> bidsList = new ArrayList<>();
        try {
            bidsList = idBids.get();
        } catch (Exception e) {
            Log.e("Error", "Failed to get list of bidders");
        }

        String lowestBidString = "";

        title.setText(task.getTitle());
        status.setText(task.getStatus());
        username.setText(task.getRequester());


        if (task.getStatus().equals("requested")) {
            lowestBidString = "N/A";
        } else {
            if (bidsList != null && bidsList.size() > 0)
            lowestBidString = Double.toString(getLowestBid(bidsList).getAmount()) +
                    "  by  " + getLowestBid(bidsList).getBidder();
        }

        lowestBid.setText(lowestBidString);


        return view;

    }

    /**
     * Returns the lowest bid, if any, that is currently placed on a task
     *
     * @param bidsList an array list that holds all the bids that were returned by the search
     * @return a double representing the lowest bid on the task
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
