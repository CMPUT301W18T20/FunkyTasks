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

//Perform this test with pre-existing account test1111 and test2222.
    //create task providerBd and providerAs with test2222, and let test1111 bid on these two tasks, and assign requesterAs to test1111.
    //create task requestorBd and requestorAs with test1111, and assign requestorAs to test2222,and bid requestorBd with test2222.
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


    public void goToMyTask() {
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "test1111");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnView(solo.getView(R.id.fab));
        solo.waitForActivity("MyTasksActivity.class");
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
    }

    //test for US 05.02.01
    public void testViewProviderBiddedOnTaskTest(){
        goToMyTask();
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
        solo.clickOnText("To Solve");
        solo.waitForText("MY ASSOCIATED TASKS");
        solo.clickOnText("MY ASSOCIATED TASKS");
        solo.waitForText("BIDDED ON");
        solo.clickOnText("BIDDED ON");
        assertTrue(solo.searchText("providerBd"));
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
        assertTrue(solo.searchText("requestorBd"));

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
        assertTrue(solo.searchText("requesterAs"));
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
        assertTrue(solo.searchText("providerAs"));
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}