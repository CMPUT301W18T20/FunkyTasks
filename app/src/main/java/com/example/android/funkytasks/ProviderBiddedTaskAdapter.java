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



public class ProviderBiddedTaskAdapter extends ArrayAdapter<Task>{

    private String username = LoginActivity.username;

    public ProviderBiddedTaskAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

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

        ArrayList<Bid> bids = new ArrayList<Bid>();
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

        TextView requesterUsername = (TextView) view.findViewById(R.id.requesterUsername);
        TextView title = (TextView) view.findViewById(R.id.taskTitle);
        TextView status = (TextView) view.findViewById(R.id.taskStatus);
        TextView lowestbid = (TextView) view.findViewById(R.id.lowestBid);
        TextView myBid = (TextView) view.findViewById(R.id.myBid);

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
