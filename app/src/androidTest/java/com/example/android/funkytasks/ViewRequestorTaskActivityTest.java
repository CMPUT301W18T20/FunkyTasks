package com.example.android.funkytasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract;
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
    private Task cancel = new Task("CancelTask", "CancelTask", "monica11");
    private Task place = new Task("PlaceBid", "PlaceBid", "monica11");
    private Task photo = new Task("Photo", "Photo", "monica11");
    private ArrayList<Task> taskList;

    public ViewRequestorTaskActivityTest(){
        super(LoginActivity.class);
    }

    public void postTask(Task newTask){
        ElasticSearchController.PostTask postTask = new ElasticSearchController.PostTask();
        postTask.execute(newTask);
        solo.sleep(5000);
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


    public boolean checkTask(Task postTask) {
        ElasticSearchController.GetAllTask getAllTask = new ElasticSearchController.GetAllTask();
        getAllTask.execute("monica11");
        try {
            taskList = getAllTask.get();
            for (Task eachtask : taskList) {
                if (eachtask.getId().equals(postTask.getId())){
                    return true;
                }
            }

        } catch (Exception e) {
            Log.e("Error", "We aren't getting the list of tasks");
            return false;

        }
        return false;
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
        if (checkTask(cancel) == false){
            postTask(cancel);
        }
        goToViewRequeestedTask();
        //exising task posted by another user with description "Solve"
        solo.enterText((EditText) solo.getView(R.id.search), cancel.getDescription());
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.waitForText(cancel.getTitle());
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText(cancel.getTitle());
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
        solo.clickOnButton("Bid");
        assertTrue(solo.searchText("Place Bid"));
        solo.clickOnButton("Cancel");
        assertFalse(solo.searchText("Place Bid"));
        ElasticSearchController.deleteTask DeTask = new ElasticSearchController.deleteTask();
        DeTask.execute(cancel.getId());
        solo.sleep(5000);
    }

    //Bid on task
    public void testClickOnBid(){
        if (checkTask(place) == false){
            postTask(place);
        }
        goToViewRequeestedTask();
        solo.enterText((EditText) solo.getView(R.id.search), place.getDescription());
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.waitForText(place.getTitle());
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText(place.getTitle());
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
        solo.clickOnButton("Bid");
        assertTrue(solo.searchText("Place Bid"));
        solo.enterText((EditText) solo.getView(R.id.bidMoney), "10.0");
        solo.clickOnButton("Place Bid");
        assertFalse(solo.searchText("Place Bid"));
        solo.waitForActivity("SolveTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", SolveTaskActivity.class);
        //test for update
        solo.enterText((EditText) solo.getView(R.id.search), place.getDescription());
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.waitForText(place.getTitle());
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText(place.getTitle());
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
        solo.clickOnButton("Bid");
        assertTrue(solo.searchText("Update Bid"));
        solo.enterText((EditText) solo.getView(R.id.bidMoney), "5.0");
        solo.clickOnButton("Update Bid");
        solo.waitForActivity("SolveTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", SolveTaskActivity.class);
        ElasticSearchController.deleteTask DeTask = new ElasticSearchController.deleteTask();
        DeTask.execute(place.getId());
        solo.sleep(5000);
    }


    public void testViewLocation(){
        if (checkTask(place) == false){
            postTask(place);
        }
        goToViewRequeestedTask();
        //existing task posted by another user with description "update" that has been bid on by the user "qwerty123"
        solo.enterText((EditText) solo.getView(R.id.search), place.getDescription());
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.waitForText(place.getTitle());
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText(place.getTitle());
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
        solo.clickOnButton("View Location");
        solo.waitForActivity("DisplayMap.class");
        solo.assertCurrentActivity("Wrong acticity", DisplayMap.class);
        ElasticSearchController.deleteTask DeTask = new ElasticSearchController.deleteTask();
        DeTask.execute(place.getId());
        solo.sleep(5000);
    }

    public void testNoPhotos(){
        if (checkTask(place) == false){
            postTask(place);
        }
        goToViewRequeestedTask();
        //existing task posted by another user with description "update" that has been bid on by the user "qwerty123"
        solo.enterText((EditText) solo.getView(R.id.search), place.getDescription());
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.waitForText(place.getTitle());
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText(place.getTitle());
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
        solo.clickOnButton("ViewPhoto");
        solo.waitForText("No photos to show");
        assertTrue(solo.searchText("No photos to show"));
        ElasticSearchController.deleteTask DeTask = new ElasticSearchController.deleteTask();
        DeTask.execute(place.getId());
        solo.sleep(5000);
    }

    public void testPhotos(){
        if (checkTask(photo) == false){
            //TODO add photo
            postTask(photo);

        }
        goToViewRequeestedTask();
        //existing task posted by another user with description "photo" and photo
        solo.enterText((EditText) solo.getView(R.id.search), photo.getDescription());
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.waitForText(photo.getTitle());
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText(photo.getTitle());
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
        solo.clickOnButton("ViewPhoto");
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