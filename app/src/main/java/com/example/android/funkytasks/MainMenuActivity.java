/**
 * MainMenuActivity
 *
 * Version 1.0.0
 *
 * Create by Funky Tasks on March 8th
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */


package com.example.android.funkytasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**
 * This is the main screen for the project
 */
public class MainMenuActivity extends AppCompatActivity {
    public static ArrayList<Task> tasksArrayList = new ArrayList<Task>();
    ArrayList<User> userArrayList = new ArrayList<User>();
    public static String username;
    final int ADD_CODE = 1;
    final int EDIT_CODE = 2;
    ArrayList<String> taskIds = new ArrayList<>();


    /**
     * Overrides the default onCreate function and prepares the view for the user
     *
     * @param savedInstanceState a bundle representing the state of the app the
     *                           last time it was open
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Main Menu");
        Intent intent = getIntent();

        //username = intent.getExtras().getString("username");
        username = LoginActivity.username;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToDashboard(view);
            }
        });



    }

    /**
     * States what should happen when an options menu is created
     *
     * @param menu a menu object representing the menu to be created
     * @return returns a boolean stating whether it was successful or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * States what happens when an item in the menu is selected
     *
     * @param item a menu item that states which item was selected
     * @return returns a boolean stating whether the activity was successful or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileActionBar:
                Intent intent = new Intent(MainMenuActivity.this, EditProfileActivity.class);
                intent.putExtra("username", username);
                startActivityForResult(intent, EDIT_CODE);
                return true;

            /* The following code is used for the notifications pop-up */
            case R.id.item_notification:
                Button notis = findViewById(R.id.notis);
                PopupMenu popup = new PopupMenu(MainMenuActivity.this, notis);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.pop_up_notis, popup.getMenu());
                notifyBidsChanged(popup);

                popup.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Overrides the onStart function
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * States what happens when the back button is pressed
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,LoginActivity.class);
        LoginActivity.username = null;
        startActivity(intent);
    }

    /**
     * Sends the user to a view holding all their tasks. This happens when the user
     * pressed the floating action button at the bottom right of the screen.
     *
     * @param view a view object representing the view to be loaded/used
     */
    public void sendToDashboard(View view) {
        Intent intent = new Intent(this, MyTasksActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


    /**
     * Loads the "Create task" view when the user selects the corresponding button.
     *
     * @param view the view to be used for the activity
     */
    public void sendToCreateTaskActivity(View view) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        intent.putExtra("username", username);
        startActivityForResult(intent, ADD_CODE);
    }

    /**
     * Loads the "Solve task" view when the user selects the corresponding button.
     *
     * @param view a view object that is used in the function
     */
    public void sendToSolveTaskActivity(View view) {
        Intent intent = new Intent(this, SolveTaskActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /**
     * States what happens when an activity is run.
     *
     * @param requestCode an integer representing the action that the activity should perform
     * @param resultCode an integer that the activity returns specifying whether the activity
     *                   was successfully complete or not
     * @param intent an intent object used to load the proper intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == ADD_CODE && resultCode == RESULT_OK) {
            Toast.makeText(MainMenuActivity.this, "Add requested task to user successful", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_CODE && resultCode == RESULT_OK){
            User user;
            ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
            getUser.execute(username);
            try {
                user = getUser.get();
                Log.e("Got the username: ", user.getUsername());
                Log.e("Got the email: ", user.getEmail());
                Log.e("Got the phone: ", user.getPhonenumber());

            } catch (Exception e) {
                Log.e("Error", "We aren't getting the user");
                return;
            }

            Toast.makeText(MainMenuActivity.this, "Edit user profile successful", Toast.LENGTH_SHORT).show();


        }
    }

    /**
     * A team-defined function that updates the items in the pop-up window
     *
     * @param popup a popup menu item that specifies which menu to update
     */
    public void notifyBidsChanged(PopupMenu popup) {
        ArrayList<Task> taskList;
        ElasticSearchController.GetAllTask getAllTask = new ElasticSearchController.GetAllTask();
        getAllTask.execute(username);
        try {
            taskList = getAllTask.get();    // get all the tasks in the server
            Log.e("Got the tasks ", taskList.toString());
            Iterator itr = taskList.iterator();     // create an iterator for the array list
            while (itr.hasNext()) {
                Log.e("In the itr loop", "we are");
                Task x = (Task) itr.next();
                ElasticSearchController.GetBidsByTaskID idBids = new ElasticSearchController.GetBidsByTaskID();
                idBids.execute(x.getId()); // grab all current users in the system
                ArrayList<Bid> xBids = idBids.get();
                if (x.getRequester().equals(username) && xBids.size() > 0) {
                    /* If the usernames are the same and the task has multiple bids,
                    add it to the popup menu to be displayed
                     */
                    String taskId = x.getId();
                    taskIds.add(taskId);

                    String displayString = "You have " + xBids.size() +
                            " new bids on task: " + x.getTitle();
                    popup.getMenu().add(displayString);

                }
            }

        } catch (Exception e) {
            Log.e("Error", "in notify bids changed");
            return;

        }

        if (taskIds.size() == 0) {
            popup.getMenu().add("You don't have any notifications");
        }

        loadNotificationItem(popup);

    }

    /**
     * When a user clicks on a item in their notifications popup, they are brought
     * to the screen displaying the information for the task they selected in the popup.
     *
     * @param popup a popup menu item that specifies which popup to load
     */
    public void loadNotificationItem(PopupMenu popup) {
        if (taskIds.size() == 0) {
            return;
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(MainMenuActivity.this, DashboardRequestedTask.class);
                Integer taskIndex = item.getItemId();
                Log.e("Task index", taskIndex.toString());
                String taskName = taskIds.get(taskIndex);

                ElasticSearchController.GetTask getTask=new ElasticSearchController.GetTask();
                getTask.execute(taskName);
                Task task;

                try {
                    task = getTask.get();
                    String taskId = task.getId();
                    intent.putExtra("username", username);
                    intent.putExtra("id", taskId);

                } catch (Exception e) {
                    Log.e("Error", "in loading task item");

                }
                startActivity(intent);

                return true;
            }
        });

    }
}
