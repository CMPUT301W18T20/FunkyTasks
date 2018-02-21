package com.example.android.funkytasks;

import android.app.Application;
import android.content.Intent;
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
//            ((GlobalVariables) this.getApplication()).getUserArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userArrayList = ((GlobalVariables) this.getApplication()).getUserArrayList();

        User testuser1 = new User("Jim", "jim@ualberta.ca", "6666969642");

        userArrayList.add(testuser1);
        Log.e("WHAT THE FUCK", "The globalvariable is not fuck up");
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
            //TODO implement alphanum check
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
                finish();
            }
            else{
                Toast.makeText(LoginActivity.this, "Incorrect username", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    public void sendToSignUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }
}
