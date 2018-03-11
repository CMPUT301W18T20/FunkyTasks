package com.example.android.funkytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class EditDashboardRequestedTask extends AppCompatActivity {
    private String username;
    private String Id;
    private EditText editTitle;
    private EditText editDescription;
    private Button saveBT;
    private Task task;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dashboard_requested_task);
        editTitle=(EditText) findViewById(R.id.editTitle);
        editDescription=(EditText) findViewById(R.id.editDescription);

        Intent intent = getIntent();

        task = (Task)intent.getSerializableExtra("edittask");
        editTitle.setText(task.getTitle());
        editDescription.setText(task.getDescription());

//        saveBT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                ElasticSearchController.updateTask updateTask = new ElasticSearchController.updateTask();
////                task.setDescription( editDescription.getText().toString());
////                task.setTitle(editTitle.getText().toString());
////                updateTask.execute(task);
//
//
//
//
//            }
//        });



    }

//    public void setTaskDetails(){
//        saveBT=(Button)findViewById(R.id.buttonDone) ;
//        Intent intent= getIntent();
//        Id = intent.getExtras().getString("id");
//
//        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();
//        getTask.execute(Id);
//
//        final Task task;
//        try{
//            task = getTask.get();
//            Log.e("Got the Title ", task.getTitle());
//        }catch (Exception e) {
//            Log.e("Error", "We arnt getting the task");
//            return;
//        }
//
//        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
//        getUser.execute(task.getRequester().getUsername());
//
//        User user;
//        try {
//            user = getUser.get();
//            Log.e("Got the username: ", user.getUsername());
//
//        } catch (Exception e) {
//            Log.e("Error", "We arnt getting the user");
//            return;
//        }
//
//
//        editTitle=(EditText) findViewById(R.id.editTitle);
//        editDescription=(EditText) findViewById(R.id.editDescription);
//
//        editTitle.setText(task.getTitle());
//        editDescription.setText(task.getDescription());
//
//        saveBT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                ElasticSearchController.updateTask updateTask = new ElasticSearchController.updateTask();
//                task.setDescription( editDescription.getText().toString());
//                task.setTitle(editTitle.getText().toString());
//                updateTask.execute(task);
//
//
//
//
//            }
//        });



 //   }

}
