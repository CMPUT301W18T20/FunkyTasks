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

/**
 * Created by fc1 on 2018-03-30.
 */

public class LocalRequestedTaskController {
    private String FILENAME;
    private Context context;
    private ArrayList<Task> taskList;
    private String username;
    private User user;

    /**
     *
     * @param context context of the application
     * @param userName the userName of the user
     */
    public LocalRequestedTaskController(Context context, String userName) {
        this.context = context;
        this.FILENAME = userName + "Requested.sav";
        this.username = userName;
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
            taskList = loadRequestedTask();
            Log.d("File", "File exist");
            return true;
        }else{
            taskList = new ArrayList<>();
            Log.d("File", "File does not exist");
            return false;
        }
    }

    /**
     * Get all the requested tasks of the user from Elastic Search and store them in a local file
     * @throws IOException
     */
    public void SaveRequestedTask() throws IOException {
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(username);
        try {
            user = getUser.get();
            Log.e("Got the username: ", user.getUsername());

        } catch (Exception e) {
            Log.e("Error", "We arnt getting the user");
            return;

        }

        // Getting the all the tasks associated with the user
        ElasticSearchController.GetAllTask getAllTask = new ElasticSearchController.GetAllTask();
        getAllTask.execute(username);
        try {
            taskList = getAllTask.get();
            for (Task eachtask : taskList) {
                Log.d("task title base", eachtask.getTitle());
                Log.d("task description base", eachtask.getDescription());
            }

        } catch (Exception e) {
            Log.e("Error", "We aren't getting the list of tasks");
            return;

        }



        FileOutputStream fos = context.openFileOutput(FILENAME, context.MODE_PRIVATE);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
        Gson gson = new Gson();
        gson.toJson(taskList, out);
        Log.e("Got the tasks ", taskList.toString());
        out.flush();

    }

    /**
     * Add a task given to the local file
     * @param task task to be added to the local file
     */
    public void addOfflineTask(Task task){
        try {
            fileExists();
            taskList.add(task);
            for (Task eachtask : taskList) {
                Log.d("task title base", eachtask.getTitle());
                Log.d("task description base", eachtask.getDescription());
            }
            FileOutputStream fos = context.openFileOutput(FILENAME, context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(taskList, out);
            out.flush();
            Log.d("Tasktitle in requested", task.getTitle());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * This function reads in the list stored in the file and assign it to taskList
     * @return taskList which is a list of all the tasks stored in the file
     */
    public ArrayList<Task> loadRequestedTask() {

        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type dataType = new TypeToken<ArrayList<Task>>() {}.getType();
            taskList = gson.fromJson(in, dataType);
            return taskList;

        } catch (FileNotFoundException e) {
            taskList = new ArrayList<>();
            return taskList;

        }
    }

    /**
     * This function updates a task that is stored in the local file
     * @param task task to be updated
     * @param index index of the task in the list
     */
    public void updateRequestedTask(Task task, int index){

        taskList = loadRequestedTask();
        Task updateTask = taskList.get(index);
        updateTask.setTitle(task.getTitle());
        updateTask.setDescription(task.getDescription());
        updateTask.setImagesList(task.getImages());

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

    public void deleteTask(Task task){
        taskList = loadRequestedTask();
        taskList.remove(task);

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

}
