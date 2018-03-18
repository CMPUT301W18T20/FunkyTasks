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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_task_dashboard, container, false);

        Intent intent = getActivity().getIntent();

        username = intent.getExtras().getString("username");
        username = LoginActivity.username;

        listView = (ListView) rootView.findViewById(R.id.myTasks);
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


    public void setListViewAdapter(ArrayList<Task> tasklist){
        listViewAdapter = new ListViewAdapter(getActivity(), R.layout.listviewitem, tasklist);
        listViewAdapter.notifyDataSetChanged();
        listView.setAdapter(listViewAdapter);

    }

    public void getTask(){
        ArrayList<Bid> allBids;


        ElasticSearchController.GetBidsByBidder getallBids=new ElasticSearchController.GetBidsByBidder();
        getallBids.execute(username);
        try {
            allBids = getallBids.get();
            Log.e("Got the task: ", getallBids.get().get(0).getTaskID().toString());

        } catch (Exception e) {
            Log.e("Error", "We arnt getting all the bids");
            return;
        }


        // Getting the all the tasks associated with the user
        int size=allBids.size();
        for(int index=0;index<size;index++){
            Task task ;
            ElasticSearchController.GetTask getTask=new ElasticSearchController.GetTask();
            getTask.execute(allBids.get(index).getTaskID());
            Log.e("Task id",allBids.get(index).getTaskID());
            try{
                task = getTask.get();
                Log.e("Return task title",task.getTitle());}
            catch(Exception e){
                Log.e("Task get","not workng");
                return;}
            taskList.add(task);
            Log.e("Success","loop");

        }

    }

    public void taskOnClick(int i){
        Intent intent = new Intent(getActivity(), DashboardProviderTask.class);
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
