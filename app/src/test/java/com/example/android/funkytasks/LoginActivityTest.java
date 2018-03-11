package com.example.android.funkytasks;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

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


}
