package com.example.android.funkytasks;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by ${fc1} on 2018-03-19.
 */
//before testing;
    //assign tasks with title "test" and "testEdit" to user "qwerty123"

public class DashboardProviderTaskTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private User user;
    private Task newTask;

    public DashboardProviderTaskTest(){
        super(LoginActivity.class);
    }

    @Before
    public void setUp() throws Exception {
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

    public void goToDashboardProvider(){
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        addUser();
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "qwerty123");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnView(solo.getView(R.id.fab));
        solo.waitForActivity("MyTasksActivity.class");
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
        solo.clickOnText("To Solve");
        solo.waitForText("Update");
        solo.clickOnText("Update");
        solo.waitForActivity("DashboardProviderTask.class");

    }

    public void testProvider(){
        goToDashboardProvider();
        solo.assertCurrentActivity("Wrong activity", DashboardProviderTask.class);
        solo.clickOnButton("UPDATE BID");
        solo.waitForText("Your current bid");
        assertTrue(solo.searchText("Your current bid"));
        solo.enterText((EditText) solo.getView(R.id.bidMoney), "10.0");
        solo.clickOnButton("Update Bid");
        solo.waitForText("Successfully updated a bid");
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
        solo.clickOnText("To Solve");
        solo.waitForText("Update");
        solo.clickOnText("Update");
        solo.waitForActivity("DashboardProviderTask.class");
        solo.assertCurrentActivity("Wrong activity", DashboardProviderTask.class);
        solo.clickOnButton("UPDATE BID");
        solo.waitForText("Your current bid");
        assertTrue(solo.searchText("Your current bid"));
        solo.clickOnButton("Cancel");
        assertTrue(solo.searchText("REQUESTED BY"));
    }
    public void testViewPhoto(){
        goToDashboardProvider();
        assertTrue(solo.searchText("test"));
        solo.clickOnText("test");
        solo.assertCurrentActivity("Wrong activity", DashboardProviderTask.class);
        solo.clickOnView(solo.getView(R.id.picdetails));
        solo.waitForActivity("ImageDetails.class");
        solo.assertCurrentActivity("Wrong activity", ImageDetails.class);
    }
    public void testNoPhoto(){
        goToDashboardProvider();
        assertTrue(solo.searchText("testEdit"));
        solo.clickOnText("testEdit");
        solo.assertCurrentActivity("Wrong activity", DashboardProviderTask.class);
        solo.clickOnView(solo.getView(R.id.picdetails));
        solo.assertCurrentActivity("Wrong activity", DashboardProviderTask.class);
    }




    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}