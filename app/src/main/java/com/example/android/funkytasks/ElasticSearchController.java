package com.example.android.funkytasks;

/**
 * Created by MonicaB on 2018-02-20.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

public class ElasticSearchController {

    private static JestDroidClient client;
    private static String database = "http://cmput301.softwareprocess.es:8080/";
    private static String indexType = "cmput301w18t20";
    private static String userType = "user";
    private static String taskType = "task";

    // TODO we need a function which adds users to elastic search database

    public static class PostUser extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user : users) {
                Index index = new Index.Builder(database).index(indexType).type(userType).build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        user.setId(result.getId());
                    } else {
                        Log.i("Error", "Some error with adding user");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and add the users");
                }
            }
            return null;
        }
    }

    // TODO we need a function which adds task to elastic search

    public static class PostTask extends AsyncTask<Task, Void, Void> {

        @Override
        protected Void doInBackground(Task... tasks) {
            verifySettings();

            for (Task task : tasks) {
                Index index = new Index.Builder(database).index(indexType).type(taskType).build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        task.setId(result.getId());
                    } else {
                        Log.i("Error", "Some error with adding task");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and add the task");
                }
            }
            return null;
        }
    }


    // TODO we need a function which grabs user from elastic search
    // GET REQUEST

    public static class GetUser extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... search_parameters) {
            verifySettings();

            //TO DELETE PLACEHOLDER USER
            User user = new User("asd","as","asd");
            // TODO build the correct query that takes in username input and checks if its in the database
            String query = "{\n"
                    + "    \"query\": {\n"
                    + "        \"filtered\" : {\n"
                    + "            \"query\" : {\n"
                    + "                \"query_string\" : {\n"
                    + "                    \"query\" : \"java\"\n"
                    + "                }\n"
                    + "            }"
                    + "        }\n"
                    + "    }\n"
                    + "}";

            //https://www.elastic.co/guide/en/elasticsearch/reference/2.3/query-dsl-term-query.html

            //bingo the link above may help me

            return user;
        }

    }




    //TODO need a way to grab list of tasks


    //TODO delete a specific task (status=done)

    //TODO update a user's contact info

    //TODO update a task's status in database




    public static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(database);
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

}
