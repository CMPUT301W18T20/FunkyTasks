package com.example.android.funkytasks;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by ${fc1} on 2018-04-08.
 */
public class DisplayMapTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public DisplayMapTest(){
        super(LoginActivity.class);
    }
    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        //qwerty123 is an existing user
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "qwerty123");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnButton("create a funky task");
        solo.waitForActivity("CreateTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity",CreateTaskActivity.class);
        solo.clickOnButton("Add Location");
        solo.waitForActivity("DisplayMap.class");
        solo.assertCurrentActivity("Wrong activity", DisplayMap.class);
    }

    public void testSave(){
        solo.clickOnButton("Save");
        solo.waitForActivity("CreateTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity",CreateTaskActivity.class);
    }
    @After
    public void tearDown() throws Exception {
    }

}