package com.example.android.funkytasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class SolveTaskActivity extends AppCompatActivity {
    private String searchText;
    private String username;

    ListViewAdapter listViewAdapter;
    ArrayList<Task> taskList = new ArrayList<Task>();

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_task);

        setTitle("Solve a Task");

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        final EditText search = (EditText) findViewById(R.id.search);

        Button searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchText = search.getText().toString();
                Log.e("Specified keywords", searchText);
                Log.e("Username", username);

                ElasticSearchController.searchTask searchTask = new ElasticSearchController.searchTask();
                searchTask.execute(searchText, username);


                try{
                    taskList = searchTask.get();
                    //@TODO TEST IF THERES ANYTHING IN Tasks
                }
                catch(Exception e){
                    Log.e("Error", "Failed to get tasks");
                    return;
                }

                ListView taskView = (ListView) findViewById(R.id.taskView);
                listViewAdapter = new ListViewAdapter(SolveTaskActivity.this, R.layout.listviewitem, taskList);

                taskView.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();

                taskView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(SolveTaskActivity.this,ViewRequestorTaskActivity.class);
                        intent.putExtra("username",username);
                        Task detailedTask = taskList.get(i);

                        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();

                        getTask.execute(detailedTask.getId());
                        try{
                            Task x = getTask.get();
                            Log.e("Return task title",x.getTitle());
                        }
                        catch(Exception e){
                            Log.e("Task get","not workng");
                        }
                        intent.putExtra("task",detailedTask);
                        intent.putExtra("position",i);
                        intent.putExtra("id",detailedTask.getId());
                        startActivity(intent);
                    }
                });

            }
        });
    }
}

