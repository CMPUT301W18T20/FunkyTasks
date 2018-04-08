/**
 * ToSolveTasksFragment
 *
 * Version 1.0.0
 *
 * Create by Funky Tasks on March 8th
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * This fragment displays all the tasks a user has been accepted for and must now solve.
 */
public class ToSolveTasksFragment extends Fragment {

    ArrayList<User> userArrayList = new ArrayList<User>();
    private String username;
    private int position;
    private int taskStatusState;
    ListView listView;
    ProviderBiddedTaskAdapter adapter;
    ArrayList<Task> taskList = new ArrayList<Task>();
    ArrayList<Task> SolvedTaskList = new ArrayList<Task>();
    ArrayList<Task> SolvingTaskList = new ArrayList<Task>();
    ArrayList<Task> biddedTaskList = new ArrayList<Task>();
    final int DELETECODE = 0;
    User user;

    ArrayList<Bid> allBids;


    /**
     * Creates the view and allows the user to interact with it. This function also finds and
     * displays all the tasks that a given user has to solve and states what happens
     * when they select one of these tasks.
     *
     * @param inflater a layout inflator that helps the view display
     * @param container a view group that states where the view should be displayed
     * @param savedInstanceState a bundle representing the most recent save state of the app
     * @return returns a view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_task_dashboard, container, false);

        Intent intent = getActivity().getIntent();

        username = intent.getExtras().getString("username");
        username = LoginActivity.username;

        listView = (ListView) rootView.findViewById(R.id.myTasks);
        Spinner dropdown = rootView.findViewById(R.id.yourPostMenu);
        String[] menuOptions = new String[]{"MY ASSOCIATED TASKS", "BIDDED ON", "SOLVING", "SOLVED"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, menuOptions);
        dropdown.setAdapter(arrayAdapter);


        //Get tasks using Elastic search and display tasks

        getTask();
        setListViewAdapter(taskList);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    setListViewAdapter(taskList);
                    taskStatusState=0;
                }
                if (i == 1) {
                    setListViewAdapter(biddedTaskList);
                    taskStatusState=1;

                }
                if (i == 2) {
                    setListViewAdapter(SolvingTaskList);
                    taskStatusState=2;
                }
                if (i == 3) {
                    setListViewAdapter(SolvedTaskList);
                    taskStatusState=3;

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
                position = i;
                taskOnClick(i);
            }
        });
        return rootView;

    }


    /**
     * Sets the list view adapter to the proper adapter so that the objects can be displayed
     * properly
     *
     * @param tasklist an array list holding all the tasks
     */
    public void setListViewAdapter(ArrayList<Task> tasklist) {
        ProviderBiddedTaskAdapter adapter= new ProviderBiddedTaskAdapter(getActivity(), R.layout.listviewitem, tasklist);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

    }

    /**
     * Adds the tasks that the user has bid on the the list of tasks so they can be accessed
     * by the rest of the class
     */
    public void getTask() {

        ElasticSearchController.GetBidsByBidder getallBids = new ElasticSearchController.GetBidsByBidder();
        getallBids.execute(username);
        try {
            allBids = getallBids.get();
            Log.e("Got the related tasks: ", getallBids.get().get(0).getTaskID().toString());

        } catch (Exception e) {
            Log.e("Error", "We arnt getting all the bids");
            return;
        }

        new Thread(new Runnable() {
            public void run() {
                // Getting the all the tasks associated with the user
                int size = allBids.size();


                for (int index = 0; index < size; index++) {
                    Task atask;
                    ElasticSearchController.GetTask getTask=new ElasticSearchController.GetTask();
                    getTask.execute(allBids.get(index).getTaskID());
                    Log.e("Task id", allBids.get(index).getTaskID());
                    try {
                        atask = getTask.get();
                        Log.e("Return task title", atask.getTitle());
                    } catch (Exception e) {
                        Log.e("Task get", "not workng");
                        return;
                    }
                    taskList.add(atask);
                    Log.e("Success", "loop");

                }
                for(Task task: taskList){
                    if(task.getStatus().equals("bidded")){
                        Log.e("bidded task title",task.getTitle());
                        biddedTaskList.add(task);
                    }
                    if(task.getStatus().equals("assigned")){
                        Log.e("solving task title",task.getTitle());
                        SolvingTaskList.add(task);
                    }
                    if(task.getStatus().equals("done")){
                        Log.e("solved task title",task.getTitle());
                        SolvedTaskList.add(task);
                    }
                }
            }
        }).start();




    }

    /**
     * States what happens when an item in the list is clicked on. The tasks details are loaded
     * and a new intent is started.
     *
     * @param i an integer representing the index of the item in the list
     */
    public void taskOnClick(int i) {
        Intent intent = new Intent(getActivity(), DashboardProviderTask.class);
        intent.putExtra("username", username);
        Task detailedTask;


        detailedTask = taskList.get(i);

        if(taskStatusState==1){
            detailedTask = biddedTaskList.get(i);
        }
        if(taskStatusState==2){
            detailedTask = SolvingTaskList.get(i);
        }
        if(taskStatusState==3){
            detailedTask = SolvedTaskList.get(i);
        }

        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();
        getTask.execute(detailedTask.getId());
        try {
            Task x = getTask.get();
            Log.e("Return task title", x.getTitle());
        } catch (Exception e) {
            Log.e("Error", "Task get not working");
        }
        //intent.putExtra("task", detailedTask);
        intent.putExtra("position", i);
        intent.putExtra("id", detailedTask.getId());
        startActivityForResult(intent,DELETECODE);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (requestCode == DELETECODE) {
//            if (resultCode == RESULT_OK) {
//                assignedTaskList.remove(taskList.get(position));
//                biddedTaskList.remove(taskList.get(position));
//                taskList.remove(position);
//                setListViewAdapter(taskList);
//
//            }
//            else if (resultCode == RESULT_CANCELED){
//                getTask();
//                setListViewAdapter(taskList);
//            }
//        }
//
//    }
//
}
