package com.example.android.funkytasks;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by ${fc1} on 2018-03-17.
 */
public class DashboardRequestedTaskTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public DashboardRequestedTaskTest(){
        super(LoginActivity.class);
    }
    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }


    public void goToDashboardRequested(){
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
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
        Task newTask = new Task("Dummy task", "Testing for delete", "qwerty123", 0);
        ElasticSearchController.PostTask postTask = new ElasticSearchController.PostTask();
        postTask.execute(newTask);
    }

    public void testEdit(){
        goToDashboardRequested();
        solo.clickOnText("test");
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
        solo.clickOnView(solo.getView(R.id.editRequestedTask));
        solo.waitForActivity("EditDashboardRequestedTask.class");
        solo.assertCurrentActivity("Wrong activity", EditDashboardRequestedTask.class);

    }

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