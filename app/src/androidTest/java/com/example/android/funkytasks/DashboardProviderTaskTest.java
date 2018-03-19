package com.example.android.funkytasks;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by ${fc1} on 2018-03-19.
 */
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

    public void goToDashboardProvider(){
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
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


    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}