/**
 * This activity allows a user to login to the app.
 *
 * Version 1.0.0
 *
 * Create by Funky Tasks on March 8th
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

//TODO check if intent is for result or not

/**
 * This activity allows a user to login
 */
public class LoginActivity extends AppCompatActivity{

    ArrayList<User> userArrayList = new ArrayList<User>();
    public static String username;

    /**
     * Overrides the default onCreate function
     *
     * @param savedInstanceState a bundle representing the state of the app
     *                           the last time it was open
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Welcome to FunkyTasks!");
        EditText inputUsername = (EditText) findViewById(R.id.editLoginName);
        inputUsername.setHintTextColor(Color.WHITE);

    }

    /**
     * Overrides the default onStart function and updates the list of users from the global
     * variables file.
     */
    @Override
    protected void onStart () {
        super.onStart();
        userArrayList = ((GlobalVariables) this.getApplication()).getUserArrayList();
    }

    /**
     * This functions sends a user to the main menu.
     *
     * @param view a view object representing the view to be loaded
     */
    public void sendToMainMenu(View view){

        EditText inputUsername = findViewById(R.id.editLoginName);
        username = inputUsername.getText().toString();


        if(username == null || username.isEmpty()){
            Toast.makeText(LoginActivity.this,
                    "You can't login as no one. Valar Morghulis.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            checkUserName(username);

        }
    }

    /**
     * This function validates the username for sign-in
     *
     * @param checkUsername a string representing the username that was entered and needs
     *                      to be validated
     */
    public void checkUserName(String checkUsername){

        ElasticSearchController.GetAllUsers allUsers = new ElasticSearchController.GetAllUsers();
        allUsers.execute(""); // grab all current users in the system

        ArrayList<User> userList = new ArrayList<User>();

        try{
            userList = allUsers.get();
        }
        catch (Exception e)
        {
            Log.e("Error", "Failed to get list of users");
        }


        for (User postedUser: userList){
            Log.e("postedUser",postedUser.getUsername()); // print out all users in system
            if (postedUser.getUsername().equals(checkUsername)){ // if user is in th system, log them in
                Toast.makeText(LoginActivity.this, "Logging in", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainMenuActivity.class);
                username = checkUsername;
                intent.putExtra("username", postedUser.getUsername());
                startActivity(intent);
                finish();
            }
        }

        Toast.makeText(LoginActivity.this, "Incorrect username", Toast.LENGTH_SHORT).show();
        return;

    }

    /**
     * This activity is loaded when the user does not have an account and wishes to make one.
     *
     * @param view a view object representing the view to be loaded
     */
    public void sendToSignUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }
}
