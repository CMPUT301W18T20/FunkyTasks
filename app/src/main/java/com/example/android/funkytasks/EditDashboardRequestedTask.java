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
    private String id;
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
        saveBT = (Button)findViewById(R.id.buttonDone);

        final Intent intent = getIntent();

        //task = (Task)intent.getSerializableExtra("edittask");
        index = intent.getExtras().getInt("index");
        id = intent.getExtras().getString("id");

        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();
        getTask.execute(id);
        try {
            task = getTask.get();
            Log.e("Got the task",task.getTitle());

        } catch (Exception e) {
            Log.e("Error", "We arnt getting the task");
            return;
        }

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
                task.setDescription(descriptionValue);
                task.setTitle(titleValue);

                ElasticSearchController.updateTask updateTask = new ElasticSearchController.updateTask();
                updateTask.execute(task);

                setResult(RESULT_OK,intent);
                intent.putExtra("id",id);
                intent.putExtra("updatedTask",task);
                finish();

            }
        });

    }
}
