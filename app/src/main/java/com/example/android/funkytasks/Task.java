/**
 *
 * Task
 *
 * Version 1.0.0
 *
 * Created by MonicaB on 2018-02-20.
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 *
 */

package com.example.android.funkytasks;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import io.searchbox.annotations.JestId;

import static java.util.Collections.*;



@SuppressWarnings("serial")

public class Task implements Serializable{
    // title, description, status, lowest bid,

    private String title;
    private String description;
    private String requester;
    private String provider;
    private String offlineID;
    private String status;
    private String[] statuses={"requested","bidded","assigned","done"};
    private ArrayList<String> images;

    @JestId
    private String id;


    /**
     *
     * The Task class is a model for all the tasks in our project. It stores each task with its
     * title (max 30 characters), description (max 300 characters), requester, provider, and status.
     * This class is used throughout the project to store and access all the tasks in the app.
     *
     * @param title the string title of the task as provided by the requester
     * @param description the string description of the task as provided by the requester
     * @param requester the string name of the user who posted the task
     */
    Task(String title, String description, String requester){
        // constructor for task object
        this.title = title;
        this.description = description;
        this.requester = requester; // username of the user who requests the task
        this.provider = null; // username of user who is solving the task
        this.status = statuses[0];
        this.images = new ArrayList<String>();
    }


    /**
     * This function sets a new ID for a given task.
     *
     * @param newId a string representing the ID of a given task
     */
    public void setId(String newId){
        this.id = newId;
    }

    /**
     * Returns the ID of a given task
     *
     * @return the task ID
     */
    public String getId(){
        return this.id;
    }

    public void setOfflineId(String newId){this.offlineID = newId;}

    public String getOfflineId() {return this.offlineID;}

    /**
     * Returns the string name of the user who will complete the task
     *
     * @return the task provider
     */
    public String getProvider() { return provider; }

    /**
     * Sets the provider for a given task
     * @param provider the string name of the task provider as provided by the user
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * Returns the title of the task
     *
     * @return the string title of a given task as provided by the user
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * Sets the title of the task to the string provided by the user
     *
     * @param newTitle the string title of the task
     */
    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    /**
     * Returns the description of the task
     *
     * @return a string representing the description of the task
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * Sets the description of a task to the string provided by the user
     *
     * @param newDescription a string representing the description
     */
    public void setDescription(String newDescription){
        this.description = newDescription;
    }

    /**
     * Sets the status of a task to "Bidded", which indicates that one or more user
     * has placed a bid on the task, but the requester has not yet accepted a bid.
     */
    public void setBidded(){
        this.status = statuses[1];
    }

    /**
     * Sets the status of a task to "Requested", which indicates that a task has been posted
     * to the app and there are no bids placed on it.
     */
    public void setRequested(){
        this.status = statuses[0];
    }

    /**
     * Sets the status of a task to "Assigned", which indicates that a user has bidded on the task
     * and the task requester has accepted said user's bid. The status of "Assigned" means that the
     * task should be completed soon.
     */
    public void setAssigned(){
        this.status = statuses[2];
    }

    /**
     * Sets the status of a task to "Done", which indicates that the task has been completed.
     */
    public void setDone(){
        this.status = statuses[3];
    }

    /**
     * Returns the current status of the task.
     *
     * @return a string representing the status of the task
     */
    public String getStatus(){
        return this.status;
    }

    /**
     * Returns the requester of a particular task.
     *
     * @return a string representing the name of the user that has requested the task
     */
    public String getRequester(){
        return this.requester;
    }


    /**
     * Returns the array list of images associated with the task
     *
     * @return an arraylist bitmap containing the image attached to the task
     */
    public ArrayList<String> getImages() {
        return images;
    }

    /**
     * Adds the image of the task to the task's current list of photos
     * @param image a new image to attach to the task's current list of images
     */

    public void addImage(String image) {
        images.add(image);
    }

    /**
     * Replaces the task's list of photos with a new list(newImages)
     * @param newImages is a arraylist containing one or more images to associate with the task
     */

    public void setImagesList(ArrayList<String> newImages){
        this.images = newImages;
    }
}
