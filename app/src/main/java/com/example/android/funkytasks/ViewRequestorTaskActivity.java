package com.example.android.funkytasks;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewRequestorTaskActivity extends AppCompatActivity {

    private TextView titleValue;
    private TextView descriptionValue;
    private TextView statusValue;
    private TextView usernameValue;
    private String id;
    private String username;
    private Task task;
    private int index;


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requestor_task);


        descriptionValue = (TextView) findViewById(R.id.requestorTaskDescription);
        titleValue = (TextView) findViewById(R.id.requestorTaskName);
        statusValue = (TextView) findViewById(R.id.requestorTaskStatus);
        usernameValue = (TextView) findViewById(R.id.requestorTaskUsername);

        final Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        username = LoginActivity.username;

        id = intent.getExtras().getString("id");

        ElasticSearchController.GetTask getTask = new ElasticSearchController.GetTask();

        getTask.execute(id);
        try {
            task = getTask.get();
            Log.e("Return task title", task.getTitle());
        } catch (Exception e) {
            Log.e("Task get", "not workng");
        }

        //TODO SET REQUESTER'S CONTACT INFO TO BE SHOWN ON SCREEN WITH PHONE AND EMAIL (use case 3.3)

        titleValue.setText(task.getTitle());
        descriptionValue.setText(task.getDescription());
        statusValue.setText(task.getStatus());
        usernameValue.setText(task.getRequester());


        Button bidButton = (Button) findViewById(R.id.bidButton);

        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If user has never placed a bid on the task yet:

                // DialogFragment placeBidFragment = new PlaceBidDialogFragment();
                //placeBidFragment.show(getSupportFragmentManager(), "Bids");

                // Else if the user has placed a bid and wants to update it:

                DialogFragment updateBidFragment = new UpdateBidDialogFragment();
                updateBidFragment.show(getSupportFragmentManager(), "Bids");



                //Integer numBids = task.getNumberOfBids();
                //numBids += 1;
                //task.setNumberOfBids(numBids);

                //Log.e("Current number of bids ", numBids.toString());

                //Intent intent = new Intent(ViewRequestorTaskActivity.this, MainMenuActivity.class);
                //startActivity(intent);

            }
        });

    }

    public static class PlaceBidDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater

            builder.setTitle("Place Bid");
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_placebid, null))
                    // Add action buttons
                    .setPositiveButton("Place Bid", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // Place Bid (Elastic Search)
                            // set Task status to bidded
                            // Go back to Search Results


                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            PlaceBidDialogFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }

    }

    public static class UpdateBidDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater

            builder.setTitle("Update Bid");
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_updatebid, null))
                    // Add action buttons
                    .setPositiveButton("Update Bid", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            // Update Bid (Elastic Search)
                            // Go back to Search Results

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            UpdateBidDialogFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }

    }
}
