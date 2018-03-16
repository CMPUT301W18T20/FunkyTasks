package com.example.android.funkytasks;

/**
 * Created by MonicaB on 2018-02-20.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

public class ElasticSearchController {

    private static JestDroidClient client;
    private static String database = "http://cmput301.softwareprocess.es:8080";
    private static String indexType = "cmput301w18t20";
    private static String userType = "user";
    private static String taskType = "task";
    private static String bidType = "bid";

    public static class PostUser extends AsyncTask<User, Void, Void> { // add new user to database
        @Override
        protected Void doInBackground(User... newUser) {
            verifySettings();

            for (User user: newUser) { // get the new created user object to post to database

                Index index = new Index.Builder(user).index(indexType).type(userType).build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        user.setId(result.getId());
                        Log.e("Looking","good to add");
                    } else {
                        Log.e("Error", "Some error with adding user");
                    }
                } catch (Exception e) {
                    Log.e("Error", "The application failed to build and add the users");
                }
            }

            return null;
        }
    }

    public static class PostTask extends AsyncTask<Task, Void, Task> { // adds new task for the user

        // https://stackoverflow.com/questions/12069669/how-can-you-pass-multiple-primitive-parameters-to-asynctask
        @Override
        protected Task doInBackground(Task... params) {
            verifySettings();

            Task task = (Task) params[0];

            // POSTING TASK TO ENTIRE TASK DATABASE
            Index index = new Index.Builder(task).index(indexType).type(taskType).build();

            try {
                DocumentResult result = client.execute(index);
                if (result.isSucceeded()) {
                    task.setId(result.getId());
                    Log.e("Looking","good to add task to server");
                    return task;
                } else {
                    Log.e("Error", "Some error with adding task");
                }
            } catch (Exception e) {
                Log.e("Error", "The application failed to build and add the task");
            }

            return null;
        }
    }


    public static class GetUser extends AsyncTask<String, Void, User> { // grabs user from database

        @Override
        protected User doInBackground(String... search_parameters) {
            verifySettings();

            User returnUser = null;

            //https://www.elastic.co/guide/en/elasticsearch/reference/2.3/query-dsl-term-query.html
            // http://okfnlabs.org/blog/2013/07/01/elasticsearch-query-tutorial.html#basic-queries-using-only-the-query-string
            String query = "{\n" +
                    "    \"query\" : {\n" +
                    "       \"constant_score\" : {\n" +
                    "           \"filter\" : {\n" +
                    "               \"term\" : {\"username\": \"" + search_parameters[0] + "\"}\n" +
                    "             }\n" +
                    "         }\n" +
                    "    }\n" +
                    "}";

            Search search = (Search) new Search.Builder(query).addIndex(indexType).addType(userType).build();

            try {
                JestResult result = client.execute(search); // Use JestResult for one result and searchresult for all results to add to a list
                if (result.isSucceeded()) {
                    returnUser = result.getSourceAsObject(User.class);
                    Log.e("returnUSer worksss",returnUser.getUsername());
                    return returnUser;
                } else {
                    Log.e("Nothing", "Theres no user in database");
                }
            } catch (Exception e) {
                Log.e("Error", "Something went wrong with getting user!");
            }
            return returnUser;
        }
    }

    public static class GetAllUsers extends AsyncTask<String, Void, ArrayList<User>> { // grabs user from database

        @Override
        protected ArrayList<User> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<User> foundUsers = new ArrayList<User>();

            int size = 50; // change this number to get back more results
            // https://www.elastic.co/guide/en/elasticsearch/reference/current/search-request-from-size.html

            String query = "{\n"+
                    "\"size\":" + size + ",\n"+
                    "\"query\": {\n" +
                    "\"match_all\": {}\n" +
                    "}\n"+
                    "}";

            Search search = new Search.Builder(query).addIndex(indexType).addType(userType).build();

            try {
                SearchResult result = client.execute(search); // Use JestResult for one result and searchresult for all results to add to a list
                if (result.isSucceeded()) {
                    foundUsers = new ArrayList<>(result.getSourceAsObjectList(User.class));
                } else {
                    Log.e("Nothing", "Theres no user in database");
                }
            } catch (Exception e) {
                Log.e("Error", "Something went wrong with getting user!");
            }

            return foundUsers;
        }
    }

    public static class deleteUser extends AsyncTask<String,Void,Void>{
        // Input a user's id to delete
        // Will output a log message if it is successful
        @Override
        protected Void doInBackground(String... givenUser){
            verifySettings();

            Delete delete = new Delete.Builder(givenUser[0]).index(indexType).type(userType).build();

            try{
                DocumentResult result = client.execute(delete);
                if (result.isSucceeded()){
                    Log.e("Successful","delete of user");
                }
                else{
                    Log.e("Unable","to delete user");
                }
            }
            catch(Exception e){
                Log.e("error","something went wrong with deleting user");
            }

            return null;
        }
    }


    public static class GetTask extends AsyncTask<String,Void,Task>{
        // grabs details of one specific task
        @Override
        protected Task doInBackground(String... search_parameters){
            verifySettings();
            Task returnTask;

            String query = "{\n" +
                    "  \"query\": { \"term\": {\"_id\": \"" + search_parameters[0] + "\"} }\n" + "}";

            Search search = (Search) new Search.Builder(query).addIndex(indexType).addType(taskType).build();
            //Get get = new Get.Builder(indexType, search_parameters[0]).type(taskType).build();

            try {
                JestResult result = client.execute(search);
                if (result.isSucceeded()) {
                    returnTask = result.getSourceAsObject(Task.class);
                    Log.e("returntask works",returnTask.getTitle());
                    return returnTask;
                } else {
                    Log.e("Nothing", "Theres no task in database");
                }
            } catch (Exception e) {
                Log.e("Error", "Something went wrong with getting task!");
            }
            return null;
        }

    }


    public static class GetAllTask extends AsyncTask<String, Void, ArrayList<Task>> {

        @Override
        protected ArrayList<Task> doInBackground (String... search_parameters) {
            verifySettings();

            int size = 50000; // change this number to get back more results
            // https://www.elastic.co/guide/en/elasticsearch/reference/current/search-request-from-size.html

            String query = "{\n" +
                    "\"size\":" + size + ",\n" +
                    "\"query\": {\n" +
                    "\"match\": {\n" +
                    "\"requester\": {\n" +
                    "\"query\": \"" + search_parameters[0] +
                    "\" }\n" +
                    "}\n" +
                    "}\n" +
                    "}";

            ArrayList<Task> tasks;

            Search search = new Search.Builder(query).addIndex(indexType).addType(taskType).build();

            try{
                SearchResult result = client.execute(search);
                if(result.isSucceeded()){
                    tasks = new ArrayList<>(result.getSourceAsObjectList(Task.class));
                    return tasks;
                }
                else{
                    Log.e("Nothing", "No tasks in database");
                }
            }
            catch(Exception e){
                Log.e("Error", "Something went wrong with getting all tasks");
            }

            return null;
        }
    }

    public static class updateUser extends AsyncTask<User,Void,Void>{ // update a user's contact info by passing in the user object

        @Override
        protected Void doInBackground (User... currentUser) {
            verifySettings();

            User user = currentUser[0];
            Index index = new Index.Builder(user).index(indexType).type(userType).id(user.getId()).build();

            try {
                JestResult result = client.execute(index); // Use JestResult for one result and searchresult for all results to add to a list
                if (result.isSucceeded()) {
                    Log.e("successfull", "update of user");
                } else {
                    Log.e("Nothing", "No user in database");
                }
            } catch (Exception e) {
                Log.e("Error", "Something went wrong with getting tasks from user!");
            }
            return null;

        }

    }


    public static class updateTask extends AsyncTask<Task,Void,Void>{ // update a user's task by passing in the user object

        @Override
        protected Void doInBackground (Task... currentTask) {
            verifySettings();


            Index index = new Index.Builder(currentTask[0]).index(indexType).type(taskType).id(currentTask[0].getId())
                    .build();

            try{
                DocumentResult result = client.execute(index); // Use JestResult for one result and searchresult for all results to add to a list
                if(result.isSucceeded()){
                    Log.e("Update","successful for task");
                    return null;
                }
                else{
                    Log.e("Nothing", "Theres no user in database to update their task");
                }
            }
            catch(Exception e){
                Log.e("Error", "Something went wrong with getting tasks from user!");
            }


            return null;
        }
    }


    public static class deleteTask extends AsyncTask<String,Void,Void>{
        // Deletes task from global list by inputting the task id
        @Override
        protected Void doInBackground(String... givenTask){
            verifySettings();

            Delete delete = new Delete.Builder(givenTask[0]).index(indexType).type(taskType).build();

            try{
                DocumentResult result = client.execute(delete);
                if (result.isSucceeded()){
                    Log.e("Successful","delete");
                }
                else{
                    Log.e("Unable","to delete task");
                }
            }
            catch(Exception e){
                Log.e("error","something went wrong with deleting task");
            }

            return null;
        }
    }

    public static class searchTask extends AsyncTask<String,Void,ArrayList<Task>>{

        @Override
        protected ArrayList<Task> doInBackground(String... searchParameters){
            verifySettings();

            int size = 50000;
            String username = searchParameters[1];
            // https://www.elastic.co/guide/en/elasticsearch/guide/current/phrase-matching.html
            String query = "{\n" +
                    "\"size\":" + size + ",\n" +
                    "\"query\": {\n" +
                    "\"match\": {\n" +
                    "\"description\": {\n" +
                    "\"query\": \"" + searchParameters[0] + "\",\n" +
                    "\"minimum_should_match\": \"100%\" \n" +
                    "}\n" +
                    "}\n" +
                    "}\n" +
                    "}";
            ArrayList<Task> tasks;

            Search search = new Search.Builder(query).addIndex(indexType).addType(taskType).build();

            try{
                SearchResult result = client.execute(search);
                if(result.isSucceeded()){
                    tasks = new ArrayList<>(result.getSourceAsObjectList(Task.class));
                    for (int i = 0; i < tasks.size(); i++){
                        Task task = tasks.get(i);
                        if (task.getRequester().equals(username)){
                            tasks.remove(i);
                        }
                        else if (task.getStatus().equals("accepted") || task.getStatus().equals("done")){
                            tasks.remove(i);
                        }
                    }
                    return tasks;
                }
                else{
                    Log.e("Nothing", "No tasks in database");
                }
            }
            catch(Exception e){
                Log.e("Error", "Something went wrong with getting all tasks");
            }

            return null;
        }
    }

    public static class PlaceBid extends AsyncTask<Bid, Void, Bid> { // adds new bid for the user

        // https://stackoverflow.com/questions/12069669/how-can-you-pass-multiple-primitive-parameters-to-asynctask
        @Override
        protected Bid doInBackground(Bid... params) {
            verifySettings();

            Bid bid = (Bid) params[0];

            // POSTING TASK TO ENTIRE BID DATABASE
            Index index = new Index.Builder(bid).index(indexType).type(bidType).build();

            try {
                DocumentResult result = client.execute(index);
                if (result.isSucceeded()) {
                    bid.setId(result.getId());
                    Log.e("Bid ID / es",bid.getId());
                    Log.e("Task ID for bid/ es",bid.getTaskID());
                    Log.e("Username for bid/ es",bid.getBidder());
                    Log.e("Looking","good to add bid to server");
                    return bid;
                } else {
                    Log.e("Error", "Some error with adding bid");
                }
            } catch (Exception e) {
                Log.e("Error", "The application failed to build and add the bid");
            }

            return null;
        }
    }

    public static class updateBid extends AsyncTask<Bid,Void,Void>{

        @Override
        protected Void doInBackground (Bid... currentBid) {
            verifySettings();


            Index index = new Index.Builder(currentBid[0]).index(indexType).type(bidType).id(currentBid[0].getId())
                    .build();

            try{
                DocumentResult result = client.execute(index); // Use JestResult for one result and searchresult for all results to add to a list
                if(result.isSucceeded()){
                    Log.e("Update","successful for bid");
                    return null;
                }
                else{
                    Log.e("Nothing", "Theres no user in database to update their bid");
                }
            }
            catch(Exception e){
                Log.e("Error", "Something went wrong with getting bid from user!");
            }


            return null;
        }
    }



    public static class GetBidsByBidder extends AsyncTask<String, Void, ArrayList<Bid>> {

        @Override
        protected ArrayList<Bid> doInBackground (String... search_parameters) {
            verifySettings();

            int size = 50000; // change this number to get back more results
            // https://www.elastic.co/guide/en/elasticsearch/reference/current/search-request-from-size.html

            String query = "{\n" +
                    "\"size\":" + size + ",\n" +
                    "\"query\": {\n" +
                    "\"match\": {\n" +
                    "\"bidder\": {\n" +
                    "\"query\": \"" + search_parameters[0] +
                    "\" }\n" +
                    "}\n" +
                    "}\n" +
                    "}";

            ArrayList<Bid> bids;

            Search search = new Search.Builder(query).addIndex(indexType).addType(bidType).build();

            try{
                SearchResult result = client.execute(search);
                if(result.isSucceeded()){
                    bids = new ArrayList<>(result.getSourceAsObjectList(Bid.class));
                    return bids;
                }
                else{
                    Log.e("Nothing", "No bids in database");
                }
            }
            catch(Exception e){
                Log.e("Error", "Something went wrong with getting all bids by bidder");
            }

            return null;
        }
    }

    public static class GetBidsByTaskID extends AsyncTask<String, Void, ArrayList<Bid>> {

        @Override
        protected ArrayList<Bid> doInBackground (String... search_parameters) {
            verifySettings();

            int size = 50000; // change this number to get back more results
            // https://www.elastic.co/guide/en/elasticsearch/reference/current/search-request-from-size.html

            String query = "{\n" +
                    "\"size\":" + size + ",\n" +
                    "\"query\": {\n" +
                    "\"match\": {\n" +
                    "\"taskID\": {\n" +
                    "\"query\": \"" + search_parameters[0] +
                    "\" }\n" +
                    "}\n" +
                    "}\n" +
                    "}";

            ArrayList<Bid> bids;

            Search search = new Search.Builder(query).addIndex(indexType).addType(bidType).build();

            try{
                SearchResult result = client.execute(search);
                if(result.isSucceeded()){
                    bids = new ArrayList<>(result.getSourceAsObjectList(Bid.class));
                    return bids;
                }
                else{
                    Log.e("Nothing", "No bids in database");
                }
            }
            catch(Exception e){
                Log.e("Error", "Something went wrong with getting all bids by taskID");
            }

            return null;
        }
    }

    public static class GetBidsByRequester extends AsyncTask<String, Void, ArrayList<Bid>> {

        @Override
        protected ArrayList<Bid> doInBackground (String... search_parameters) {
            verifySettings();

            int size = 50000; // change this number to get back more results
            // https://www.elastic.co/guide/en/elasticsearch/reference/current/search-request-from-size.html

            String query = "{\n" +
                    "\"size\":" + size + ",\n" +
                    "\"query\": {\n" +
                    "\"match\": {\n" +
                    "\"requester\": {\n" +
                    "\"query\": \"" + search_parameters[0] +
                    "\" }\n" +
                    "}\n" +
                    "}\n" +
                    "}";

            ArrayList<Bid> bids;

            Search search = new Search.Builder(query).addIndex(indexType).addType(bidType).build();

            try{
                SearchResult result = client.execute(search);
                if(result.isSucceeded()){
                    bids = new ArrayList<>(result.getSourceAsObjectList(Bid.class));
                    return bids;
                }
                else{
                    Log.e("Nothing", "No bids in database");
                }
            }
            catch(Exception e){
                Log.e("Error", "Something went wrong with getting all bids by bidder");
            }

            return null;
        }
    }


    public static class deleteBid extends AsyncTask<String,Void,Void>{
        // Deletes one Bid (to delete more than one bid run this function on a array list)
        // Input the bid id
        @Override
        protected Void doInBackground(String... givenBid){
            verifySettings();

            Delete delete = new Delete.Builder(givenBid[0]).index(indexType).type(bidType).build();

            try{
                DocumentResult result = client.execute(delete);
                if (result.isSucceeded()){
                    Log.e("Successful","delete of bid");
                }
                else{
                    Log.e("Unable","to delete bid");
                }
            }
            catch(Exception e){
                Log.e("error","something went wrong with deleting bid");
            }

            return null;
        }
    }

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
