package com.example.android.funkytasks;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by ${fc1} on 2018-03-15.
 */
public class MainMenuActivityTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;
    private User user;

    public MainMenuActivityTest(){
        super(com.example.android.funkytasks.LoginActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void login() throws Exception{
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        user = new User("qwerty123", "1234567890", "IT@ualbertac.ca");
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
                break;
            }
            else {
                ElasticSearchController.PostUser postUser = new ElasticSearchController.PostUser();
                postUser.execute(user);
            }
        }
        solo.enterText((EditText) solo.getView(R.id.editLoginName), user.getUsername());
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
    }

    public void testProfile() throws Exception{
        login();
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnView(solo.getView(R.id.profileActionBar));
        solo.waitForActivity("EditProfileActivity.class");
        solo.assertCurrentActivity("Wrong activity", EditProfileActivity.class);
        //solo.goBack();
        //solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);

    }

    public void testCreateTask() throws Exception{
        login();
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        //button 2 = "create a funky task"
        solo.clickOnButton("create a funky task");
        solo.waitForActivity("CreateTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", CreateTaskActivity.class);
        //solo.goBack();
        //solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
    }

    public void testSolveTask() throws Exception{
        login();
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnButton("solve a funky task");
        solo.waitForActivity("SolveTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", SolveTaskActivity.class);
    }

    public void testMyTask() throws Exception{
        login();
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnView(solo.getView(R.id.fab));
        solo.waitForActivity("MyTasksActivity.class");
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}