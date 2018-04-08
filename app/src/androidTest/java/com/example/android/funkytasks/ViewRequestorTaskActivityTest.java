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
 * Created by ${fc1} on 2018-03-18.
 */
public class ViewRequestorTaskActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private User user;

    public ViewRequestorTaskActivityTest(){
        super(LoginActivity.class);
    }

    //checks if user exists, if it doesnt post the user
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
    public void goToViewRequeestedTask(){
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        addUser();
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "qwerty123");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnButton("solve a funky task");
        solo.waitForActivity("SolveTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity",SolveTaskActivity.class);

    }

    //cancel button bid
    public void testClickBidCancel(){
        goToViewRequeestedTask();
        //exising task posted by another user with description "Solve"
        solo.enterText((EditText) solo.getView(R.id.search), "Solve");
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.waitForText("SolveTask");
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText("SolveTask");
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
        //existing task posted by another user with description "place" that has not been bid on by the user "qwerty123"
        solo.enterText((EditText) solo.getView(R.id.search), "place");
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText("Place");
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
        solo.clickOnButton("Bid");
        assertTrue(solo.searchText("Place Bid"));
        solo.enterText((EditText) solo.getView(R.id.bidMoney), "10.0");
        solo.clickOnButton("Place Bid");
        assertFalse(solo.searchText("Place Bid"));
        solo.waitForActivity("SolveTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", SolveTaskActivity.class);

    }

    public void testClickBidUpdate(){
        goToViewRequeestedTask();
        //existing task posted by another user with description "update" that has been bid on by the user "qwerty123"
        solo.enterText((EditText) solo.getView(R.id.search), "update");
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.waitForText("Update");
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText("Update");
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
        solo.clickOnButton("Bid");
        assertTrue(solo.searchText("Update Bid"));
        solo.enterText((EditText) solo.getView(R.id.bidMoney), "10.0");
        solo.clickOnButton("Update Bid");
        solo.waitForActivity("SolveTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", SolveTaskActivity.class);
    }

    public void testViewLocation(){
        goToViewRequeestedTask();
        //existing task posted by another user with description "update" that has been bid on by the user "qwerty123"
        solo.enterText((EditText) solo.getView(R.id.search), "update");
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.waitForText("Update");
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText("Update");
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
        solo.clickOnButton("View Location");
        solo.waitForActivity("DisplayMap.class");
        solo.assertCurrentActivity("Wrong acticity", DisplayMap.class);
    }

    public void testNoPhotos(){
        goToViewRequeestedTask();
        //existing task posted by another user with description "update" that has been bid on by the user "qwerty123"
        solo.enterText((EditText) solo.getView(R.id.search), "update");
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.waitForText("Update");
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText("Update");
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
        solo.clickOnButton("viewphoto");
        solo.waitForText("No photos to show");
        assertTrue(solo.searchText("No photos to show"));
    }

    public void testPhotos(){
        goToViewRequeestedTask();
        //existing task posted by another user with description "photo" and photo
        solo.enterText((EditText) solo.getView(R.id.search), "photo");
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.waitForText("photo");
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText("photo");
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
        solo.clickOnButton("viewphoto");
        solo.waitForActivity("ImageDetails.class");
        solo.assertCurrentActivity("Wrong activity", ImageDetails.class);
    }

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}