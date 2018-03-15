package com.example.android.funkytasks;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by fc1 on 2018-02-21.
 */
public class BidTest {
    //private User bidder = new User("Jim", "jim@ualberta.ca", "1112220000");

    private Bid bid = new Bid("Jim", 10.0, "1");

    @Test
    public void getAmount() throws Exception {
        assertEquals(10.0, bid.getAmount(),0);
    }

    @Test
    public void setAmount() throws Exception {
        bid.setAmount(3.0);
        assertEquals(3.0, bid.getAmount(),0);
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

}