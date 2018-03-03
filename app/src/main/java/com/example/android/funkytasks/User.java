package com.example.android.funkytasks;

import java.util.ArrayList;
import java.util.Iterator;

import io.searchbox.annotations.JestId;

/**
 * Created by MonicaB on 2018-02-20.
 */

public class User {
    // unique username, email, phonenumber, rating
    private String username;
    private String email;
    private String phonenumber;
    private double rating;
    private ArrayList<Task> acceptedTasks;  // tasks the user has agreed to solve
    private ArrayList<Task> requestedTasks; // tasks the user has put out
    private ArrayList<Task> biddedTasks;    // tasks user has currently bidded on

    //TODO delete method for requested tasks
    //TODO

    @JestId
    private String id;

    User(String username, String email, String phonenumber){
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
        this.rating = 3;
        this.acceptedTasks = new ArrayList<Task>();
        this.requestedTasks = new ArrayList<Task>();
        this.biddedTasks = new ArrayList<Task>();
    }

    public void setId(String newId){
        this.id = newId;
    }

    public String getId(){
        return this.id;
    }

    public void addRequestedTask(Task newTask){
        requestedTasks.add(newTask);
    }

    public void addBiddedTask(Task newTask){
        biddedTasks.add(newTask);
    }
    public void addAccepedTask(Task newTask){
        acceptedTasks.add(newTask);
    }

    public ArrayList<Task> getBiddedTasks(){
        return this.biddedTasks;
    }

    public ArrayList<Task> getAcceptedTasks(){
        return this.acceptedTasks;
    }

    public ArrayList<Task> getRequestedTasks(){
        return this.requestedTasks;
    }

    public void deleteRequestedTask(Task toDelete){
        // if task has bid on it, we also have to call delete bidded task
        Iterator itr = requestedTasks.iterator();
        while (itr.hasNext()) {
            Task task = (Task) itr.next();
            if (task == toDelete) {
                itr.remove();
            }
        }
    }

    public int deleteBiddedTask(Task toDelete){
        // Elastic search should first grab all users, return user array list.
        // then we call this method for every user, if it returns 1 we also have to update it

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
        Iterator itr = acceptedTasks.iterator();
        while (itr.hasNext()) {
            Task task = (Task) itr.next();
            if (task.getStatus().equals("done")) {
                itr.remove();
            }
        }


    }

    public String getUsername(){
        return this.username;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String newEmail){
        this.email = newEmail;
    }

    public String getPhonenumber(){
        return this.phonenumber;
    }

    public void setPhonenumber(String newNumber){
        this.phonenumber = newNumber;
    }

    public double getRating(){
        return this.rating;
    }

    public void setRating(double newRating){
        this.rating = newRating;
    }

}
