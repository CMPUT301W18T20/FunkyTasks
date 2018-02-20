package com.example.android.funkytasks;

/**
 * Created by MonicaB on 2018-02-20.
 */

public class User {
    // unique username, email, phonenumber, rating
    private String username;
    private String email;
    private String phonenumber;
    private double rating;

    User(String username, String email, String phonenumber){
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
        this.rating = 3;
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
