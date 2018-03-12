package com.example.android.funkytasks;

import android.content.ClipData;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class DashboardRequestedTask extends AppCompatActivity {

    private TextView titleValue;
    private TextView descriptionValue;
    private TextView statusValue;
    private ListView bidLV;
    private String Id;
    private Button deleteBT;
    private String username;
    private Task task;
    private int index;
    private int EDIT_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_requested_task);

        deleteBT=(Button)findViewById(R.id.deleteButton);

        // set bids listview
        bidLV=(ListView)findViewById(R.id.bidlistView);
        descriptionValue=(TextView)findViewById(R.id.textDescription);
        titleValue=(TextView) findViewById(R.id.taskName);
        statusValue = (TextView) findViewById(R.id.taskStatus);



        final Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        task = (Task)intent.getSerializableExtra("task");
        index = intent.getExtras().getInt("position");

        titleValue.setText(task.getTitle());
        descriptionValue.setText(task.getDescription());
        statusValue.setText(task.getStatus());

        //setTaskDetails();
        final ArrayAdapter bidAdapter = new ArrayAdapter<Bid>(DashboardRequestedTask.this, android.R.layout.simple_list_item_1,task.getBids());
        bidLV.setAdapter(bidAdapter);

        //
        bidLV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO display bid details;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // delete a task
        deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteTask();
                Toast.makeText(getApplicationContext(), "deleted ", Toast.LENGTH_SHORT)
                        .show();
                setResult(RESULT_OK,intent);
                finish();
                //sendToTaskDashboard(view);
            }
        });


    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editmenu, menu);
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == EDIT_CODE && resultCode == RESULT_OK) {
        }
    }

    // handle button activities
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent= getIntent();
        Id = intent.getExtras().getString("id");
        switch (item.getItemId()){
            case R.id.editButton:
                if (task.getStatus().equals("requested")){
                    Intent intent1= new Intent(this,EditDashboardRequestedTask.class);
                    intent1.putExtra("edittask",task);
                    intent1.putExtra("index",index);
                    startActivityForResult(intent1,EDIT_CODE);
                    return true;
                }
                else{
                    Toast.makeText(DashboardRequestedTask.this, "Cannot edit", Toast.LENGTH_SHORT).show();
                }
            default:return super.onOptionsItemSelected(item);

        }
    }


    public void onDeleteTask(){
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(username);

        User user;
        try {
            user = getUser.get();
            Log.e("Got the username: ", user.getUsername());

        } catch (Exception e) {
            Log.e("Error", "We arnt getting the user");
            return;
        }

        user.deleteRequestedTask(index);

        ElasticSearchController.updateUser updateUser = new ElasticSearchController.updateUser();
        updateUser.execute(user);


        // delete task in global list of all tasks
        ElasticSearchController.deleteTask deleteTask = new ElasticSearchController.deleteTask();
        deleteTask.execute(task);

        return;

    }


    public void sendToTaskDashboard(View view){
        Intent intent = new Intent(this, TaskDashboardActivity.class);
        startActivity(intent);
    }

    public void sendToEditDashboardRequestedTask(View view){
        Intent intent = new Intent(this, EditDashboardRequestedTask.class);
        //intent.putExtra("id";Id);
        //intent.putExtra("username";)
        startActivity(intent);
    }
}
