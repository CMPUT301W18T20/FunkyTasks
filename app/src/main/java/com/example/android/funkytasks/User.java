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
    private double rating;
//    private ArrayList<Task> acceptedTasks;  // tasks the user has agreed to solve
    private ArrayList<String> requestedTasks; // tasks the user has put out
//    private ArrayList<Task> biddedTasks;    // tasks user has currently bidded on

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
        this.rating = 3;
//        this.acceptedTasks = new ArrayList<Task>();
//        this.requestedTasks = new ArrayList<Task>();
//        this.biddedTasks = new ArrayList<Task>();
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
//    public void addRequestedTask(String newTaskID){
//        requestedTasks.add(newTaskID);
//    }
//
//    public void addBiddedTask(Task newTask){
//        biddedTasks.add(newTask);
//    }
//    public void addAccepedTask(Task newTask){
//        acceptedTasks.add(newTask);
//    }
//
//    public ArrayList<Task> getBiddedTasks(){
//        return this.biddedTasks;
//    }
//
//    public ArrayList<Task> getAcceptedTasks(){
//        return this.acceptedTasks;
//    }
//

    /**
     * Returns the array list of requested tasks.
     *
     * @return an array list holding the requested tasks of one user
     */
//    public ArrayList<String> getRequestedTasks(){
//        return this.requestedTasks;
//    }


//    // DO NOT WRITE TESTS FOR DELETE METHODS YET
//    public void deleteRequestedTask(int index){
//        // if task has bid on it, we also have to call delete this task from other users accounts
//        // TODO return a list of bidders(users) on the task
//       requestedTasks.remove(index);
//
////        Iterator itr = requestedTasks.iterator();
////        while (itr.hasNext()) {
////            Task task = (Task) itr.next();
////            Log.e("task",task.getTitle());
////            if (toDelete != null && task.getId().equals(toDelete.getId())) {
////                itr.remove();
////            }
////        }
//    }

/*    public int deleteBiddedTask(Task toDelete){
        // Elastic search should first grab all users, return user array list.
        // then we call this method for every user, if it returns 1 we also have to update user

        boolean Task = biddedTasks.contains(toDelete);
        int returnValue = 0;
        if (Task){
            Iterator itr = biddedTasks.iterator();
            while (itr.hasNext()) {
                Task task = (Task) itr.next();
                if (task == toDelete) {
                    itr.remove();
                    returnValue = 1;
                }
            }
        }
        return returnValue;
    }

    public void deleteAcceptedTask(){
        // if we are done the task, delete from our list, then delete it from the requester's list
        Iterator itr = acceptedTasks.iterator();
        while (itr.hasNext()) {
            Task task = (Task) itr.next();
            if (task.getStatus().equals("done")) {
                itr.remove();
            }
        }


    }
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
        return this.rating;
    }

    /**
     * Sets the user's rating to the double provided
     *
     * @param newRating a double representing the new rating for the user
     */
    public void setRating(double newRating){
        this.rating = newRating;
    }

}
