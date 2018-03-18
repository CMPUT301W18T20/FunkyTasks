/**
 * GlobalVariables
 *
 * Version 1.0.0
 *
 * Created by Atharv on 2/21/2018.
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.app.Application;

import java.util.ArrayList;

/**
 * This class holds global variables for the entire project. We are not currently using this class
 * but we haven't removed all instances of it so it's not yet safe to delete.
 */
public class GlobalVariables extends Application{


    private ArrayList<User> userArrayList = new ArrayList<User>();

    /**
     * Returns the array list of current users
     *
     * @return returns an ArrayList of all the users
     */
    public ArrayList<User> getUserArrayList(){
        return userArrayList;
    }

    /**
     * Sets the user array list to a user provided user array list
     *
     * @param passedArrayList an updated array list of users
     */
    public void setUserArrayList(ArrayList<User> passedArrayList){
        this.userArrayList = passedArrayList;
    }
}
