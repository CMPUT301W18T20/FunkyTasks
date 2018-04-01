/**
 * Bid
 *
 * Version 1.0.0
 *
 * Created by Jimi on 2018-02-21.
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import io.searchbox.annotations.JestId;

/**
 * This is a constructor for all the bids in the project.
 * It serves as a template for a bid and its information and ensures each bid has the same format
 * throughout the project.
 */
public class Bid {

    //title,bid amount,bidder
    private String bidder; // username of the bidder
    private String requester;
    private double amount;
    private String taskID;
    private String status;
    private String[] statuses={"","accepted","declined"};


    @JestId
    private String id;

    //constructor for bid

    /**
     * The Bid class stores all the bids in our project and ensures they all have the same
     * format and attributes. An instance of the bid class is instantiated for every bid
     * that is placed in the project.
     *
     * @param bidder a string representing the user that bidded on the task
     * @param requester a string representing the user that posted the task
     * @param amount a double representing the currency amount of the bid
     * @param taskID a string representing the unique task ID of the task the bid was placed on
     */
    public Bid(String bidder, String requester, Double amount, String taskID) {
        this.bidder = bidder;
        this.requester = requester;
        this.amount = amount;
        this.taskID = taskID;
        this.status = statuses[0];
    }

    /**
     * Sets the ID of the bid
     *
     * @param newId a string representing the ID of the bid
     */
    public void setId(String newId) {
        this.id = newId;
    }

    /**
     * Returns the ID of the bid
     *
     * @return a string representing the ID of the bid
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the ID of the task on which the bid was placed
     *
     * @return a string representing the ID of the task on which the bid was placed
     */
    public String getTaskID() {
        return taskID;
    }

    /**
     * Assigns a task ID to the bid so the bid and task can be associated
     *
     * @param taskID a string represents the task ID
     */
    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }


    //methods for bid

    /**
     * Returns the amount of money the bid was placed at
     *
     * @return a double representing the amount of the it
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the bid
     * @param amount a double representing the amount of the bid
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Returns the user that bidded on the task
     *
     * @return a string representing the user who bidded on the task
     */
    public String getBidder() {
        return bidder;
    }

    /**
     * Sets the bidder username
     *
     * @param bidder a string representing the username of the user who placed the bid
     */
    public void setBidder(String bidder) {
        this.bidder = bidder;
    }

    /**
     * Returns the user who posted the task to the app
     *
     * @return a string representing the user who posted the task
     */
    public String getRequester() {
        return requester;
    }

    /**
     * Sets the requester as the user who posted the task to the app
     *
     * @param requester a string representing the user that posted the task
     */
    public void setRequester(String requester) {
        this.requester = requester;
    }
    /**
     * Sets the Bid of a task to "accepted", which indicates that the task has been assigned.
     */
    public void setAccepted(){
        this.status=statuses[1];
    }
    /**
     * Sets the Bid of a task to "declined".
     */
    public void setDeclined(){
        this.status=statuses[2];
    }
    public String getStatus(){
        return this.status;
    }
    public void setStatus(){
        this.status=statuses[0];
    }


}
