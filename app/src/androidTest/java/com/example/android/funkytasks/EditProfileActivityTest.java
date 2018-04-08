package com.example.android.funkytasks;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by ${fc1} on 2018-03-15.
 */
public class EditProfileActivityTest extends ActivityInstrumentationTestCase2{
        private Solo solo;
        private User user;

    public EditProfileActivityTest(){
        super(com.example.android.funkytasks.LoginActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void addUser(){
        user = new User("qwerty123", "123@gmail.com", "1112221111");
        ElasticSearchController.GetAllUsers allUsers = new ElasticSearchController.GetAllUsers();
        allUsers.execute(); // grab all current users in the system
        ArrayList<User> userList = new ArrayList<User>();
        try {
            userList = allUsers.get();
        } catch (Exception e) {
            Log.e("Error", "Failed to get list of users");
        }
        for (User postedUser : userList) {
            Log.e("ALl usernames", postedUser.getUsername());
            if (postedUser.getUsername().equals(user.getUsername())) {
                return;
            }
        }
        ElasticSearchController.PostUser postUser = new ElasticSearchController.PostUser();
        postUser.execute(user);
    }


    public void goToEditProfile() throws Exception{
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        addUser();
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "qwerty123");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnView(solo.getView(R.id.profileActionBar));
        solo.waitForActivity("EditProfileActivity.class");
        solo.assertCurrentActivity("Wrong activity", EditProfileActivity.class);

    }

    public void testUpdateProfile() throws Exception{
        goToEditProfile();
        solo.assertCurrentActivity("Wrong activity", EditProfileActivity.class);
        String newPhone = "1112221111";
        String newEmail = "12345@gmail.com";
        solo.clearEditText((EditText) solo.getView(R.id.EditPhone));
        solo.clearEditText((EditText) solo.getView(R.id.EditEmail));
        solo.enterText((EditText) solo.getView(R.id.EditPhone), newPhone);
        solo.enterText((EditText) solo.getView(R.id.EditEmail), newEmail);
        solo.clickOnText("DONE");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnView(solo.getView(R.id.profileActionBar));
        solo.waitForActivity("EditProfileActivity.class");
        //check changes
        assertTrue(solo.searchText(newPhone));
        assertTrue(solo.searchText(newEmail));
        //RESTORE CHANGES
        solo.clearEditText((EditText) solo.getView(R.id.EditPhone));
        solo.clearEditText((EditText) solo.getView(R.id.EditEmail));
        solo.enterText((EditText) solo.getView(R.id.EditPhone), "2221112222");
        solo.enterText((EditText) solo.getView(R.id.EditEmail), "321@email.com");
        solo.clickOnText("DONE");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}