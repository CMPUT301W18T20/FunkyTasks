package com.example.android.funkytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import static com.example.android.funkytasks.MainMenuActivity.tasksArrayList;

public class EditDashboardRequestedTask extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private Button editTask;
    private ListView bidList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dashboard_requested_task);


    }
}
