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
 * Created by ${fc1} on 2018-03-17.
 */

//before testing
// 1.create accounts with username "testing1","testing2","testing3"
//2.create four tasks by "testing1", with title"test"(with photp),"testEdit","testDelete","testAccept","testDone","testReassign1","testReassign2"
//3.place bids by "testing2" on every tasks except "testEdit"
//4. place a bid by "testing3" on "testReassign2"
//5. accept bids with "testing1" on "testDone","testReassign1","testReassign2"


public class DashboardRequestedTaskTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public DashboardRequestedTaskTest(){
        super(LoginActivity.class);
    }
    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        goToDashboardRequestedTask();
    }


    //test for US 01.02.01
    public void goToDashboardRequestedTask(){
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "testing1");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnView(solo.getView(R.id.fab));
        solo.waitForActivity("MyTasksActivity.class");
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
        solo.clickOnActionBarItem(R.id.tabItem);

    }
    //test for US 05.05.01
    public void testRequestorViewBids(){
        assertTrue(solo.searchText("test"));
        solo.clickOnText("test");
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
        solo.waitForText("testing2");
        solo.clickOnText("testing2");
    }

    //test for US 01.03.01
    public void testEdit(){
        assertTrue(solo.searchText("testEdit"));
        solo.clickOnText("testEdit");
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
        solo.clickOnView(solo.getView(R.id.editRequestedTask));
        solo.waitForActivity("EditDashboardRequestedTask.class");
        solo.assertCurrentActivity("Wrong activity", EditDashboardRequestedTask.class);
    }
    //test for US 05.07.01
    public void testDeclineBid(){
        assertTrue(solo.searchText("test"));
        solo.clickOnText("test");
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
        solo.waitForText("testing2");
        solo.clickOnText("testing2");
        solo.waitForText("DECLINE");
        solo.clickOnText("DECLINE");
        assertTrue(solo.searchText("declined"));
    }
    //test for US 05.06.01
    public void testAcceptBid(){
        assertTrue(solo.searchText("testAccept"));
        solo.clickOnText("testAccept");
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
        assertTrue(solo.searchText("testing2"));
        solo.clickOnText("testing2");
        solo.waitForText("ACCEPT");
        solo.clickOnText("ACCEPT");
        assertTrue(solo.searchText("accepted"));
        assertTrue(solo.searchText("assigned"));
    }


    //test for US 01.04.01
    public void testDelete(){
        assertTrue(solo.searchText("testDelete"));
        solo.clickOnText("testDelete");
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
        solo.clickOnView(solo.getView(R.id.deleteActionBar));
        solo.assertCurrentActivity("Wrong activity", MyTasksActivity.class);
        assertFalse(solo.searchText("testDelete"));
    }

//    //test for US 07.01.01
    public void testSetTaskDone(){
        assertTrue(solo.searchText("testDone"));
        solo.clickOnText("testDone");
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
        solo.waitForText("FINISH TASK");
        solo.clickOnText("FINISH TASK");
    }
    //test for US 07.02.01
    public void testReassignToRequested(){
        assertTrue(solo.searchText("testReassign1"));
        solo.clickOnText("testReassign1");
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
        solo.waitForText("Reassign");
        solo.clickOnText("Reassign");
        assertTrue(solo.searchText("requested"));

    }
    //test for US 07.03.01
    public void testReassignToBidded(){
        assertTrue(solo.searchText("testReassign2"));
        solo.clickOnText("testReassign2");
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
        solo.waitForText("Reassign");
        solo.clickOnText("Reassign");
        assertTrue(solo.searchText("bidded"));
    }

    // test for viewing photo
    public void testViewPhoto(){
        assertTrue(solo.searchText("test"));
        solo.clickOnText("test");
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
        solo.clickOnView(solo.getView(R.id.picdetails));
        solo.waitForActivity("ImageDetails.class");
        solo.assertCurrentActivity("Wrong activity", ImageDetails.class);
    }
    public void testNoPhoto(){
        assertTrue(solo.searchText("testEdit"));
        solo.clickOnText("testEdit");
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
        solo.clickOnView(solo.getView(R.id.picdetails));
        solo.assertCurrentActivity("Wrong activity", DashboardRequestedTask.class);
    }



    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}