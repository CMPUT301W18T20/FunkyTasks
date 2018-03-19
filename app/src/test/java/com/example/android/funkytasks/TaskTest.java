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
    private String inputRequester ="Ken";
//    private User bidder1 =new User("Jimi", "jimi@ualberta.ca", "7806668889");
//    private User bidder2 = new User ("Jim", "jim@ualberta.ca", "1112220000");
//    private ArrayList<Bid> bids = new ArrayList<Bid>();
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
        String expected = "assigned";
        test.setAssigned();
        String output = test.getStatus();
        assertEquals(output,expected);
    }

    @Test
    public void testSetRequested() throws Exception{
        test.setRequested();
        assertEquals("requested",test.getStatus());
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
/*
    @Test
    public void testSetRequester() throws Exception {
        String newUser = "Jimi";
        //User newUser= new User("Jimi","jimi@gmail.com","00000000");
        test.setRequester(newUser);
        User output = test.getRequester();
        assertEquals(newUser,output);
    }
*/
    @Test
    public void testGetRequester() throws Exception {
        String output = test.getRequester();
        assertEquals(output,inputRequester);

    }

    @Test
    public void testSetID() throws Exception{
        String newID = "123";
        test.setId(newID);
        assertEquals(newID,test.getId());
    }

    @Test
    public void testGetID(){
        String ID = "123";
        assertEquals(null, test.getId());
        test.setId(ID);
        assertEquals(ID, test.getId());
    }

    @Test
    public void testSetProvider(){
        String newProv = "testing";
        test.setProvider(newProv);
        assertEquals(newProv, test.getProvider());
    }

    @Test
    public void testGetProvider(){
        test.setProvider("TEST");
        assertEquals("TEST", test.getProvider());
    }

    //not needed anymore
/*
    @Test
    public void testGetBids() throws Exception{
        Bid newBid=new Bid(bidder1,10.0);
        test.addBid(newBid);
        bids.add(newBid);
        assertEquals(test.getBids(),bids);

    }


    @Test
    public void testAddBids() throws Exception{

        Bid newBid=new Bid(bidder1,10.0);
        bids.add(newBid);
        test.addBid(newBid);
        assertEquals(test.getBids(),bids);


    }

    @Test
    public void testGetLowestBid() throws  Exception{
        Bid bid1 = new Bid(bidder1, 10.0);
        Bid bid2 = new Bid(bidder2, 3.0);
        test.addBid(bid1);
        test.addBid(bid2);
        assertEquals(bid2.getAmount(),test.getLowestBid());
    }
*/

}