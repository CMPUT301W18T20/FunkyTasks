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

        title.setText(task.getTitle());
        //description.setText(task.getDescription());
        status.setText(task.getStatus());
        username.setText(task.getRequester());


        return view;

    }

}
