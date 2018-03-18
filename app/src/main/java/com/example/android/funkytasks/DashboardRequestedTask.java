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
   // private Button editBtn;

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
       // editBtn = (Button) findViewById(R.id.editTaskButton);

        final Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        username = LoginActivity.username;
        task = (Task)intent.getSerializableExtra("task");
        index = intent.getExtras().getInt("position");
        id = intent.getExtras().getString("id");

        setTaskDetails();
        setBids();
        setAdapter();

        /*editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (task.getStatus().equals("requested")) {
                    Intent editIntent = new Intent(DashboardRequestedTask.this, EditDashboardRequestedTask.class);
                    editIntent.putExtra("username", username);
                    editIntent.putExtra("id", id);
                    startActivityForResult(editIntent, EDIT_CODE);
                }
                else{
                    Toast.makeText(DashboardRequestedTask.this, "Task cannot be edited", Toast.LENGTH_SHORT).show();
                }
            }
        });
        */

        bidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final AlertDialog.Builder Builder=new AlertDialog.Builder(DashboardRequestedTask.this);
                View View=getLayoutInflater().inflate(R.layout.bids_dialog,null);
                Builder.setView(View);
                final AlertDialog dialog=Builder.create();
                dialog.show();

                TextView bidderTextView =(TextView) View.findViewById(R.id.bidderTextView);
                TextView contactTextViewPhone =(TextView) View.findViewById(R.id.contactTextView);
                TextView contactTextViewEmail = (TextView) View.findViewById(R.id.contactTextViewEmail);
                TextView amountTextView =(TextView) View.findViewById(R.id.amountTextView);
                Button acceptBTN=(Button) View.findViewById(R.id.acceptButton);
                Button declineBTN=(Button) View.findViewById(R.id.declineButton);

                //TODO get rating for user
                String biddername = bidList.get(i).getBidder();
                ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
                getUser.execute(biddername);
                User bidder;
                try{
                    bidder = getUser.get();
                    Log.e("Success",bidder.getUsername());
                    contactTextViewPhone.setText("PHONE: "+bidder.getPhonenumber());
                    contactTextViewEmail.setText("EMAIL: "+bidder.getEmail());
                }
                catch (Exception e){
                    Log.e("Error","Unable to get the bidder's username");
                }

                bidderTextView.setText(biddername);
                Double bidAmount = bidList.get(i).getAmount();
                amountTextView.setText("$"+bidAmount.toString());


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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_requested, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteActionBar:
                if (task.getStatus().equals("requested")){
                    Intent intent = getIntent();
                    onDeleteTask();
                    intent.putExtra("id", id);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                Toast.makeText(DashboardRequestedTask.this, "Task cannot be deleted", Toast.LENGTH_SHORT).show();
                break;
            case R.id.editRequestedTask:
                if (task.getStatus().equals("requested")) {
                    Intent editIntent = new Intent(DashboardRequestedTask.this, EditDashboardRequestedTask.class);
                    editIntent.putExtra("username", username);
                    editIntent.putExtra("id", id);
                    startActivityForResult(editIntent, EDIT_CODE);
                }
                else{
                    Toast.makeText(DashboardRequestedTask.this, "Task cannot be edited", Toast.LENGTH_SHORT).show();
                }
               break;
            default:
                return super.onOptionsItemSelected(item);
        }
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


    public void setAdapter(){
        BidListViewAdapter adpater=new BidListViewAdapter(DashboardRequestedTask.this, android.R.layout.simple_list_item_1, bidList) ;
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

    public void setTaskDetails(){

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

        if (!task.getStatus().equals("requested")) {
            // to delete any bids associated with the task
            ArrayList<Bid> bids;
            ElasticSearchController.GetBidsByTaskID taskBids = new ElasticSearchController.GetBidsByTaskID();
            taskBids.execute(task.getId());
            try {
                bids = taskBids.get();
                Log.e("It works", "got list of bids");

                ElasticSearchController.deleteBid deleteBids = new ElasticSearchController.deleteBid();
                for (Bid bid : bids) {
                    deleteBids.execute(bid.getId());
                }
                Log.e("It works", "Deleted all related bids to this task");
            } catch (Exception e) {
                Log.e("Error", "With getting bids by task id");
            }
        }
        ElasticSearchController.deleteTask deleteTask = new ElasticSearchController.deleteTask();
        deleteTask.execute(id);
        Log.e("deleted","task");

    }

    public void acceptBid(int target){
        //deleting all tasks except the accepted bid
        if (task.getStatus().equals("accepted")){
            Toast.makeText(DashboardRequestedTask.this, "Task has already been assigned", Toast.LENGTH_SHORT).show();
            return;
        }
        Bid acceptedBid=bidList.get(target);
        ElasticSearchController.deleteBid deleteAllBids=new ElasticSearchController.deleteBid();
        for (int index = 0; index < bidList.size(); index++) {
            if(!acceptedBid.getId().equals(bidList.get(index).getId())){
                deleteAllBids.execute(bidList.get(index).getId());
            }
        }

        //update local bidList
        bidList.clear();
        bidList.add(acceptedBid);

        //change task status to assigned and set provider field of the task to the bidder
        task.setAssigned();
        task.setProvider(username);
        ElasticSearchController.updateTask assigned= new ElasticSearchController.updateTask();
        assigned.execute(task);
        Toast.makeText(DashboardRequestedTask.this, "Task has been assigned", Toast.LENGTH_SHORT).show();
    }
    public void declineBids(int target){
        if (task.getStatus().equals("accepted")){
            Toast.makeText(DashboardRequestedTask.this, "Task has already been assigned", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(DashboardRequestedTask.this, "Declined Bid", Toast.LENGTH_SHORT).show();

    }

}
