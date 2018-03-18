package com.example.android.funkytasks;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by ${fc1} on 2018-03-18.
 */
public class ViewRequestorTaskActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public ViewRequestorTaskActivityTest(){
        super(LoginActivity.class);
    }

    public void goToViewRequeestedTask(){
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "qwerty123");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnButton("solve a funky task");
        solo.waitForActivity("SolveTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity",SolveTaskActivity.class);

    }

    //Update bid
    public void testClickBidCancel(){
        goToViewRequeestedTask();
        solo.enterText((EditText) solo.getView(R.id.search), "drink");
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.waitForText("pureleaf");
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText("pureleaf");
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
        solo.clickOnButton("Bid");
        assertTrue(solo.searchText("Place Bid"));
        solo.clickOnButton("Cancel");
        assertFalse(solo.searchText("Place Bid"));
    }

    //Bid on task
    public void testClickOnBid(){
        goToViewRequeestedTask();
        solo.enterText((EditText) solo.getView(R.id.search), "bid");
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText("bid");
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
        solo.clickOnButton("Bid");
        assertTrue(solo.searchText("Place Bid"));
        solo.clickOnButton("Cancel");
        assertFalse(solo.searchText("Place Bid"));
    }

/*    public void testClickBidUpdate(){
        goToViewRequeestedTask();
        solo.enterText((EditText) solo.getView(R.id.search), "juice");
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.waitForText("test");
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText("test");
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
        solo.clickOnButton("Bid");
        assertTrue(solo.searchText("Update Bid"));
        solo.enterText((EditText) solo.getView(R.id.bidMoney), "1.0");
        solo.clickOnButton("Update Bid");
        solo.waitForActivity("SolveTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", SolveTaskActivity.class);
    }*/


    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}