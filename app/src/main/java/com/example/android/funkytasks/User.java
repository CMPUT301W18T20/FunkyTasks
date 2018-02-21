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
    private ArrayList<Task> tasks;

    @JestId
    private String id;

    User(String username, String email, String phonenumber){
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
        this.rating = 3;
        this.tasks = new ArrayList<Task>();
    }

    public void setId(String newId){
        this.id = newId;
    }

    public String getId(){
        return this.id;
    }

    public void addTask(Task newTask){
        tasks.add(newTask);
    }

    public ArrayList<Task> getTasks(){
        return this.tasks;
    }

    public void deleteTask(){
        Iterator itr = tasks.iterator();
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
