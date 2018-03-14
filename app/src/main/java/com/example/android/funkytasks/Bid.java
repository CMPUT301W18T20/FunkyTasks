package com.example.android.funkytasks;

/**
 * Created by jimi on 2018-02-21.
 */

public class Bid {

    //title,bid amount,bidder
    private String bidder; // username of the bidder
    private double amount;
    private String taskID;



    //constracutor for bid
    public Bid(String bidder,Double amount, String taskID){
        this.bidder=bidder;
        this.amount=amount;
        this.taskID = taskID;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
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
