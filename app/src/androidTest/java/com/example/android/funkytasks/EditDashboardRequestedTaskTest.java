package com.example.android.funkytasks;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by ${fc1} on 2018-03-16.
 */
public class EditDashboardRequestedTaskTest extends ActivityInstrumentationTestCase2<LoginActivity>{
        private Solo solo;


    public EditDashboardRequestedTaskTest(){
        super(LoginActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(),getActivity());
    }

    public void goToEditTask(){
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "qwerty123");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnView(solo.getView(R.id.fab));
        solo.waitForActivity("TaskDashboardActivity.class");
        solo.assertCurrentActivity("Wrong activity", TaskDashboardActivity.class);
        solo.clickInList(1);
        //This is not done
    }

    public void testClickSave() throws Exception{
        solo.assertCurrentActivity("Wrong activity", EditDashboardRequestedTask.class);
        solo.clickOnButton(R.id.buttonDone);
        solo.waitForActivity("DashboardRequestedTask.class");
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
    }



    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}