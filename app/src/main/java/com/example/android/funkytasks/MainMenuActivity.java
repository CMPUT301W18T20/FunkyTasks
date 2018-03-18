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

public class MainMenuActivity extends AppCompatActivity {
    public static ArrayList<Task> tasksArrayList = new ArrayList<Task>();
    ArrayList<User> userArrayList = new ArrayList<User>();
    public static String username;
    final int ADD_CODE = 1;
    final int EDIT_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Main Menu");
        Intent intent = getIntent();

        //username = intent.getExtras().getString("username");
        username = LoginActivity.username;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToDashboard(view);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileActionBar:
                Intent intent = new Intent(MainMenuActivity.this, EditProfileActivity.class);
                intent.putExtra("username", username);
                startActivityForResult(intent, EDIT_CODE);
                return true;

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


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,LoginActivity.class);
        LoginActivity.username = null;
        startActivity(intent);
    }

    public void sendToDashboard(View view) {
        Intent intent = new Intent(this, MyTasksActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


    public void sendToCreateTaskActivity(View view) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        intent.putExtra("username", username);
        startActivityForResult(intent, ADD_CODE);
    }

    public void sendToSolveTaskActivity(View view) {
        Intent intent = new Intent(this, SolveTaskActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == ADD_CODE && resultCode == RESULT_OK) {
            Task newTask = (Task)intent.getSerializableExtra("task");
            ElasticSearchController.PostTask postTask = new ElasticSearchController.PostTask();
            postTask.execute(newTask);
            try {
                newTask = postTask.get();
                Log.e("newtask title",newTask.getTitle());
            }
            catch(Exception e){
                Log.e("Error","Task not posted");
            }

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

    public void notifyBidsChanged(PopupMenu popup) {
        ArrayList<Task> taskList;
        ElasticSearchController.GetAllTask getAllTask = new ElasticSearchController.GetAllTask();
        getAllTask.execute(username);
        try {
            taskList = getAllTask.get();
            Log.e("Got the tasks ", taskList.toString());
            Iterator itr = taskList.iterator();
            while (itr.hasNext()) {
                Log.e("In the itr loop", "we are");
                Task x = (Task) itr.next();
                ElasticSearchController.GetBidsByTaskID idBids = new ElasticSearchController.GetBidsByTaskID();
                idBids.execute(x.getId()); // grab all current users in the system
                ArrayList<Bid> xBids = idBids.get();
                if (x.getRequester().equals(username) && xBids.size() > 0) {
                    popup.getMenu().add("You have " + xBids.size() +
                            " new bids on task: " + x.getTitle());
                }
            }

        } catch (Exception e) {
            Log.e("Error", "We arnt getting the list of tasks");
            return;

        }

    }
}
