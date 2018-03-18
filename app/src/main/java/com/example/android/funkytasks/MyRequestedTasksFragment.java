/**
 * This is a fragment that loads the requested tasks for a particular view.
 *
 * Version 1.0.0
 *
 * Create by Funky Tasks on March 8th
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;


import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * This fragment displays the tasks one particular user has requested.
 */
public class MyRequestedTasksFragment extends Fragment {
    ArrayList<User> userArrayList = new ArrayList<User>();
    private String username;
    private int position ;
    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<Task> taskList = new ArrayList<Task>();
    ArrayList<Task> assignedTaskList = new ArrayList<Task>();
    ArrayList<Task> biddedTaskList = new ArrayList<Task>();
    final int DELETECODE = 0;
    ArrayList<Task> requestedTasks;
    User user;

    /**
     * This function overrides the default onCreateView function and dictates what happens
     * when this view is instantiated
     * @param inflater a layout inflater that helps the fragment display properly
     * @param container a view group representing where the fragment should be displayed
     * @param savedInstanceState a bundle representing the state of the view last time it was open
     * @return returns a view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_task_dashboard, container, false);

        Intent intent = getActivity().getIntent();

        username = intent.getExtras().getString("username");
        username = LoginActivity.username;

        listView = rootView.findViewById(R.id.myTasks);
        Spinner dropdown = rootView.findViewById(R.id.yourPostMenu);
        String[] menuOptions = new String[]{"My Tasks","Bidded", "Assigned"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,menuOptions);
        dropdown.setAdapter(arrayAdapter);


        //Get tasks using Elastic search and display tasks

        getTask();
        setListViewAdapter(taskList);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    setListViewAdapter(taskList);
                }
                if(i==1){
                    setListViewAdapter(biddedTaskList);
                }
                if(i==2){
                    setListViewAdapter(assignedTaskList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //ListView item on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position=i;
                taskOnClick(i);
            }
        });
        return rootView;

    }

    /**
     * Sets the proper list view adapter for the list of tasks to be displayed.
     *
     * @param tasklist an array list of task to be treated by the list view adapter
     */
    public void setListViewAdapter(ArrayList<Task> tasklist){
        listViewAdapter = new ListViewAdapter(getActivity(), R.layout.listviewitem, tasklist);
        listViewAdapter.notifyDataSetChanged();
        listView.setAdapter(listViewAdapter);

    }

    /**
     * This function gets all the tasks associated with one username
     */
    public void getTask(){
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(username);
        try {
            user = getUser.get();
            Log.e("Got the username: ", user.getUsername());

        } catch (Exception e) {
            Log.e("Error", "We arnt getting the user");
            return;

        }

        // Getting the all the tasks associated with the user
        ElasticSearchController.GetAllTask getAllTask = new ElasticSearchController.GetAllTask();
        getAllTask.execute(username);
        try {
            taskList = getAllTask.get();
            Log.e("Got the tasks ", taskList.toString());

        } catch (Exception e) {
            Log.e("Error", "We arnt getting the list of tasks");
            return;

        }

        int size=taskList.size();
        for(int i=0;i<size;i++){
            if(taskList.get(i).getStatus().equals("bidded")){
                biddedTaskList.add(taskList.get(i));
            }
        }


        for(int i=0;i<size;i++){
            if(taskList.get(i).getStatus().equals("assigned")){
                assignedTaskList.add(taskList.get(i));
            }
        }

    }

    /**
     * This function states what happens when a task is clicked on
     *
     * @param i an integer representing the index of the item that was cicked on
     */
    public void taskOnClick(int i){
        Intent intent = new Intent(getActivity(), DashboardRequestedTask.class);
        intent.putExtra("username", username);
        Task detailedTask;

        detailedTask = taskList.get(i);
        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();
        getTask.execute(detailedTask.getId());
        try {
            Task x = getTask.get();
            Log.e("Return task title", x.getTitle());
        } catch (Exception e) {
            Log.e("Error", "Task get not working");
        }
        intent.putExtra("task", detailedTask);
        intent.putExtra("position", i);
        intent.putExtra("id", detailedTask.getId());
        startActivityForResult(intent,DELETECODE);

    }

    /**
     * States what happens when the activity runs
     *
     * @param requestCode an integer representing what action the activity should perform
     * @param resultCode an integer returned by the activity stating whether or not the
     *                   activity was carried out successfully
     * @param intent an intent that states the environment in which the activity is carried out
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == DELETECODE) {
            if (resultCode == RESULT_OK) {
                assignedTaskList.remove(taskList.get(position));
                biddedTaskList.remove(taskList.get(position));
                taskList.remove(position);
                setListViewAdapter(taskList);

            }
            else if (resultCode == RESULT_CANCELED){
                getTask();
                setListViewAdapter(taskList);
            }
        }

    }

}

