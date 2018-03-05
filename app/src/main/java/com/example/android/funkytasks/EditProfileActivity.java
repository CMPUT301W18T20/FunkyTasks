package com.example.android.funkytasks;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EditProfileActivity extends AppCompatActivity {
    private String username;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Button done = (Button) findViewById(R.id.doneEditProfile);

        final Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        Log.e("USERNAME", username);
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(username);

        try {
            user = getUser.get();
            Log.e("Got the username: ", user.getUsername());


        } catch (Exception e) {
            Log.e("Error", "We arnt getting the user");
            return;
        }

        //id of username textView = EditUsername
        //id of email editText = EditEmail
        //id of phone editText = EditPhone
        final TextView userName = findViewById(R.id.EditUsername);
        final EditText email = findViewById(R.id.EditEmail);
        final EditText phone = findViewById(R.id.EditPhone);

        userName.setText(username);
        email.setText(user.getEmail(), TextView.BufferType.EDITABLE);
        phone.setText(user.getPhonenumber(), TextView.BufferType.EDITABLE);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });



    }
}
