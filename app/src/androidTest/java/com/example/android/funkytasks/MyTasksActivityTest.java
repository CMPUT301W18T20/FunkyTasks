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


    public void goToMyTask() {
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "qwerty123");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnView(solo.getView(R.id.fab));
        solo.waitForActivity("MyTasksActivity.class");
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
    }

    public void addTask(){
        newTask = new Task("Dummy task", "Testing for delete", "qwerty123");
        ElasticSearchController.PostTask postTask = new ElasticSearchController.PostTask();
        postTask.execute(newTask);
    }


    public void testRequesterTab(){
        addTask();
        goToMyTask();
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
        solo.clickOnActionBarItem(R.id.tabItem);
        solo.clickOnText(newTask.getTitle());
        solo.waitForActivity("DashboardRequestedTask.class");
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
    }

    public void testProviderTest(){
        addTask();
        goToMyTask();
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
        solo.clickOnText("To Solve");
        solo.waitForText("Update");
        solo.clickOnText("Update");
        solo.waitForActivity("DashboardProviderTask.class");
        solo.assertCurrentActivity("Wrong activity", DashboardProviderTask.class);
    }




    @After
    public void tearDown() throws Exception {
        ElasticSearchController.deleteTask DeTask = new ElasticSearchController.deleteTask();
        DeTask.execute(newTask.getId());
        solo.finishOpenedActivities();
    }

}