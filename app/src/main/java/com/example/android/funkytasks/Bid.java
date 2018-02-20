package com.example.android.funkytasks;

/**
 * Created by jimi on 2018-02-20.
 */


public class Bid {

    private String title;
    private String bidder;
    private double amount;

    Bid(String title,String bidder,double amount){
        this.title = title;
        this.bidder = bidder;
        this.amount = amount;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setBidder(String newBidder){
        this.bidder=newBidder;
    }
    public String getBidder(){
        return this.bidder;
    }
    public void setAmount(double newAmount){
        this.amount=newAmount;
    }
    public double getAmount(){
        return this.amount;
    }

}
