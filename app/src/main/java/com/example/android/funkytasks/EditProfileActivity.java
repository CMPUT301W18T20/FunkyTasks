/**
 * EditProfileActivity
 *
 * Version 1.0.0
 *
 * Created on March 8th by Funky Tasks
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

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
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * This activity allows a user to edit their profile information
 */
public class EditProfileActivity extends BaseActivity {
    private String username;
    private User user;

    private TextView userName;
    private EditText email;
    private EditText phone;

    /**
     * Overrides the built-in onCreate function and prepares the app for interaction.
     * This function loads the proper view and locates all the text fields
     * and buttons that are needed to interact with the activity.
     *
     * @param savedInstanceState a bundle holding the state the app was in last time it was open
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Button done = findViewById(R.id.doneEditProfile);

        final Intent intent = getIntent();
        userName = findViewById(R.id.EditUsername);
        email = findViewById(R.id.EditEmail);
        phone = findViewById(R.id.EditPhone);

        username = intent.getExtras().getString("username");
        username = LoginActivity.username;

        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(username);

        try {
            user = getUser.get();
            Log.e("Got the username: ", user.getUsername());

        } catch (Exception e) {
            Log.e("Error", "We arnt getting the user");
            return;
        }

        if (user != null){
            double rating = user.getRating();
            TextView ratingView = findViewById(R.id.ratingsViewProfile);
            ratingView.setText(Double.toString(user.getRating())+"/5 \uD83C\uDF4C");

            userName.setText(username);
            email.setText(user.getEmail(), TextView.BufferType.EDITABLE);
            phone.setText(user.getPhonenumber(), TextView.BufferType.EDITABLE);

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String newEmail = email.getText().toString();
                    String newPhone = phone.getText().toString();
                    if (!validateInput(newEmail,newPhone)){
                        return;
                    }

                    user.setEmail(newEmail);
                    user.setPhonenumber(newPhone);

                    ElasticSearchController.updateUser updateUser= new ElasticSearchController.updateUser();
                    updateUser.execute(user);

                    setResult(RESULT_OK);
                    finish();
                }
            });
        }

    }

    /**
     * Validates the user input of the phone and email
     * @param newEmail the new user input email
     * @param newPhone the new user input phone
     * @return a boolean checking if input was ok or not
     */

    private boolean validateInput(String newEmail, String newPhone){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!newEmail.matches(emailPattern)){
            Toast.makeText(EditProfileActivity.this,"Invalid email address format",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (newPhone.length() != 10){
            Toast.makeText(EditProfileActivity.this,"Invalid phone number length",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
