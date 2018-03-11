package com.example.android.funkytasks;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;
import junit.framework.TestCase;

/**
 * Created by ${fc1} on 2018-03-11.
 */

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public LoginActivityTest(){
        super(LoginActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void setSolo() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testSignUp(){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnText("Don't already have an account?");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
    }

    public void testMainMenu(){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "IntentTesting");
        solo.clickOnButton(R.id.button);
        solo.assertCurrentActivity("Wrong Activity", MainMenuActivity.class);
    }

}
