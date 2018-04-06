/**
 * User
 *
 * Version 1.0.0
 *
 * Created by MonicaB on 2018-02-20.
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.searchbox.annotations.JestId;


@SuppressWarnings("serial")
/**
 * This object class holds a user and dictates how users should be stored in memory.
 */
public class User implements Serializable{
    // unique username, email, phonenumber, rating
    private String username;
    private String email;
    private String phonenumber;
    private double averagerating;
    private List<Double> ratingList;


    @JestId
    private String id;

    /**
     * A user object that holds its username, email, and phone number
     *
     * @param username a string representing a user's username
     * @param email a string representing a user's email address
     * @param phonenumber a string representing a user's phone number
     */
    User(String username, String email, String phonenumber){
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
        this.averagerating = 3;
        this.ratingList = new ArrayList<Double>();
    }

    /**
     * Sets the user's ID to the string provided
     *
     * @param newId a string representing the user's new ID
     */
    public void setId(String newId){
        this.id = newId;
    }

    /**
     * Returns the user's ID
     *
     * @return a string representing the user's ID
     */
    public String getId(){
        return this.id;
    }

    /**
     * Adds a requested task to the user's list of requested tasks. This function is
     * not currently being used.
     *
     * @param newTaskID a string holding the new tasks ID that is to be added to the list of tasks
     */




    /**
     * Returns the user's username
     *
     * @return a string representing the user's username
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Returns the user's email address
     *
     * @return a string representing a user's email
     */
    public String getEmail(){
        return this.email;
    }

    /**
     * Sets the user's email to the email string provided
     *
     * @param newEmail a string representing the user's new email
     */
    public void setEmail(String newEmail){
        this.email = newEmail;
    }

    /**
     * Returns the user's phone number
     *
     * @return a string representing the user's phone number
     */
    public String getPhonenumber(){
        return this.phonenumber;
    }

    /**
     * Sets the user's phone number to the string provided
     *
     * @param newNumber a string representing the user's new phone number
     */
    public void setPhonenumber(String newNumber){
        this.phonenumber = newNumber;
    }

    /**
     * Returns the user's rating
     *
     * @return a double representing the user's rating
     */
    public double getRating(){
        return this.averagerating;
    }

    /**
     * Sets the user's rating to the double provided after calculation
     */
    public void setRating(){
        this.calculateAverageRating(ratingList);
    }

    /**
     * adds a new rating to the ratingList, which is used to calculate the average rating
     * @param newRating input rating from client
     */

    public void addRatingToList(double newRating){
        this.ratingList.add(newRating);
    }

    /**
     * calculates a straight average of ratings given to the user.
     * @param ratingList a list of ratings given to the user from clients
     */

    private void calculateAverageRating(List<Double> ratingList){
        int i = 0;
        double sum = 0;
        for(double rating : ratingList){
            sum = sum + rating;
            i = i + 1;
        }
        this.averagerating = sum/i;
    }

}
