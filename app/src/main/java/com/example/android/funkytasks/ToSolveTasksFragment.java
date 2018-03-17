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


public class ToSolveTasksFragment extends Fragment {

    private String username;
    private int position;
    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<Task> doneTaskList = new ArrayList<Task>();
    ArrayList<Task> acceptedTaskList = new ArrayList<Task>();
    ArrayList<Task> biddedTaskList = new ArrayList<Task>();
    ArrayList<Bid> allBids = new ArrayList<Bid>();
    final int DETAILCODE = 0;

    User user;

    private int whichList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_task_dashboard, container, false);

        Intent intent = getActivity().getIntent();

        username = intent.getExtras().getString("username");
        username = LoginActivity.username;

        listView = (ListView) rootView.findViewById(R.id.myTasks);
        Spinner dropdown = rootView.findViewById(R.id.yourPostMenu);
        String[] menuOptions = new String[]{"Bidded for","Solving", "Solved"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,menuOptions);
        dropdown.setAdapter(arrayAdapter);


        //Get tasks using Elastic search and display tasks

        getTask();
        setListViewAdapter(biddedTaskList);
        whichList = 0;

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    setListViewAdapter(biddedTaskList);
                    whichList = 0;
                }
                if(i==1){
                    setListViewAdapter(acceptedTaskList);
                    whichList = 1;
                }
                if(i==2){
                    setListViewAdapter(doneTaskList);
                    whichList = 2;
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

    public void setListViewAdapter(ArrayList<Task> tasklist){
        listViewAdapter = new ListViewAdapter(getActivity(), R.layout.listviewitem, tasklist);
        listViewAdapter.notifyDataSetChanged();
        listView.setAdapter(listViewAdapter);
    }

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

        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();

        // get all the tasks that the task provider has bidded on
        // first get all the bids, then grab each task ID to get the task
        ElasticSearchController.GetBidsByBidder getBids = new ElasticSearchController.GetBidsByBidder();
        getBids.execute(username);
        try{
            allBids = getBids.get();
            for (Bid bid: allBids){
                getTask.execute(bid.getTaskID()); // get the task associated with the bidder's username;
                try{
                    biddedTaskList.add(getTask.get());
                }
                catch(Exception e){
                    Log.e("Error","unable to get the bidded task");
                }

            }
        }
        catch(Exception e){
            Log.e("Error","No bids with the user");
        }


        ElasticSearchController.GetAllProviderTask getAllProviderTask = new ElasticSearchController.GetAllProviderTask();
        getAllProviderTask.execute(username);
        try{
            acceptedTaskList = getAllProviderTask.get();
        }
        catch(Exception e){
            Log.e("Error","unable to get the bidded task");
        }

        if (acceptedTaskList != null || acceptedTaskList.size() >= 0){
            int size = acceptedTaskList.size();
            for(int i = 0 ; i < size ; i++){
                if(acceptedTaskList.get(i).getStatus().equals("done")){
                    acceptedTaskList.remove(i);
                    doneTaskList.add(acceptedTaskList.get(i));
                }
            }
        }

    }

    public void taskOnClick(int position){
        Intent intent = new Intent(getActivity(), DashboardProviderTask.class);
        intent.putExtra("username", username);
        Task detailedTask;

        // get the task depending on which list the user clicked on
        if (whichList == 0){
            detailedTask = biddedTaskList.get(position);
        }
        else if (whichList == 1){
            detailedTask = acceptedTaskList.get(position);
        }
        else{
            detailedTask = doneTaskList.get(position);
        }

        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();
        getTask.execute(detailedTask.getId());
        try {
            Task x = getTask.get();
            Log.e("Return task title", x.getTitle());
        } catch (Exception e) {
            Log.e("Error", "Task get not working");
        }
        intent.putExtra("task", detailedTask);
        intent.putExtra("position", position);
        intent.putExtra("id", detailedTask.getId());
        startActivityForResult(intent,DETAILCODE);

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


}
