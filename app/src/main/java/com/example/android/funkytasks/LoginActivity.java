package com.example.android.funkytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

//TODO create sign up page
//TODO check if intent is for result or not

public class LoginActivity extends AppCompatActivity {

    public ArrayList<User> userArrayList = new ArrayList<User>();

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        User testuser1 = new User("Jim", "jim@ualberta.ca", "6666969642");

        userArrayList.add(testuser1);

    }

    public void sendToMainMenu(View view){

        String username;
        EditText inputUsername = (EditText) findViewById(R.id.editLoginName);
        username = inputUsername.getText().toString();

        if(username == null || username.isEmpty()){
            Toast.makeText(LoginActivity.this, "You can't login as no one. Valar Morghulis.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
//            if (username.matches(".*[A-Za-z].*") && username.matches(".*[0-9].*") && username.matches("[A-Za-z0-9]*")) {
//                checkUserName(username);
//            } else {
//                Toast.makeText(LoginActivity.this, "Incorrect username", Toast.LENGTH_SHORT).show();
//                return;
//            }
            checkUserName(username);

        }

    }

    public void checkUserName(String username){
        for (User user: userArrayList){
            Log.e("HALP", user.getUsername());
            if(username.equals(user.getUsername())){
                Intent intent = new Intent(this, MainMenuActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(LoginActivity.this, "Incorrect username", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }
}
