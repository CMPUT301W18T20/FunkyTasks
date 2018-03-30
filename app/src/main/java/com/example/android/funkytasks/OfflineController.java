package com.example.android.funkytasks;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by fc1, Ivy on 2018-03-29.
 */

public class OfflineController {

    private String FILENAME;
    private Context context;
    private ArrayList<Task> taskList;

    public OfflineController(Context context, String userName) {
        this.context = context;
        this.FILENAME = userName + ".sav";
    }

    public boolean isFileExist() {
        File file = context.getFileStreamPath(FILENAME);
        if(file.exists()){
            taskList = loadFromFile();
            Log.d("File", "File exist");
            return true;
        }else{
            taskList = new ArrayList<>();
            Log.d("File", "File does not exist");
            return false;
        }
    }

    public void saveInFile(Task task) {

        try {
            isFileExist();
            taskList.add(task);
            FileOutputStream fos = context.openFileOutput(FILENAME, context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(taskList, out);
            out.flush();
            Log.d("Tasktitle in controller", task.getTitle());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Task> loadFromFile() {

        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            Type dataType = new TypeToken<ArrayList<Task>>() {}.getType();
            taskList = gson.fromJson(in, dataType);
            Log.d("size in controller", String.valueOf(taskList.size()));

            return taskList;

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public void deleteFromArrayList(){
        try {
            isFileExist();
            Task task = taskList.remove(0);
            Log.d("task deleted", task.getTitle());
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(taskList, out);
            out.flush();
            Log.d("Tasks size", String.valueOf(taskList.size()));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
