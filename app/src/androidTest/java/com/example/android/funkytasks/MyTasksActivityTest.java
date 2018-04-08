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
public class MyTasksActivityTest  extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private User user;
    private Task newTask;

    public MyTasksActivityTest(){
        super(LoginActivity.class);
    }
    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    //checks if user exists, if it doesnt post the user
//    public void addUser(){
//        user = new User("qwerty123", "123@gmail.com", "1112221111");
//        ElasticSearchController.GetAllUsers allUsers = new ElasticSearchController.GetAllUsers();
//        allUsers.execute(); // grab all current users in the system
//        ArrayList<User> userList = new ArrayList<User>();
//        try {
//            userList = allUsers.get();
//        } catch (Exception e) {
//            Log.e("Error", "Failed to get list of users");
//        }
//        for (User postedUser : userList) {
//            Log.e("ALl usernames", postedUser.getUsername());
//            if (postedUser.getUsername().equals(user.getUsername())) {
//                return;
//            }
//        }
//        ElasticSearchController.PostUser postUser = new ElasticSearchController.PostUser();
//        postUser.execute(user);
//    }


    public void goToMyTask() {
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
//        addUser();
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "test1111");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnView(solo.getView(R.id.fab));
        solo.waitForActivity("MyTasksActivity.class");
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
    }
//
//    public void addTask(){
//        newTask = new Task("Dummy task", "Testing for delete", "qwerty123");
//        ElasticSearchController.PostTask postTask = new ElasticSearchController.PostTask();
//        postTask.execute(newTask);
//    }
//
//
//
//    public void testRequesterTab(){
//        addTask();
//        goToMyTask();
//        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
//        solo.clickOnActionBarItem(R.id.tabItem);
//        solo.clickOnText(newTask.getTitle());
//        solo.waitForActivity("DashboardRequestedTask.class");
//        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
//    }

//    public void testProviderTest(){
//        addTask();
//        goToMyTask();
//        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
//        solo.clickOnText("To Solve");
//        solo.waitForText("Update");
//        solo.clickOnText("Update");
//        solo.waitForActivity("DashboardProviderTask.class");
//        solo.assertCurrentActivity("Wrong activity", DashboardProviderTask.class);
//    }
    //test for US 05.02.01
    public void testViewProviderBiddedOnTaskTest(){
        goToMyTask();
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
        solo.clickOnText("To Solve");
        solo.waitForText("MY ASSOCIATED TASKS");
        solo.clickOnText("MY ASSOCIATED TASKS");
        solo.waitForText("BIDDED ON");
        solo.clickOnText("BIDDED ON");
    }
    //test for US 05.04.01
    public void testViewRequestorBiddedTaskTest(){
        goToMyTask();
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
        solo.clickOnText("Your Posts");
        solo.waitForText("My Tasks");
        solo.clickOnText("My Tasks");
        solo.waitForText("Bidded");
        solo.clickOnText("Bidded");
    }

    //test for US 06.02.01
    public void testViewRequestorAssignedTask(){
        goToMyTask();
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
        solo.clickOnText("Your Posts");
        solo.waitForText("My Tasks");
        solo.clickOnText("My Tasks");
        solo.waitForText("Assigned");
        solo.clickOnText("Assigned");
    }
    //US 06.01.01
    public void testViewproviderAsignedTask(){
        goToMyTask();
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
        solo.clickOnText("To Solve");
        solo.waitForText("MY ASSOCIATED TASKS");
        solo.clickOnText("MY ASSOCIATED TASKS");
        solo.waitForText("SOLVING");
        solo.clickOnText("SOLVING");
    }

    @After
    public void tearDown() throws Exception {
//        ElasticSearchController.deleteTask DeTask = new ElasticSearchController.deleteTask();
//        DeTask.execute(newTask.getId());
        solo.finishOpenedActivities();
    }

}