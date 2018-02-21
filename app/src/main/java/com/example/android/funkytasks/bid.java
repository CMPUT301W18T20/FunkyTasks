package com.example.android.funkytasks;

/**
 * Created by jimi on 2018-02-21.
 */

public class bid {

    //title,bid amount,bidder
    private String bidder;
    private double amount;

    //constracutor for bid
    bid(String bidder,Double amount){
        this.bidder=bidder;
        this.amount=amount;
    }

    //methods for bid
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getBidder() {
        return bidder;
    }

    public void setBidder(String bidder) {
        this.bidder = bidder;
    }


}
