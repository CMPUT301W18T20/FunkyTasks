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
 * Created by eivenlour on 2018-03-15.
 */

public class SearchListViewAdapter extends ArrayAdapter<Task> {

    public SearchListViewAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (view  == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.search_listviewitem, parent, false);
        }
        // Lookup view for data population
        TextView title = (TextView) view.findViewById(R.id.taskTitle);
        //TextView description = (TextView) view.findViewById(R.id.taskDescription);
        TextView status = (TextView) view.findViewById(R.id.taskStatus);
        //TextView bid = (TextView) view.findViewById(R.id.taskBid);
        TextView username = (TextView) view.findViewById(R.id.taskUsername);
        TextView lowestbid = (TextView) view.findViewById(R.id.taskLowestBid);


        ElasticSearchController.GetBidsByTaskID idBids = new ElasticSearchController.GetBidsByTaskID();
        idBids.execute(task.getId()); // grab all current users in the system

        ArrayList<Bid> bidsList = new ArrayList<Bid>();
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
        } else if (task.getStatus().equals("bidded")) {
            lowestBidString = Double.toString(getLowestBid(bidsList).getAmount()) + "  by  " + getLowestBid(bidsList).getBidder();
        }

        lowestbid.setText(lowestBidString);


        return view;

    }

    public static Bid getLowestBid(ArrayList<Bid> bidsList){
        int i = 0;
        Bid lowestBid;
        Double lowestBidAmount = bidsList.get(i).getAmount();

        if (bidsList.size() > 1) {
            for (i = 1; i < bidsList.size(); i++) {
                if (lowestBidAmount > bidsList.get(i).getAmount()) {
                    Log.e("Lowest bid", Double.toString(bidsList.get(i).getAmount()));
                }
            }
        }
        Log.e("Index", Integer.toString(i));
        lowestBid = bidsList.get(i);

        return lowestBid;
    }


}
