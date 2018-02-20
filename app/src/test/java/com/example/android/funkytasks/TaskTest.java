package com.example.android.funkytasks;

import junit.framework.TestCase;

import org.junit.Test;



/**
 * Created by jimi on 2018-02-20.
 */
public class TaskTest extends TestCase{


    private String inputTitle = "FunkyClass";
    private String inputDescription = "make Ken happy";
    private String inputStatus = "requested";
    private String inputRequester = "Ken";
    private Task test=new Task(inputTitle,inputDescription,inputRequester);


    @Test
    public void testGetTitle() throws Exception {
        String output = test.getTitle();
        assertEquals(output,inputTitle);

    }

    @Test
    public void testSetTitle() throws Exception {
        String input= "Funky";
        test.setTitle(input);
        String output = test.getTitle();
        assertEquals(output,input);

    }

    @Test
    public void testGetDescription() throws Exception {
        String output = test.getDescription();
        assertEquals(output,inputDescription);
    }

    @Test
    public void testSetDescription() throws Exception {
        String input= "make Wang happy";
        test.setDescription(input);
        String output = test.getDescription();
        assertEquals(output,input);

    }

    @Test
    public void testSetBidded() throws Exception {

        String expected = "bidded";
        test.setBidded();
        String output = test.getStatus();
        assertEquals(output,expected);

    }

    @Test
    public void testSetAsigned() throws Exception {

        int input= 1;
        String expected = "asigned";
        test.setAsigned();
        String output = test.getStatus();
        assertEquals(output,expected);
    }

    @Test
    public void testSetDone() throws Exception {

        int input= 1;
        String expected = "done";
        test.setDone();
        String output = test.getStatus();
        assertEquals(output,expected);
    }

    @Test
    public void testGetStatus() throws Exception {
        String output = test.getStatus();
        assertEquals(output,inputStatus);
    }

    @Test
    public void testSetRequester() throws Exception {
        String input="Wang";
        test.setRequester(input);
        String output = test.getRequester();
        assertEquals(input,output);
    }

    @Test
    public void testGetRequester() throws Exception {
        String output = test.getRequester();
        assertEquals(output,inputRequester);

    }

}