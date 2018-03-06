package com.example.android.funkytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class TaskDashboardActivity extends AppCompatActivity {
    ArrayList<User> userArrayList = new ArrayList<User>();
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dashboard);
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        userArrayList = ((GlobalVariables) this.getApplication()).getUserArrayList();
        User user1 = userArrayList.get(0);
        Task task = new Task("TITLE task","description",user1);
        Bid bidz = new Bid(user1,1.00);
        task.addBid(bidz);
        user1.addRequestedTask(task);
        ArrayList<Task> tasks = user1.getRequestedTasks();



        ListView dashboardView = (ListView) findViewById(R.id.myTasks);
        // get data from the table by the ListAdapter
        ListViewAdapter customAdapter = new ListViewAdapter(this, 1, tasks);
        dashboardView .setAdapter(customAdapter);



        // TODO USE RETURNED ARRAY LIST AND DISPLAY THE TASK CONTENTS IN ADAPTER
        // ****** HERE IS AN ARRAY LIST OF REQUESTED TASK RETURNED FROM USER **************
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(username);

        User user;
        final ArrayList<Task> userRequests; //TODO TAKE THIS ARRAY LIST AND DISPLAY IT IN ADAPTER
        try {
            user = getUser.get();
            Log.e("Got the username: ", user.getUsername());
            userRequests = user.getRequestedTasks(); // TODO display contents of array to listview

        } catch (Exception e) {
            Log.e("Error", "We arnt getting the user");
            return;
        }

        //************************************************************************


        Task example1;
        for (Task i: userRequests){

            example1 = i;
            break;
        }

        dashboardView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TaskDashboardActivity.this,DashboardRequestedTask.class);
                String Id= userRequests.get(i).getTitle();
                intent.putExtra("id",Id);
                startActivity(intent);
            }
        });


                // ANOTHER TEST TO MAKE SURE WE HAVE GLOBAL LIST OF TASKS
        ElasticSearchController.GetAllTask alltasks = new ElasticSearchController.GetAllTask();
        alltasks.execute("");
        ArrayList<Task> everytask;
        try{
            everytask = alltasks.get();
            for (Task i: everytask){
                Log.e("every task database",i.getTitle());
                Log.e("associated user",i.getRequester().getUsername());
            }
        }
        catch(Exception e){
            Log.e("Unable to grab","all tasks");
        }

        // ************************************************


    }
}
