package com.example.android.funkytasks;

import android.app.AlertDialog;
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
    private ListView bidListView;
    private String id;
    private Button deleteBT;
    private String username;
    private Task task;
    private int index;
    private int EDIT_CODE = 1;

    ListViewAdapter listViewAdapter;
    ArrayList<Bid> bidList = new ArrayList<Bid>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_requested_task);

        deleteBT=(Button)findViewById(R.id.deleteButton);

        // set bids listview
        bidListView=(ListView)findViewById(R.id.bidlistView);
        descriptionValue=(TextView)findViewById(R.id.textDescription);
        titleValue=(TextView) findViewById(R.id.taskName);
        statusValue = (TextView) findViewById(R.id.taskStatus);

        final Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        username = LoginActivity.username;
        task = (Task)intent.getSerializableExtra("task");
        index = intent.getExtras().getInt("position");
        id = intent.getExtras().getString("id");

        setTaskdetails();


//TODO waiting for  E.S
//        ElasticSearchController.GetBids getBids = new ElasticSearchController.GetBids();
//
//        getBids.execute(id);
//        try{
//            bidList=getBids.get();
//            Log.e("Got bids",bidList.toString());
//
//        }
//        catch(Exception e){
//            Log.e("Bid get","not workng");
//        }
//
//
//        listViewAdapter = new ListViewAdapter(this, R.layout.listviewitem, bidList);
//        bidListView.setAdapter(listViewAdapter);
//        listViewAdapter.notifyDataSetChanged();

        //view bids

//        bidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                AlertDialog.Builder Builder=new AlertDialog.Builder(DashboardRequestedTask.this);
//                View View=getLayoutInflater().inflate(R.layout.bids_dialog,null);
//                TextView bidderTextView =(TextView) View.findViewById(R.id.bidderTextView);
//                TextView contactTextView =(TextView) View.findViewById(R.id.contactTextView);
//                TextView amountTextView =(TextView) View.findViewById(R.id.amountTextView);
//                Button acceptBTN=(Button) View.findViewById(R.id.acceptButton);
//                Button declineBTN=(Button) View.findViewById(R.id.declineButton);
//
//                //TODO get contact info and rating for user
//                bidderTextView.setText(bidList.get(i).getBidder());
//                Double bidAmount = bidList.get(i).getAmount();
//                amountTextView.setText(bidAmount.toString());
//
//                //accept or decline bids
//                acceptBTN.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        acceptBid();
//                    }
//                });
//                declineBTN.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        declineBids();
//                    }
//                });
//                Builder.setView(View);
//                AlertDialog dialog=Builder.create();
//                dialog.show();
//
//            }
//
//        });



        // delete a task
        deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteTask();
                //Intent intent = new Intent(DashboardRequestedTask.this,TaskDashboardActivity.class);
                intent.putExtra("id",id);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,TaskDashboardActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
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
            task = (Task) intent.getSerializableExtra("updatedTask");

            titleValue.setText(task.getTitle());
            descriptionValue.setText(task.getDescription());

        }
    }

    // handle button activities
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.editButton:
                if (task.getStatus().equals("requested")){
                    Intent intent1= new Intent(this,EditDashboardRequestedTask.class);
                    intent1.putExtra("edittask",task);
                    intent1.putExtra("index",index);
                    intent1.putExtra("id",id);
                    startActivityForResult(intent1,EDIT_CODE);
                    return true;
                }
                else{
                    Toast.makeText(DashboardRequestedTask.this, "Cannot edit", Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setTaskdetails(){

        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();

        getTask.execute(id);
        try{
            task = getTask.get();
            Log.e("Return task title",task.getTitle());

        }
        catch(Exception e){
            Log.e("Task get","not workng");
        }

        titleValue.setText(task.getTitle());
        descriptionValue.setText(task.getDescription());
        statusValue.setText(task.getStatus());

    }

    public void onDeleteTask(){
        // delete task in global list of all tasks
        ElasticSearchController.deleteTask deleteTask = new ElasticSearchController.deleteTask();
        deleteTask.execute(id);
        Log.e("deleted","task");

    }

    public void acceptBid(){

    }
    public void declineBids(){

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
