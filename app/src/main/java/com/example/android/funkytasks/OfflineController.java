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
 * Created by fc1, eivenlour on 2018-03-29.
 */

public class OfflineController {

    private String FILENAME;
    private Context context;
    private ArrayList<Task> taskList;

    public OfflineController(Context context, String userName) {
        this.context = context;
        this.FILENAME = userName + ".sav";
    }

    /**
     * Checks if the file exists, if it does, assign taskList to the list from the file;
     * if it doesn't, set the taskList to be an empty list, then returns a boolean
     *
     * @return Boolean indicating if the file exists or not
     */
    public boolean fileExists() {
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

    /**
     * appends the task to taskList and write the new taskList into the local file
     * @param task task to be added to the local file
     */
    public void saveInFile(Task task) {

        try {
            fileExists();
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

    /**
     * This function updates a task in the local file
     * @param task task to be updated
     */
    public void updateInFile(Task task) {
        String offlineId = task.getOfflineId();
        taskList = loadFromFile();
        for (Task eachTask: taskList){
            if (offlineId != null) {
                if (offlineId.equals(eachTask.getOfflineId())) {
                    Log.d("Title before", eachTask.getTitle());
                    eachTask.setTitle(task.getTitle());
                    Log.d("Title after", eachTask.getTitle());
                    Log.d("Desc before", eachTask.getDescription());
                    eachTask.setDescription(task.getDescription());
                    Log.d("Title after", eachTask.getDescription());
                    eachTask.setImagesList(task.getImages());
                }
            }
        }

        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(taskList, out);
            Log.e("Got the tasks ", taskList.toString());
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This function reads in the list stored in the file and assign it to taskList
     * @return taskList which is a list of tasks stored in the local file
     */
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
            taskList = new ArrayList<>();
            return taskList;

        }
    }

    /**
     * This function deletes the first element in the list and stores the new taskList back to the local file
     */
    public void deleteFromArrayList(){
        try {
            fileExists();
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

    public void deleteTask(Task task){
        try {
            fileExists();
            Log.d("task deletedddddd", task.getTitle());
            taskList.remove(task);
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
