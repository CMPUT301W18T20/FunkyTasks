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
import android.widget.Toast;

public class EditDashboardRequestedTask extends AppCompatActivity {
    private String username;
    private String Id;
    private EditText editTitle;
    private EditText editDescription;
    private Button saveBT;
    private Task task;
    private int index;
    private String titleValue;
    private String descriptionValue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dashboard_requested_task);
        editTitle=(EditText) findViewById(R.id.editTitle);
        editDescription=(EditText) findViewById(R.id.editDescription);

        final Intent intent = getIntent();

        task = (Task)intent.getSerializableExtra("edittask");
        index = intent.getExtras().getInt("index");
        editTitle.setText(task.getTitle());
        editDescription.setText(task.getDescription());

        saveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                titleValue = editTitle.getText().toString();            // grab title from edit text input
                if (titleValue.length() >= 30) {                    // validating name input length
                    Toast.makeText(getApplicationContext(), "Title must be at least 30 characters long ", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                descriptionValue = editDescription.getText().toString(); // grab description from edit text input
                if (descriptionValue.length() >= 300) {               // validating name input length
                    Toast.makeText(getApplicationContext(), "Description must be at least 300 characters long ", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                ElasticSearchController.updateTask updateTask = new ElasticSearchController.updateTask();
                task.setDescription(descriptionValue);
                task.setTitle(titleValue);
                updateTask.execute(task);

                setResult(RESULT_OK,intent);
                finish();

            }
        });



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
