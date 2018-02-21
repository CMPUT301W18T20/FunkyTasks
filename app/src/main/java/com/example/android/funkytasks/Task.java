package com.example.android.funkytasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import io.searchbox.annotations.JestId;

import static java.util.Collections.*;

/**
 * Created by MonicaB on 2018-02-20.
 */

public class Task {
    // title, description, status, lowest bid,

    private String title;
    private String description;
    private User requester;
    private String status;
    private String[] statuses={"requested","bidded","asigned","done"};
    private ArrayList<bid> bids;
    private ArrayList<Integer> bidsAmount;

    @JestId
    private String id;


    Task(String title, String description,User requester){
        // constructor for task object
        this.title = title;
        this.description = description;
        this.requester = requester;
        this.status = statuses[0];
        bids = new ArrayList<bid>();
    }


    //public int getLowestBid(){
        //for (int i = 0; i < bids.size(); i++){
            // amount = bids.
        //}

    //}
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public ArrayList<bid> getBids() {
        return bids;
    }
    public void setBid(ArrayList<bid> bidders) {
        this.bids = bids;
    }
    public void addBid(bid newBidder){
        bids.add(newBidder);
    }
    public String getTitle(){
        return this.title;
    }
    public void setTitle(String newTitle){
        this.title = newTitle;
    }
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String newDescription){
        this.description = newDescription;
    }
    public void setBidded(){
        this.status = statuses[1];
    }
    public void setAsigned(){
        this.status = statuses[2];
    }
    public void setDone(){
        this.status = statuses[3];
    }
    public String getStatus(){
        return this.status;
    }
    public void setRequester(User newRequester){
        this.requester=newRequester;
    }
    public User getRequester(){
        return this.requester;
    }

}
