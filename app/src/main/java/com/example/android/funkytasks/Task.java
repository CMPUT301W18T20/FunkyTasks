package com.example.android.funkytasks;

import java.util.ArrayList;

/**
 * Created by MonicaB on 2018-02-20.
 */

public class Task {
    // title, description, status, lowest bid,

    private String title;
    private String description;
    private String requester;
    private String status;
    private String[] statuses={"requested","bidded","asigned","done"};
    private ArrayList<User> bidders;


    Task(String title, String description,String requester){
        // constructor for task object
        this.title = title;
        this.description = description;
        this.requester = requester;
        this.status = statuses[0];
        bidders = new ArrayList<User>();
    }

    public ArrayList<User> getBidders() {
        return bidders;
    }

    public void setBidders(ArrayList<User> bidders) {
        this.bidders = bidders;
    }

    public void addBidders(User newBidder){
        bidders.add(newBidder);
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
    public void setRequester(String newRequester){
        this.requester=newRequester;
    }
    public String getRequester(){
        return this.requester;
    }

}
