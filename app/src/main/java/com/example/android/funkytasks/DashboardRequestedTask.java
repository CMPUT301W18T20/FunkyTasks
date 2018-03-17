package com.example.android.funkytasks;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
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
    //private Button deleteBT;
    private String username;
    private Task task;
    private int index;
    private int EDIT_CODE = 1;

    ListViewAdapter listViewAdapter;
    ArrayList<Bid> bidList = new ArrayList<Bid>();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_requested_task);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.DashboardRequestedTasktoolbar);
        setSupportActionBar(myToolbar);

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
        setBids();
        setAdapter();


//TODO waiting for  E.S


        //view bids

        bidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final AlertDialog.Builder Builder=new AlertDialog.Builder(DashboardRequestedTask.this);
                View View=getLayoutInflater().inflate(R.layout.bids_dialog,null);
                Builder.setView(View);
                final AlertDialog dialog=Builder.create();
                dialog.show();

                TextView bidderTextView =(TextView) View.findViewById(R.id.bidderTextView);
                TextView contactTextView =(TextView) View.findViewById(R.id.contactTextView);
                TextView amountTextView =(TextView) View.findViewById(R.id.amountTextView);
                Button acceptBTN=(Button) View.findViewById(R.id.acceptButton);
                Button declineBTN=(Button) View.findViewById(R.id.declineButton);

                //TODO get contact info and rating for user
                bidderTextView.setText(bidList.get(i).getBidder());
                Double bidAmount = bidList.get(i).getAmount();
                amountTextView.setText(bidAmount.toString());
                final int target=i;

                //accept or decline bids
                acceptBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        acceptBid(target);
                        dialog.dismiss();
                        statusValue.setText("assigned");
                        setAdapter();
                    }
                });
                declineBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        declineBids(target);
                        dialog.dismiss();
                        setAdapter();
                    }
                });


            }

        });



     /*   // delete a task
        deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteTask();
                intent.putExtra("id",id);
                setResult(RESULT_OK,intent);
                finish();
            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteActionBar:
                Intent intent = getIntent();
                onDeleteTask();
                intent.putExtra("id",id);
                setResult(RESULT_OK,intent);
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,TaskDashboardActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
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


    public void setAdapter(){
        ArrayAdapter<Bid> adpater=new ArrayAdapter<Bid>(DashboardRequestedTask.this, android.R.layout.simple_list_item_1, bidList) ;
        adpater.notifyDataSetChanged();
        bidListView.setAdapter(adpater);
    }
    public void setBids(){
        ElasticSearchController.GetBidsByTaskID getBids = new ElasticSearchController.GetBidsByTaskID();
        getBids.execute(id);
        try{
            bidList=getBids.get();
            Log.e("Got bids",bidList.toString());

        }
        catch(Exception e){
            Log.e("Bid get","not workng");
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

    public void acceptBid(int target){
        //deleting all tasks excpet acceptted one;
        Bid accepetBid=bidList.get(target);
        for (int i = 0; i < bidList.size(); i++) {
            if(!accepetBid.getId().equals(bidList.get(i).getId())){
                ElasticSearchController.deleteBid deleteAllBids=new ElasticSearchController.deleteBid();
                deleteAllBids.execute(bidList.get(i).getId());
            }
        }
        //update local bidList
        bidList.clear();
        bidList.add(accepetBid);
        //change task status to done
        task.setAssigned();
        ElasticSearchController.updateTask assigned= new ElasticSearchController.updateTask();
        assigned.execute(task);
        Toast.makeText(DashboardRequestedTask.this, "acceptted", Toast.LENGTH_SHORT).show();


    }
    public void declineBids(int target){
        ElasticSearchController.deleteBid deleteABid=new ElasticSearchController.deleteBid();
        deleteABid.execute(bidList.get(target).getId());
        bidList.remove(bidList.get(target));

    }

}
