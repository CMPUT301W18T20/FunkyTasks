package com.example.android.funkytasks;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import static com.example.android.funkytasks.MainMenuActivity.tasksArrayList;


public class DashboardRequestedTask extends AppCompatActivity {

    private TextView titleValue;
    private TextView descriptionValue;
    private Button edit;
    private ListView bids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_requested_task);


        Bundle extras = getIntent().getExtras();
        final int ID= extras.getInt("id");

        //Set bidList inside lisetview


    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.editButton) {
            Button edit = (Button) findViewById(R.id.editButton);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendToEditDashboardRequestedTask(view);
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    //set title and description
    public void setTaskDetails(TextView titleValue,int ID) {

        descriptionValue=(TextView)findViewById(R.id.textDescription);
        titleValue=(TextView) findViewById(R.id.taskName);
        titleValue.setText(tasksArrayList.get(ID).getTitle());
        descriptionValue.setText(tasksArrayList.get(ID).getDescription());

    }


    //decline bids
    public void declineBids(ListView bids,int ID){
        bids = (ListView) findViewById(R.id.listView);

        ArrayList<bid> bidList= tasksArrayList.get(ID).getBids();
        ArrayAdapter<bid> adapter= new ArrayAdapter<bid>(DashboardRequestedTask.this,android.R.layout.simple_list_item_multiple_choice,bidList);

        bids.setAdapter(adapter);
        bids.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        bids.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

        SparseBooleanArray checked=bids.getCheckedItemPositions();
        int size=bids.getCount();

        for(int i = size-1;i>-1;i--){
            if (bids.isItemChecked(i)){
                sublist.remove(i);
            }
        }
        checked.clear();
        oldadapter.notifyDataSetChanged();
        if(size==0) {
            Toast.makeText(MainActivity.this, "List is empty", Toast.LENGTH_SHORT).show();
        }


    }


    public void sendToEditDashboardRequestedTask(View view){
        Intent intent = new Intent(this, EditDashboardRequestedTask.class);
        startActivityForResult(intent, 42);
    }
}
