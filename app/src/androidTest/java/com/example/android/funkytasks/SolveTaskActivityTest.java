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
public class SolveTaskActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private User user;
    private Task newTask;


    public SolveTaskActivityTest(){

        super(LoginActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());

    }
/*
    public void addUser(){
        //user = new User("SolveTaskTest","123@email.com", "1112221111");
        //ElasticSearchController.PostUser postUser = new ElasticSearchController.PostUser();
        //postUser.execute(user);
    }

    public void addTask(){
        //newTask = new Task("Test Title", "pen", user.getUsername());
        //ElasticSearchController.PostTask postTask = new ElasticSearchController.PostTask();
        //postTask.execute(newTask);
    }*/

    public void goToSolveTask(){
        //addUser();
        //addTask();
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "qwerty123");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnButton("solve a funky task");
        solo.waitForActivity("SolveTaskActivity.class");

    }

    public void testSolveTask(){
        goToSolveTask();
        solo.assertCurrentActivity("Wrong activity",SolveTaskActivity.class);
        solo.enterText((EditText) solo.getView(R.id.search), "juice");
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.waitForText("test");
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText("test");
        solo.waitForActivity("ViewRequestorTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity", ViewRequestorTaskActivity.class);
    }


    @After
    public void tearDown() throws Exception {
        //delete task
        //ElasticSearchController.deleteTask DeTask = new ElasticSearchController.deleteTask();
        //DeTask.execute(newTask.getId());
        //delete user
        //ElasticSearchController.deleteUser DeUser = new ElasticSearchController.deleteUser();
        //DeUser.execute(user.getId());
        solo.finishOpenedActivities();
    }

}