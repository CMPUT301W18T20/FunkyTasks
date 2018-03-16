package com.example.android.funkytasks;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

import static org.junit.Assert.*;

/**
 * Created by ${fc1} on 2018-03-15.
 */
public class EditProfileActivityTest extends ActivityInstrumentationTestCase2{
        private Solo solo;
        private User user;

    public EditProfileActivityTest(){
        super(com.example.android.funkytasks.LoginActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void goToEditProfile() throws Exception{
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editLoginName), "qwerty123");
        solo.clickOnButton("Login");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnView(solo.getView(R.id.profileActionBar));
        solo.waitForActivity("EditProfileActivity.class");
        solo.assertCurrentActivity("Wrong activity", EditProfileActivity.class);

    }

    public void testUpdateProfile() throws Exception{
        goToEditProfile();
        solo.assertCurrentActivity("Wrong activity", EditProfileActivity.class);
        String newPhone = "1112221111";
        String newEmail = "123@EMAIL.COM";
        solo.clearEditText((EditText) solo.getView(R.id.EditPhone));
        solo.clearEditText((EditText) solo.getView(R.id.EditEmail));
        solo.enterText((EditText) solo.getView(R.id.EditPhone), newPhone);
        solo.enterText((EditText) solo.getView(R.id.EditEmail), newEmail);
        solo.clickOnButton("DONE");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
        solo.clickOnView(solo.getView(R.id.profileActionBar));
        solo.waitForActivity("EditProfileActivity.class");
        //check changes
        assertTrue(solo.searchText(newPhone));
        assertTrue(solo.searchText(newEmail));
        //RESTORE CHANGES
        solo.clearEditText((EditText) solo.getView(R.id.EditPhone));
        solo.clearEditText((EditText) solo.getView(R.id.EditEmail));
        solo.enterText((EditText) solo.getView(R.id.EditPhone), "2221112222");
        solo.enterText((EditText) solo.getView(R.id.EditEmail), "321@email.com");
        solo.clickOnButton("DONE");
        solo.waitForActivity("MainMenuActivity.class");
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}