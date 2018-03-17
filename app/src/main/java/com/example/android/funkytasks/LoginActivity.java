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

public class LoginActivity extends AppCompatActivity{

    ArrayList<User> userArrayList = new ArrayList<User>();
    public static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Welcome to FunkyTasks!");
        EditText inputUsername = (EditText) findViewById(R.id.editLoginName);
        inputUsername.setHintTextColor(Color.WHITE);

    }

    @Override
    protected void onStart () {
        super.onStart();
        userArrayList = ((GlobalVariables) this.getApplication()).getUserArrayList();
    }

    public void sendToMainMenu(View view){

        EditText inputUsername = (EditText) findViewById(R.id.editLoginName);
        username = inputUsername.getText().toString();


        if(username == null || username.isEmpty()){
            Toast.makeText(LoginActivity.this, "You can't login as no one. Valar Morghulis.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            checkUserName(username);

        }
    }

    public void checkUserName(String checkusername){

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
            if (postedUser.getUsername().equals(checkusername)){ // if user is in th system, log them in
                Toast.makeText(LoginActivity.this, "Logging in", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainMenuActivity.class);
                username = checkusername;
                intent.putExtra("username", postedUser.getUsername());
                startActivity(intent);
                finish();
            }
        }

        Toast.makeText(LoginActivity.this, "Incorrect username", Toast.LENGTH_SHORT).show();
        return;

    }

    public void sendToSignUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }
}
