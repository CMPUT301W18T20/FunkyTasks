package com.example.android.funkytasks;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.view.View;

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
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testClickAdd(){
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        User testingUser = new User("IntentTesting", "1234567890", "IT@ualbertac.ca");
        solo.enterText((EditText) solo.getView(R.id.editAddUsername), "IntentTesting");
        solo.enterText((EditText) solo.getView(R.id.editAddPhone), "1234567890");
        solo.enterText((EditText) solo.getView(R.id.editAddEmail), "IT@ualberta.ca");
        View fab = getActivity().findViewById(R.id.fab);
        solo.clickOnView(fab);

        //Get user from the E.S
        String username = testingUser.getUsername();
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
        //see if user is added to E.S
        assertEquals(testingUser, user);
        solo.waitForActivity(LoginActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

    }
}
