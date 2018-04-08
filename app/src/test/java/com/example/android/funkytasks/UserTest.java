package com.example.android.funkytasks;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ArrayAdapter;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fc1 on 2018-02-20.
 */

public class UserTest extends TestCase {

    private User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");

    @Test
    public void testGetUsername() {
        //User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        assertEquals("Kenw", user.getUsername());

    }

    @Test
    public void testGetEmail()  {
        //User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        assertEquals("kenw@ualberta.ca", user.getEmail());
    }

    @Test
    public void testSetEmail()  {
        //User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        user.setEmail("kenw@cs.ualberta.ca");
        assertEquals("kenw@cs.ualberta.ca", user.getEmail());
    }

    @Test
    public void testGetPhonenumber(){
        //User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        assertEquals("101-001-0011", user.getPhonenumber());
    }

    @Test
    public void testSetPhonenumber() {
        //User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        user.setPhonenumber("101-001-1111");
        assertEquals("101-001-1111", user.getPhonenumber());
    }

    @Test
    public void testRating() {
        Double rating1 = 5.0;
        Double rating2 = 10.0;
        //test addRatingToList
        user.addRatingToList(rating1);
        user.addRatingToList(rating2);
        Double avg = (rating1 + rating2 )/ 2;
        //test setRating
        user.setRating();
        //test getRating and calculateAverageRating
        assertEquals(avg, user.getRating());
    }


    @Test
    public void testSetID(){
        String newID = "123";
        user.setId(newID);
        assertEquals(newID, user.getId());
    }

    @Test
    public void testGetID(){
        String ID = "123";
        assertEquals(null, user.getId());
        user.setId(ID);
        assertEquals(ID, user.getId());
    }

}
