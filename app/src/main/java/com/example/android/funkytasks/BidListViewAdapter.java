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
 * Created by jimi on 2018-03-16.
 */

public class BidListViewAdapter extends ArrayAdapter<Bid> {
    public BidListViewAdapter(@NonNull Context context, int resource, @NonNull List objects) {
       super(context, resource, objects);
    }
    @Override
    public View getView(int position, View view, ViewGroup parent){
        Bid bid =getItem(position);
        if (view  == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.bidlistviewitem, parent, false);
        }

        TextView bidder = (TextView) view.findViewById(R.id.bidderTextView);
        TextView amount = (TextView) view.findViewById(R.id.amountTextView);


        bidder.setText(bid.getBidder());

        Double amountValue=(bid.getAmount());
        amount.setText(amountValue.toString());


        return view;
    }



}
