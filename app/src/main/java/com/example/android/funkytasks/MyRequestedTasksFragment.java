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

import static android.app.Activity.RESULT_OK;


public class MyRequestedTasksFragment extends Fragment {
    ArrayList<User> userArrayList = new ArrayList<User>();
    private String username;
    CheckBox statusCheckbox;
    private int position ;
    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<Task> taskList = new ArrayList<Task>();
    ArrayList<Task> assignedTaskList = new ArrayList<Task>();
    ArrayList<Task> biddedTaskList = new ArrayList<Task>();
    final int DELETECODE = 0;
    final int Assignedcode=0;
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


        //Get tasks using E.S and display tasks

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == DELETECODE) {
            if (resultCode == RESULT_OK) {
                assignedTaskList.remove(taskList.get(position));
                biddedTaskList.remove(taskList.get(position));
                taskList.remove(position);
                setListViewAdapter(taskList);

            }
        }

    }

}

