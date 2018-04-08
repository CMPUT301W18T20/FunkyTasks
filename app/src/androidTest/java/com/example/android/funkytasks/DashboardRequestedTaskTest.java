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
 * Created by ${fc1} on 2018-03-17.
 */
public class DashboardRequestedTaskTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private User user;
    private Task newTask;

    public DashboardRequestedTaskTest(){
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


    //test for US 01.02.01
    public void goToDashboardRequested(){
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        addUser();
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "qwerty123");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnView(solo.getView(R.id.fab));
        solo.waitForActivity("MyTasksActivity.class");
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
        solo.clickOnActionBarItem(R.id.tabItem);
    }

    public void addTask(){
        newTask = new Task("Dummy task", "Testing", "qwerty123");
        ElasticSearchController.PostTask postTask = new ElasticSearchController.PostTask();
        postTask.execute(newTask);
    }

    //test for US 01.03.01
    public void testEdit(){
        addTask();
        goToDashboardRequested();
        solo.clickOnText(newTask.getTitle());
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
        solo.clickOnView(solo.getView(R.id.editRequestedTask));
        solo.waitForActivity("EditDashboardRequestedTask.class");
        solo.assertCurrentActivity("Wrong activity", EditDashboardRequestedTask.class);

    }

    //test for US 01.04.01
    public void testDelete(){
        addTask();
        goToDashboardRequested();
        assertTrue(solo.searchText("Dummy task"));
        solo.clickOnText("Dummy task");
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
        solo.clickOnView(solo.getView(R.id.deleteActionBar));
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
        assertFalse(solo.searchText("Dummy task"));

    }



    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}