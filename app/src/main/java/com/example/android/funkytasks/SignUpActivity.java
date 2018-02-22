package com.example.android.funkytasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    ArrayList<User> userArrayList = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userArrayList = ((GlobalVariables) this.getApplication()).getUserArrayList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp(view);
            }
        });
    }

    public void signUp(View view){
        String username;
        String phone;
        String email;

        EditText inputUsername = (EditText) findViewById(R.id.editAddUsername);
        username = inputUsername.getText().toString();
        EditText inputPhone= (EditText) findViewById(R.id.editAddPhone);
        EditText inputEmail = (EditText) findViewById(R.id.editAddEmail);
        email = inputEmail.getText().toString();
        phone = inputPhone.getText().toString();

        if(username == null || username.isEmpty() || phone == null || phone.isEmpty() || email == null || email.isEmpty()){
            Toast.makeText(SignUpActivity.this, "One or more fields are missing.", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            checkForClash(username, phone, email);
        }
    }

    public void checkForClash(String username, String phone, String email){
        User userToAdd = new User(username, email, phone);

        for(User user: userArrayList){
            if(username.equals(user.getUsername())){
                Toast.makeText(SignUpActivity.this, "Username Taken", Toast.LENGTH_SHORT).show();
            }
            else if(username.length() < 8){
                Toast.makeText(SignUpActivity.this, "Username needs to be at least 8 characters long", Toast.LENGTH_SHORT).show();
            }
            else if(phone.equals(user.getPhonenumber())){
                Toast.makeText(SignUpActivity.this, "Phone number already in use", Toast.LENGTH_SHORT).show();
            }
            else if(email.equals(user.getEmail())){
                Toast.makeText(SignUpActivity.this, "E-mail already in use", Toast.LENGTH_SHORT).show();
            }
            else{
                userArrayList.add(userToAdd);
                ((GlobalVariables) this.getApplication()).setUserArrayList(userArrayList);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }

}
