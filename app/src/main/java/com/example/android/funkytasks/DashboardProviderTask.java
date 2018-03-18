package com.example.android.funkytasks;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.android.funkytasks.SearchListViewAdapter.getLowestBid;


public class DashboardProviderTask extends AppCompatActivity {

    private TextView titleValue;
    private TextView descriptionValue;
    private TextView lowestBidValue;
    private TextView myBidValue;

    private TextView statusValue;
    private String id;
    private Button multiFunctionButton;

    private String username;
    private Task task;
    private int index;
    private int EDIT_CODE = 1;

    ListViewAdapter listViewAdapter;
    ArrayList<Bid> bidList = new ArrayList<Bid>();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_provider_task);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.DashboardProviderTasktoolbar);
        //setSupportActionBar(myToolbar);

        // set bids listview

        descriptionValue=(TextView)findViewById(R.id.textDescriptionprovider);
        titleValue=(TextView) findViewById(R.id.taskNamerequester);
        statusValue = (TextView) findViewById(R.id.taskStatustext);
        multiFunctionButton=(Button) findViewById(R.id.multiFunction);
        lowestBidValue = (TextView) findViewById(R.id.lowestBidAmount);
        myBidValue = (TextView) findViewById(R.id.myBidAmount);

        final Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        username = LoginActivity.username;
        task = (Task)intent.getSerializableExtra("task");
        index = intent.getExtras().getInt("position");
        id = intent.getExtras().getString("id");

        //change button function
//         TODO implement each button function
        if(task.getStatus().equals("bidded")){
            Log.e("Provider task status",task.getStatus());
            multiFunctionButton.setText("UPDATE BID");
        }

        if(task.getStatus().equals("assigned")){
            Log.e("Provider task status",task.getStatus());
            multiFunctionButton.setText("UPDATE STATUS");
        }
        if(task.getStatus().equals("done")){

        }

        setTaskDetails();


//        bidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                final AlertDialog.Builder Builder=new AlertDialog.Builder(DashboardProviderTask.this);
//                View View=getLayoutInflater().inflate(R.layout.bids_dialog,null);
//                Builder.setView(View);
//                final AlertDialog dialog=Builder.create();
//                dialog.show();
//
//                TextView bidderTextView =(TextView) View.findViewById(R.id.bidderTextView);
//                TextView contactTextViewPhone =(TextView) View.findViewById(R.id.contactTextView);
//                TextView contactTextViewEmail = (TextView) View.findViewById(R.id.contactTextViewEmail);
//                TextView amountTextView =(TextView) View.findViewById(R.id.amountTextView);
//                Button acceptBTN=(Button) View.findViewById(R.id.acceptButton);
//                Button declineBTN=(Button) View.findViewById(R.id.declineButton);
//
//                //TODO get rating for user
//                String biddername = bidList.get(i).getBidder();
//                ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
//                getUser.execute(biddername);
//                User bidder;
//                try{
//                    bidder = getUser.get();
//                    Log.e("Success",bidder.getUsername());
//                    contactTextViewPhone.setText("PHONE: "+bidder.getPhonenumber());
//                    contactTextViewEmail.setText("EMAIL: "+bidder.getEmail());
//                }
//                catch (Exception e){
//                    Log.e("Error","Unable to get the bidder's username");
//                }
//
//                bidderTextView.setText(biddername);
//                Double bidAmount = bidList.get(i).getAmount();
//                amountTextView.setText("$"+bidAmount.toString());
//
//
//                final int target=i;
//                //accept or decline bids
//                acceptBTN.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        acceptBid(target);
//                        dialog.dismiss();
//                        statusValue.setText("assigned");
//                        setAdapter();
//                    }
//                });
//                declineBTN.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        declineBids(target);
//                        dialog.dismiss();
//                        setAdapter();
//                    }
//                });
//
//            }
//
//        });

    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteActionBar:

            default:
                return super.onOptionsItemSelected(item);
        }
    }

*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == EDIT_CODE && resultCode == RESULT_OK) {
            task = (Task) intent.getSerializableExtra("updatedTask");
            titleValue.setText(task.getTitle());
            descriptionValue.setText(task.getDescription());

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
        ArrayList<Bid> bids = new ArrayList<Bid>();
        ElasticSearchController.GetBidsByTaskID getBidsByTaskID = new ElasticSearchController.GetBidsByTaskID();
        getBidsByTaskID.execute(task.getId());
        try{
            bids = getBidsByTaskID.get();
        }
        catch(Exception e){
            Log.e("Error","Something wrong with getting bids in adapter");
        }
        Double lowest = getLowestBid(bids).getAmount();
        lowestBidValue.setText(lowest.toString());
        for (Bid bid: bids){
            if (bid.getBidder().equals(username)){
                myBidValue.setText(String.valueOf(bid.getAmount()));
                break;
            }
        }

    }



    public void acceptBid(int target){
        //deleting all tasks except the accepted bid
        if (task.getStatus().equals("accepted")){
            Toast.makeText(DashboardProviderTask.this, "Task has already been assigned", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(DashboardProviderTask.this, "Task has been assigned", Toast.LENGTH_SHORT).show();
    }
    public void declineBids(int target){
        if (task.getStatus().equals("accepted")){
            Toast.makeText(DashboardProviderTask.this, "Task has already been assigned", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(DashboardProviderTask.this, "Declined Bid", Toast.LENGTH_SHORT).show();

    }

}
