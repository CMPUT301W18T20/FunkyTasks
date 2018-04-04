package com.example.android.funkytasks;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ArrayAdapter;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

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
    public void testGetRating() {
        //User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        Double rating = 3.0;
        assertEquals(rating, user.getRating());
    }

    @Test
    public void testSetRating() {
        //User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        user.setRating(5);
        Double rating = 5.0;
        assertEquals(rating, user.getRating());
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
