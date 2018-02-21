package com.example.android.funkytasks;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;


/**
 * Created by jimi on 2018-02-20.
 */
public class TaskTest extends TestCase{


    private String inputTitle = "FunkyClass";
    private String inputDescription = "make Ken happy";
    private String inputStatus = "requested";
    private User inputRequester =new User("Ken", "ken@ualberta.ca", "7806668888");
    private User bidder1 =new User("Jimi", "ken@ualberta.ca", "7806668888");
    private ArrayList<bid> bids = new ArrayList<bid>();
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
        User newUser= new User("Jimi","jimi@gmail.com","00000000");
        test.setRequester(newUser);
        User output = test.getRequester();
        assertEquals(newUser,output);
    }

    @Test
    public void testGetRequester() throws Exception {
        User output = test.getRequester();
        assertEquals(output,inputRequester);

    }

    @Test
    public void testGetBids() throws Exception{
        bid newBid=new bid(bidder1,10.0);
        test.addBid(newBid);
        bids.add(newBid);
        assertEquals(test.getBids(),bids);

    }

    @Test
    public void testSetBids() throws Exception{

    }
    @Test
    public void testAddBids() throws Exception{

        bid newBid=new bid(bidder1,10.0);
        bids.add(newBid);
        test.addBid(newBid);
        assertEquals(test.getBids(),bids);


    }



}