/**
 * A list adapter for the list items.
 *
 * Version 1.0.0
 *
 * Created by Atharv on 2/21/2018.
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

import java.util.ArrayList;
import java.util.List;


/**
 * An adapter for the list view items to properly display the tasks
 */
public class ListViewAdapter extends ArrayAdapter<Task>{

    /**
     * This is a view adapter to properly display all the tasks
     *
     * @param context the context of the app where the bids list will be displayed
     * @param resource a context instantiation parameter needed for the super class constructor
     * @param objects a list instantiation needed for the super class constructor
     */
    public ListViewAdapter(@NonNull Context context, int resource, @NonNull List objects) {
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
            view = LayoutInflater.from(getContext()).inflate(R.layout.listviewitem, parent, false);
        }

        // Check to see if task is null
        if (task == null) {
            return view;
        }
        // Lookup view for data population
        TextView title = (TextView) view.findViewById(R.id.taskTitle);
        //TextView description = (TextView) view.findViewById(R.id.taskDescription);
        TextView status = (TextView) view.findViewById(R.id.taskStatus);
        //TextView bid = (TextView) view.findViewById(R.id.taskBid);


        title.setText(task.getTitle());
        //description.setText(task.getDescription());
        status.setText(task.getStatus());


        return view;

    }

}
