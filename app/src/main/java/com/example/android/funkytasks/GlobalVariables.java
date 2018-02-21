package com.example.android.funkytasks;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Atharv on 2/21/2018.
 */

public class GlobalVariables extends Application{
    private ArrayList<User> userArrayList = new ArrayList<User>();

    public ArrayList<User> getUserArrayList(){
        return userArrayList;
    }

    public void setUserArrayList(ArrayList<User> passedArrayList){
        this.userArrayList = passedArrayList;
    }
}
