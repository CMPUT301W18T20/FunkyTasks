package com.example.android.funkytasks;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by fc1}, Ivy on 2018-03-29.
 */

public class OfflineController {

    private String FILENAME;

    public OfflineController(String userName){
        FILENAME = userName + ".sav";
    }
}
