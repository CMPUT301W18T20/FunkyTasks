package com.example.android.funkytasks;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;


/**
 * Created by ${fc1} on 2018-03-11.
 */

public class SignUpActivityTest extends ActivityInstrumentationTestCase2<SignUpActivity> {
    private Solo solo;

    public SignUpActivityTest(){
        super(com.example.android.funkytasks.SignUpActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testClickAdd(){
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        User testingUser = new User("IntentTesting", "1234567890", "IT@ualbertac.ca");
        solo.enterText((EditText) solo.getView(R.id.editAddUsername), "IntentTesting");
        solo.enterText((EditText) solo.getView(R.id.editAddPhone), "1234567890");
        solo.enterText((EditText) solo.getView(R.id.editAddEmail), "IT@ualbertac.ca");
        String username = testingUser.getUsername();
        solo.clickOnButton(R.id.fab);

        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(username);

        User user;
        try {
            user = getUser.get();
            Log.e("Got the username: ", user.getUsername());


        } catch (Exception e) {
            Log.e("Error", "We arnt getting the user");
            return;
        }

        assertEquals(testingUser, user);
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

    }
}
