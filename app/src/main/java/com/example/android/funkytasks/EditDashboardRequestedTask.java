package com.example.android.funkytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class EditDashboardRequestedTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dashboard_requested_task);

        TextView title = (TextView) findViewById(R.id.Title);
        TextView description = (TextView) findViewById(R.id.Description);
        EditText newTitle = (EditText) findViewById(R.id.editTitle);
        EditText newDescription = (EditText) findViewById(R.id.editDescription);
        Button done = (Button) findViewById(R.id.buttonDone);
        Button location = (Button) findViewById(R.id.buttonLocation);

    }


}
