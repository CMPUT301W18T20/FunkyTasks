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

    public LocalRequestedTaskController(Context context, String userName) {
        this.context = context;
        this.FILENAME = userName + "Requested.sav";
        this.username = userName;
    }

    public boolean isFileExist() {
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

        } catch (Exception e) {
            Log.e("Error", "We arnt getting the list of tasks");
            return;

        }

        for (Task eachtask : taskList) {
            Log.d("task title base", eachtask.getTitle());
            Log.d("task description base", eachtask.getDescription());
        }

        FileOutputStream fos = context.openFileOutput(FILENAME, context.MODE_PRIVATE);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
        Gson gson = new Gson();
        gson.toJson(taskList, out);
        Log.e("Got the tasks ", taskList.toString());
        out.flush();




    }

    public void addOfflineTask(Task task){
        try {
            isFileExist();
            taskList.add(task);
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


    public ArrayList<Task> loadRequestedTask() {

        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type dataType = new TypeToken<ArrayList<Task>>() {}.getType();
            taskList = gson.fromJson(in, dataType);
            return taskList;

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            taskList = new ArrayList<>();
            return taskList;

        }
    }
}
