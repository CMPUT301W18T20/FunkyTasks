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
 * Created by ${fc1} on 2018-03-16.
 */
public class EditDashboardRequestedTaskTest extends ActivityInstrumentationTestCase2<LoginActivity>{
        private Solo solo;
        private Task newTask;
        private User user;

    public EditDashboardRequestedTaskTest(){
        super(LoginActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(),getActivity());
    }

    public void addTask(){
        newTask = new Task("test1", "test description", "qwerty123");
        ElasticSearchController.PostTask postTask = new ElasticSearchController.PostTask();
        postTask.execute(newTask);
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

    public void goToEditTask(){

        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        addUser();
        addTask();
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "qwerty123");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnView(solo.getView(R.id.fab));
        solo.waitForActivity("MyTasksActivity.class");
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
        solo.clickOnActionBarItem(R.id.tabItem);
        solo.clickOnText("test1");
        solo.clickOnView(solo.getView(R.id.editRequestedTask));
    }



    public void testClickSave() throws Exception{
        goToEditTask();
        solo.assertCurrentActivity("Wrong activity", EditDashboardRequestedTask.class);
        String newTitle = "New Title";
        String newDes = "New Description";
        solo.clearEditText((EditText) solo.getView(R.id.editTitle));
        solo.clearEditText((EditText) solo.getView(R.id.editDescription));
        solo.enterText((EditText) solo.getView(R.id.editTitle), newTitle);
        solo.enterText((EditText) solo.getView(R.id.editDescription), newDes);
        solo.clickOnText("Save");
        solo.waitForActivity("DashboardRequestedTask.class");
        assertTrue(solo.searchText(newTitle));
        assertTrue(solo.searchText(newDes));
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
    }



    @After
    public void tearDown() throws Exception {
        ElasticSearchController.deleteTask DeTask = new ElasticSearchController.deleteTask();
        DeTask.execute(newTask.getId());
        solo.finishOpenedActivities();
    }

}