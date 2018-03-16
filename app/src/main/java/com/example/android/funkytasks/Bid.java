package com.example.android.funkytasks;

import io.searchbox.annotations.JestId;

/**
 * Created by jimi on 2018-02-21.
 */

public class Bid {

    //title,bid amount,bidder
    private String bidder; // username of the bidder
    private String requester;
    private double amount;
    private String taskID;


    @JestId
    private String id;

    //constracutor for bid
    public Bid(String bidder, String requester, Double amount, String taskID) {
        this.bidder = bidder;
        this.requester = requester;
        this.amount = amount;
        this.taskID = taskID;
    }

    public void setId(String newId) {
        this.id = newId;
    }

    public String getId() {
        return this.id;
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

    public String getRequester() {
        return bidder;
    }

    public void setRequester(String requester) {
        this.bidder = bidder;
    }


}
