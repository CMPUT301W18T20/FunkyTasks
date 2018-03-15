package com.example.android.funkytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewRequestorTaskActivity extends AppCompatActivity {

    private TextView titleValue;
    private TextView descriptionValue;
    private TextView statusValue;
    private TextView usernameValue;
    private String id;
    private String username;
    private Task task;
    private int index;


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requestor_task);


        descriptionValue = (TextView) findViewById(R.id.requestorTaskDescription);
        titleValue = (TextView) findViewById(R.id.requestorTaskName);
        statusValue = (TextView) findViewById(R.id.requestorTaskStatus);
        usernameValue = (TextView) findViewById(R.id.requestorTaskUsername);

        final Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        username = LoginActivity.username;

        id = intent.getExtras().getString("id");

        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();

        getTask.execute(id);
        try {
            task = getTask.get();
            Log.e("Return task title", task.getTitle());
        } catch (Exception e) {
            Log.e("Task get", "not workng");
        }

        //TODO SET REQUESTER'S CONTACT INFO TO BE SHOWN ON SCREEN WITH PHONE AND EMAIL (use case 3.3)

        titleValue.setText(task.getTitle());
        descriptionValue.setText(task.getDescription());
        statusValue.setText(task.getStatus());
        usernameValue.setText(task.getRequester());


        Button bidButton = (Button) findViewById(R.id.bidButton);

        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer numBids = task.getNumberOfBids();
                numBids += 1;
                task.setNumberOfBids(numBids);

                Log.e("Current number of bids ", numBids.toString());

                Intent intent = new Intent(ViewRequestorTaskActivity.this, MainMenuActivity.class);
                startActivity(intent);

            }
        });

    }

    //public void sendToSolveTaskActivity(View view){
    //Intent intent = new Intent(this, SolveTaskActivity.class);
    //startActivity(intent);
    //}
}
