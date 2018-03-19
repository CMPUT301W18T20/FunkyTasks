package com.example.android.funkytasks;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by fc1 on 2018-02-21.
 */
public class BidTest {
    //private User bidder = new User("Jim", "jim@ualberta.ca", "1112220000");

    private Bid bid = new Bid("Jim","Someone", 10.0, "1");

    @Test
    public void testSetID(){
        bid.setId("2");
        assertEquals("2", bid.getId());
        bid.setId("1");
    }
    @Test
    public void testGetID(){
        bid.setId("1");
        assertEquals("1", bid.getId());
    }
    @Test
    public void testSetTaskID(){
        bid.setTaskID("2");
        assertEquals("2", bid.getTaskID());

    }
    @Test
    public void testGetTaskID(){
        bid.setTaskID("2");
        assertEquals("2", bid.getTaskID());
    }

    @Test
    public void getAmount() throws Exception {
        assertEquals(10.0, bid.getAmount(),0);
    }

    @Test
    public void setAmount() throws Exception {
        bid.setAmount(3.0);
        assertEquals(3.0, bid.getAmount(),0);
        bid.setAmount(10.0);
    }

    @Test
    public void getBidder() throws Exception {
        assertEquals("Jim", bid.getBidder());
    }

    @Test
    public void setBidder() throws Exception {
        //User newBidder = new User("Ken", "ken@ualberta.ca", "0001112222");
        String newBidder = "Testing";
        bid.setBidder(newBidder);
        assertEquals(newBidder, bid.getBidder());
    }

    @Test
    public void setRequester(){
        String newRequester = "requester";
        bid.setRequester(newRequester);
        assertEquals(newRequester, bid.getRequester());
    }

    @Test
    public void getRequester(){
        assertEquals("Someone", bid.getRequester());
    }

}