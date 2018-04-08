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
public class RateActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private Task result=new Task("rateTest","rateTest","qwerty123");

    public RateActivityTest(){
            super(LoginActivity.class);
        }
    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void PostTask(){
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "qwerty123");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnButton("create a funky task");
        solo.waitForActivity("CreateTaskActivity.class");
        solo.assertCurrentActivity("Wrong activity",CreateTaskActivity.class);
        solo.enterText((EditText) solo.getView(R.id.AddTitle), result.getTitle());
        solo.enterText((EditText) solo.getView(R.id.AddDescription), result.getDescription());
        solo.clickOnView(solo.getView(R.id.fab));
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.sleep(5000);
        solo.goBack();

    }

    public void bidOnTask(){
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "monica11");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnButton("solve a funky task");
        solo.waitForActivity("SolveTaskActivity.class");
        solo.enterText((EditText) solo.getView(R.id.search), result.getDescription());
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.clickOnText(result.getDescription());
        solo.clearEditText((EditText) solo.getView(R.id.search));
        solo.clickOnText(result.getTitle());
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

    public void testRate(){
        PostTask();
        bidOnTask();
    }
    @After
    public void tearDown() throws Exception {
    }

}