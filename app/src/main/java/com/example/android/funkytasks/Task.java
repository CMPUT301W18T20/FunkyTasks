package com.example.android.funkytasks;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import io.searchbox.annotations.JestId;

import static java.util.Collections.*;

/**
 * Created by MonicaB on 2018-02-20.
 */

@SuppressWarnings("serial")
public class Task implements Serializable{
    // title, description, status, lowest bid,

    private String title;
    private String description;
    private String requester;
    private String provider;
    private String status;
    private String[] statuses={"requested","bidded","assigned","done"};
    private Integer numberOfBids;

    @JestId
    private String id;


    Task(String title, String description, String requester, Integer numberOfBids){
        // constructor for task object
        this.title = title;
        this.description = description;
        this.requester = requester; // username of the user who requests the task
        this.provider = null; // username of user who is solving the task
        this.status = statuses[0];
        this.numberOfBids = numberOfBids;
    }

    public void setId(String newId){
        this.id = newId;
    }

    public String getId(){
        return this.id;
    }
  public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
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
    public void setRequested(){
        this.status = statuses[0];
    }
    public void setAssigned(){
        this.status = statuses[2];
    }
    public void setDone(){
        this.status = statuses[3];
    }
    public String getStatus(){
        return this.status;
    }

    public String getRequester(){
        return this.requester;
    }

    public void setNumberOfBids(Integer numberOfBids) { this.numberOfBids = numberOfBids; }

    public Integer getNumberOfBids() { return this.numberOfBids; }

}
