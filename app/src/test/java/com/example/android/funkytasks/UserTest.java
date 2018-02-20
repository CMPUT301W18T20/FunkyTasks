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



    @Test
    public void testAddTask(){
        User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        Task task = new Task("Snow removal", "Remove the snow in my yard", user);
        user.addTask(task);
        ArrayList<Task> tasks = user.getTasks();
        assertTrue(tasks.contains(task));

    }

    @Test
    public void testGetTasks() {
        User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        Task task1 = new Task("Snow removal", "Remove the snow in my yard", user);
        user.addTask(task1);
        Task task2 = new Task("Deliver package", "deliver a package for me.", user);
        user.addTask(task2);
        ArrayList<Task> tasks = user.getTasks();
        Task returned1 = tasks.get(0);
        Task returned2 = tasks.get(1);
        assertEquals(task1, returned1);
        assertEquals(task2, returned2);

    }

    @Test
    public void testDeleteTask(){
        User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        Task task1 = new Task("Snow removal", "Remove the snow in my yard", user);
        user.addTask(task1);
        Task task2 = new Task("Deliver package", "deliver a package for me.", user);
        user.addTask(task2);
        ArrayList<Task> tasks = user.getTasks();
        task1.setDone();
        user.deleteTask();
        assertFalse(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }


    @Test
    public void testGetUsername() {
        User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        assertEquals("Kenw", user.getUsername());

    }

    @Test
    public void testGetEmail()  {
        User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        assertEquals("kenw@ualberta.ca", user.getEmail());
    }

    @Test
    public void testSetEmail()  {
        User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        user.setEmail("kenw@cs.ualberta.ca");
        assertEquals("kenw@cs.ualberta.ca", user.getEmail());
    }

    @Test
    public void testGetPhonenumber(){
        User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        assertEquals("101-001-0011", user.getPhonenumber());
    }

    @Test
    public void testSetPhonenumber() {
        User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        user.setPhonenumber("101-001-1111");
        assertEquals("101-001-1111", user.getPhonenumber());
    }

    @Test
    public void testGetRating() {
        User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        Double rating = 3.0;
        assertEquals(rating, user.getRating());
    }

    @Test
    public void testSetRating() {
        User user = new User("Kenw", "kenw@ualberta.ca", "101-001-0011");
        user.setRating(5);
        Double rating = 5.0;
        assertEquals(rating, user.getRating());
    }
}
