/**
 * BidListViewAdapter
 *
 * Version 1.0.0
 *
 * Created by jimi on 2018-03-16.
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * This adapter dictates how the bids should be displayed on screen.
 */
public class BidListViewAdapter extends ArrayAdapter<Bid> {

    /**
     * This is a view adapter to properly display all the bids.
     *
     * @param context the context of the app where the bids list will be displayed
     * @param resource a context instantiation parameter needed for the super class constructor
     * @param objects a list instantiation needed for the super class constructor
     */
    public BidListViewAdapter(@NonNull Context context, int resource, @NonNull List objects) {
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
        Bid bid =getItem(position);
        if (view  == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.bidlistviewitem, parent, false);
        }

        TextView bidder = view.findViewById(R.id.bidderTextView);
        TextView amount = view.findViewById(R.id.amountTextView);
        TextView status = view.findViewById(R.id.bidStatus);


        bidder.setText(bid.getBidder());
        status.setText(bid.getStatus());
        Double amountValue=(bid.getAmount());
        amount.setText(amountValue.toString());


        return view;
    }



}
