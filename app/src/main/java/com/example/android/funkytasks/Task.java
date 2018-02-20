package com.example.android.funkytasks;

/**
 * Created by MonicaB on 2018-02-20.
 */

public class Task {
    // title, description, status, lowest bid,
    //TODO: figure out how to change bid status
    private String title;
    private String description;

    private String status;
    private double bid;
    private String[] statuses={"requested","bidded","asigned","done"};

    Task(String title, String description){
        // constructor for task object
        this.title = title;
        this.description = description;
        this.status = statuses[0];

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
}
