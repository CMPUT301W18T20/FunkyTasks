package com.example.android.funkytasks;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.robotium.solo.Solo;

import java.util.ArrayList;


/**
 * Created by ${fc1} on 2018-03-11.
 */

public class SignUpActivityTest extends ActivityInstrumentationTestCase2<SignUpActivity> {
    private Solo solo;
    private User existUser;

    private User testingUser = new User("IntentTesting", "it@gmail.com", "1112221111");

    public SignUpActivityTest(){
        super(com.example.android.funkytasks.SignUpActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();

    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    //signing up an existing user
    public void testFailedSignUp(){
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        //existing user "qwerty123"
        existUser =  new User("SignUpTest","123@email.com", "2221112222");

        //Post the user if the user doesnt exist
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
            if (postedUser.getUsername().equals(existUser.getUsername())) {
                break;
            }
            else {
                ElasticSearchController.PostUser postUser = new ElasticSearchController.PostUser();
                postUser.execute(existUser);
            }
        }
        solo.enterText((EditText) solo.getView(R.id.editAddUsername), existUser.getUsername());
        solo.enterText((EditText) solo.getView(R.id.editAddPhone), existUser.getPhonenumber());
        solo.enterText((EditText) solo.getView(R.id.editAddEmail), existUser.getEmail());
        View fab = getActivity().findViewById(R.id.fabSignUp);
        solo.clickOnView(fab);
        solo.waitForText("Username Taken");
        assertTrue(solo.searchText("Username Taken"));
        solo.assertCurrentActivity("Wrong activity", SignUpActivity.class);

    }

    public void testSignUp() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editAddUsername), testingUser.getUsername());
        solo.enterText((EditText) solo.getView(R.id.editAddPhone), testingUser.getPhonenumber());
        solo.enterText((EditText) solo.getView(R.id.editAddEmail), testingUser.getEmail());
        View fab = getActivity().findViewById(R.id.fabSignUp);
        solo.clickOnView(fab);
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);

    }

    @Override
    public void tearDown() throws Exception{
        ElasticSearchController.deleteUser DeUser = new ElasticSearchController.deleteUser();
        DeUser.execute(testingUser.getId());
        DeUser.execute(existUser.getId());
        solo.finishOpenedActivities();

    }
}
