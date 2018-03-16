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
    private User user;

    private User testingUser = new User("IntentTesting", "1234567890", "IT@ualbertac.ca");

    public SignUpActivityTest(){
        super(com.example.android.funkytasks.SignUpActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();

    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    //signing up an existing user
    public void testFailedSignUp(){
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        //existing user "qwerty123"
        Log.e("Hellooooo", "why you failling");
        solo.enterText((EditText) solo.getView(R.id.editAddUsername), "qwerty123");
        solo.enterText((EditText) solo.getView(R.id.editAddPhone), "2221112222");
        solo.enterText((EditText) solo.getView(R.id.editAddEmail), "321@email.com");
        View fab = getActivity().findViewById(R.id.fabSignUp);
        solo.clickOnView(fab);
        solo.waitForText("Username Taken");
        assertTrue(solo.searchText("Username Taken"));
        solo.assertCurrentActivity("Wrong activity", SignUpActivity.class);

    }

    public void testSignUp() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editAddUsername), testingUser.getUsername());
        solo.enterText((EditText) solo.getView(R.id.editAddPhone), testingUser.getPhonenumber());
        solo.enterText((EditText) solo.getView(R.id.editAddEmail), testingUser.getEmail());
        View fab = getActivity().findViewById(R.id.fabSignUp);
        solo.clickOnView(fab);
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);

    }

    @Override
    public void tearDown() throws Exception{
        ElasticSearchController.deleteUser DeUser = new ElasticSearchController.deleteUser();
        DeUser.execute(testingUser.getId());
        solo.finishOpenedActivities();

    }
}
