/**
 * TaskDashboardActivity
 *
 * Version 1.0.0
 *
 * Create by Funky Tasks on March 8th
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * This activity is not currently being used, but it may be used in the future.
 */
public class TaskDashboardActivity extends BaseActivity {
    ArrayList<User> userArrayList = new ArrayList<User>();
    private String username;
    CheckBox statusCheckbox;
    private int position ;
    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<Task> taskList = new ArrayList<Task>();
    ArrayList<Task> biddedTaskList = new ArrayList<Task>();
    final int DELETECODE = 0;
    ArrayList<Task> requestedTasks;
    User user;

    /**
     * Overrides the onCreate super class and instantiates the proper view for this class
     *
     * @param savedInstanceState a bundle of the previous saved instance state that is used to
     *                           load a snapshot of the app in the state it was last in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dashboard);
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        username = LoginActivity.username;
//        statusCheckbox=(CheckBox) findViewById(R.id.checkBox);
        listView = findViewById(R.id.myTasks);

        //Get tasks using E.S and display tassks
        getTask();
        setListViewAdapter(taskList);

       //show bided task
//        statusCheckbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showBided();
//            }
//        });


        //ListView item on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position=i;
                taskOnClick(i);
            }
        });

    }

    /**
     * checks which screen we go back to for the back button
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /**
     * sets the list view adapter
     * @param tasklist a list containing the tasks
     */
    public void setListViewAdapter(ArrayList<Task> tasklist){
        listViewAdapter = new ListViewAdapter(this, R.layout.listviewitem, tasklist);
        listViewAdapter.notifyDataSetChanged();
        listView.setAdapter(listViewAdapter);

    }

    /**
     * Sets the list by grabbing all the tasks associated with the user
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
            if(taskList.get(i).getStatus()=="bidded"){
                biddedTaskList.add(taskList.get(i));
            }
        }


    }

    /**
     * if we click on the task, grab its details
     * @param i position we clicked on the task
     */

    public void taskOnClick(int i){
        Intent intent = new Intent(TaskDashboardActivity.this, DashboardRequestedTask.class);
        intent.putExtra("username", username);
        Task detailedTask;

        if(statusCheckbox.isChecked()){
            detailedTask = biddedTaskList.get(i);
        }
        else{
            detailedTask = taskList.get(i);
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
        intent.putExtra("position", i);
        intent.putExtra("id", detailedTask.getId());
        startActivityForResult(intent,DELETECODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == DELETECODE) {
            if (resultCode == RESULT_OK) {
                biddedTaskList.remove(taskList.get(position));
                taskList.remove(position);
                setListViewAdapter(taskList);

            }
        }
    }

}
