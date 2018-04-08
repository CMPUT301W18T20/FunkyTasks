
/**
 * ElasticSearchController
 *
 * Version 1.0.0
 *
 * Created by MonicaB on 2018-02-20.
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */

package com.example.android.funkytasks;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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

/**
 * The controller for our Elastic Search functions for the project
 */
public class ElasticSearchController {

    private static JestDroidClient client;
    private static String database = "http://cmput301.softwareprocess.es:8080";
    private static String indexType = "cmput301w18t20";
    private static String userType = "user";
    private static String taskType = "task";
    private static String bidType = "bid";

    public static class PostUser extends AsyncTask<User, Void, Void> { // add new user to database
        /**
         * Tells the elastic search what to do in the background
         *
         * @param newUser a User representing a new user to be added to the database
         * @return returns null to indicate it's working
         */
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

    /**
     * Posts a task to the server
     */
    public static class PostTask extends AsyncTask<Task, Void, Task> { // adds new task for the user

        /**
         * Posts a task to the server. This process runs in the background of other processes
         *
         * @param params represents a task to be posted
         * @return returns the task if it is successful, returns null otherwise
         */
        @Override
        protected Task doInBackground(Task... params) {
            verifySettings();

            Task task = params[0];

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


    /**
     * Gets the user from the server
     */
    public static class GetUser extends AsyncTask<String, Void, User> { // grabs user from database

        /**
         * Performs these actions in the background while it's looking for the user
         *
         * @param search_parameters a string representing the parameters the elastic search
         *                          should use when conducting its search
         * @return returns the users it finds in the search; null if none are found, a user
         *          if one or more are found
         */
        @Override
        protected User doInBackground(String... search_parameters) {
            verifySettings();

            User returnUser = null;

            String query = "{\n" +
                    "    \"query\" : {\n" +
                    "       \"constant_score\" : {\n" +
                    "           \"filter\" : {\n" +
                    "               \"term\" : {\"username\": \"" + search_parameters[0] + "\"}\n" +
                    "             }\n" +
                    "         }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query).addIndex(indexType).addType(userType).build();

            try {
                /* Use JestResult for one result and search result for all results to add to a list */
                JestResult result = client.execute(search);
                if (result.isSucceeded()) {
                    returnUser = result.getSourceAsObject(User.class);
                    Log.e("returnUser works",returnUser.getUsername());
                    return returnUser;
                } else {
                    Log.e("Nothing", "There's no user in database");
                }
            } catch (Exception e) {
                Log.e("Error", "Something went wrong with getting user!");
            }
            return returnUser;
        }
    }

    /**
     * Returns all the users in the database
     */
    public static class GetAllUsers extends AsyncTask<String, Void, ArrayList<User>> { // grabs user from database

        /**
         * This class performs these actions in the background while it searches for
         * all the users in the database.
         *
         * @param search_parameters a string of search parameters that's fed to the elastic search
         * @return returns the users that were found in the search; null if none were found,
         *          an array list if one or more were found
         */
        @Override
        protected ArrayList<User> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<User> foundUsers = new ArrayList<User>();

            int size = 50; // change this number to get back more results

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

    /**
     * Deletes a user from the database
     */
    public static class deleteUser extends AsyncTask<String,Void,Void>{
        // Input a user's id to delete
        // Will output a log message if it is successful

        /**
         * Runs this operations in the background to find and delete a particular user
         *
         * @param givenUser a string representing the user the search controller needs to
         *                  find and remove
         * @return returns null
         */
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


    /**
     * This function searches for and returns a task
     */
    public static class GetTask extends AsyncTask<String,Void,Task>{
        // grabs details of one specific task

        /**
         * Performed in the background, this function searches for and returns a task based
         * on the given search parameters.
         *
         * @param search_parameters a string representing the parameters the search controller
         *                          takes in to perform its search
         * @return returns the task; null if nothing is found, a single task if it is found
         */
        @Override
        protected Task doInBackground(String... search_parameters){
            verifySettings();
            Task returnTask;

            String query = "{\n" +
                    "  \"query\": { \"term\": {\"_id\": \"" + search_parameters[0] + "\"} }\n" + "}";

            Search search = new Search.Builder(query).addIndex(indexType).addType(taskType).build();

            try {
                JestResult result = client.execute(search);
                if (result.isSucceeded()) {
                    returnTask = result.getSourceAsObject(Task.class);
                    Log.e("return task works",returnTask.getTitle());
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


    /**
     * Returns all the tasks in the server
     */
    public static class GetAllTask extends AsyncTask<String, Void, ArrayList<Task>> {

        /**
         * Searches for and returns all the tasks in the server.
         *
         * @param search_parameters a string representing the parameters for the search
         * @return returns null if no tasks are found, returns an array list if tasks are found
         */
        @Override
        protected ArrayList<Task> doInBackground (String... search_parameters) {
            verifySettings();

            int size = 50000; // change this number to get back more results

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


    /**
     * Returns all the users who are fulfilling a task
     */
    public static class GetAllProviderTask extends AsyncTask<String, Void, ArrayList<Task>> {

        /**
         * This function searches for all the task providers in the database and returns them
         * if they're found.
         *
         * @param search_parameters a string representing the parameters needed for the search
         * @return returns null if no providers are found, returns an array list if they are found
         */
        @Override
        protected ArrayList<Task> doInBackground (String... search_parameters) {
            verifySettings();

            int size = 50000; // change this number to get back more results

            String query = "{\n" +
                    "\"size\":" + size + ",\n" +
                    "\"query\": {\n" +
                    "\"match\": {\n" +
                    "\"provider\": {\n" +
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

    /**
     * Locates and updates a user's information
     */
    public static class updateUser extends AsyncTask<User,Void,Void>{ // update a user's contact info by passing in the user object

        /**
         * Replaces a user in the server with their updated user information.
         *
         * @param currentUser a user object that represents the user to be located and replaced
         * @return returns null
         */
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

    /**
     * Updates a task in the server
     */
    public static class updateTask extends AsyncTask<Task,Void,Void>{ // update a user's task by passing in the user object

        /**
         * Called when a task provider changes the details of their task,
         * this function searches for the task and replaces it with the updated version of itself
         *
         * @param currentTask a task object representing the task to be located and replaced
         * @return returns null
         */
        @Override
        protected Void doInBackground (Task... currentTask) {
            verifySettings();


            Index index = new Index.Builder(currentTask[0]).index(indexType).type(taskType).id(currentTask[0].getId())
                    .build();

            try{
                DocumentResult result = client.execute(index);
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

    /**
     * Deletes a task from the server
     */
    public static class deleteTask extends AsyncTask<String,Void,Void>{
        // Deletes task from global list by inputting the task id

        /**
         * This function searches for and deletes a task from the server.
         *
         * @param givenTask a string representing the task to be located and deleted
         * @return returns null
         */
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

    /**
     * Searches the database for a particular task
     */
    public static class searchTask extends AsyncTask<String,Void,ArrayList<Task>> {

        /**
         * Searches for and returns tasks based on given search criteria
         *
         * @param searchParameters a string representing the parameters needed for the search
         * @return returns the tasks if they are found, returns null otherwise
         */
        @Override
        protected ArrayList<Task> doInBackground(String... searchParameters) {
            verifySettings();

            int size = 50000;
            String username = searchParameters[1];

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

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    tasks = new ArrayList<>(result.getSourceAsObjectList(Task.class));

                    for (Iterator<Task> iterator = tasks.iterator(); iterator.hasNext(); ) {
                        Task task = iterator.next();
                        Log.e("Task title ", task.getTitle());
                        Log.e("Requester and username ", task.getRequester() + "-" + username);
                        if (task.getRequester().equals(username)) {
                            Log.e("Deleting ", task.getRequester() + "-" +username);
                            iterator.remove();
                        } else if (task.getStatus().equals("accepted") || task.getStatus().equals("done")) {
                            iterator.remove();
                        }
                    }
                    return tasks;
                } else {
                    Log.e("Nothing", "No tasks in database");
                }
            } catch (Exception e) {
                Log.e("Error", "Something went wrong with getting all tasks");
            }

            return null;
        }
    }

    /**
     *  If a user doesn't know what to search for, it displays all tasks posted by other users
     */
    public static class GetDefaultSearchTaskList extends AsyncTask<String,Void,ArrayList<Task>> {

        /**
         * This function displays all the tasks other users have posted if no search criteria
         * are given. This function is not currently being implemented by our project.
         *
         * @param searchParameters a string representing the parameters needed for the search
         * @return returns tasks if they're found, returns null otherwise
         */
        @Override
        protected ArrayList<Task> doInBackground(String... searchParameters) {
            verifySettings();

            int size = 50000;
            String username = searchParameters[0];

            String query = "{\n"+
                    "\"size\":" + size + ",\n"+
                    "\"query\": {\n" +
                    "\"match_all\": {}\n" +
                    "}\n"+
                    "}";

            ArrayList<Task> tasks;

            Search search = new Search.Builder(query).addIndex(indexType).addType(taskType).build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    tasks = new ArrayList<>(result.getSourceAsObjectList(Task.class));

                    for (Iterator<Task> iterator = tasks.iterator(); iterator.hasNext(); ) {
                        Task task = iterator.next();
                        Log.e("Task title ", task.getTitle());
                        Log.e("Requester and username ", task.getRequester() + "-" + username);
                        if (task.getRequester().equals(username)) {
                            Log.e("Deleting ", task.getRequester() + "-" +username);
                            iterator.remove();
                        } else if (task.getStatus().equals("accepted") || task.getStatus().equals("done")) {
                            iterator.remove();
                        }
                    }
                    return tasks;
                } else {
                    Log.e("Nothing", "No tasks in database");
                }
            } catch (Exception e) {
                Log.e("Error", "Something went wrong with getting all tasks");
            }

            return null;
        }
    }

    /**
     * Posts a bid to the server
     */
    public static class PlaceBid extends AsyncTask<Bid, Void, Bid> { // adds new bid for the user

        /**
         * Posts a bid to the server when someone posts a bid on a task
         *
         * @param params a bid object that is used to perform the search
         * @return returns null
         */
        @Override
        protected Bid doInBackground(Bid... params) {
            verifySettings();

            Bid bid = params[0];

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

    /**
     * This function searches for and returns a bid
     */
    public static class GetBid extends AsyncTask<String,Void,Bid>{
        // grabs details of one specific bid

        /**
         * Performed in the background, this function searches for and returns a bid based
         * on the given search parameters.
         *
         * @param search_parameters a string representing the parameters the search controller
         *                          takes in to perform its search
         * @return returns the bid; null if nothing is found, a single bid if it is found
         */
        @Override
        protected Bid doInBackground(String... search_parameters){
            verifySettings();
            Bid returnBid;

            String query = "{\n" +
                    "    \"query\" : {\n" +
                    "       \"constant_score\" : {\n" +
                    "           \"filter\" : {\n" +
                    "               \"term\" : {\"bidID\": \"" + search_parameters[0] + "\"}\n" +
                    "             }\n" +
                    "         }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query).addIndex(indexType).addType(bidType).build();

            try {
                JestResult result = client.execute(search);
                if (result.isSucceeded()) {
                    returnBid = result.getSourceAsObject(Bid.class);
                    Log.e("return task works",returnBid.getBidId());
                    return returnBid;
                } else {
                    Log.e("Nothing", "Theres no bid in database");
                }
            } catch (Exception e) {
                Log.e("Error", "Something went wrong with getting bid!");
            }
            return null;
        }

    }

    /**
     * Locates and updates a bid in the server
     */
    public static class updateBid extends AsyncTask<Bid,Void,Void>{

        /**
         * When a task provider changes their bid, this function searches for the bid
         * and replaces it with the updated version of itself.
         *
         * @param currentBid a Bid object representing the bid to search for and replace
         * @return returns null
         */
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


    /**
     * Returns all the bids that one user has placed
     */
    public static class GetBidsByBidder extends AsyncTask<String, Void, ArrayList<Bid>> {

        /**
         * This function returns all the bids that one user has placed on any and all tasks
         * they have bidded on.
         *
         * @param search_parameters a string representing the parameters needed for the search
         * @return returns bids if they exist, returns null otherwise
         */
        @Override
        protected ArrayList<Bid> doInBackground (String... search_parameters) {
            verifySettings();

            int size = 50000; // change this number to get back more results

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

    /**
     * Returns all the bids placed on a certain task
     */
    public static class GetBidsByTaskID extends AsyncTask<String, Void, ArrayList<Bid>> {

        /**
         * Returns all the bids placed on one task
         *
         * @param search_parameters a string representing the parameters needed for the search
         * @return returns the bids if they exist, returns null otherwise
         */
        @Override
        protected ArrayList<Bid> doInBackground (String... search_parameters) {
            verifySettings();

            int size = 50000; // change this number to get back more results

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

    /**
     * Returns the bids posted by one particular user
     */
    public static class GetBidsByRequester extends AsyncTask<String, Void, ArrayList<Bid>> {

        /**
         * Returns all the bids one requester has posted.
         *
         * @param search_parameters a string representing the parameters needed for the search
         * @return returns the bids if they exist, returns null otherwise
         */
        @Override
        protected ArrayList<Bid> doInBackground (String... search_parameters) {
            verifySettings();

            int size = 50000; // change this number to get back more results

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
                Log.e("Error", "Something went wrong with getting all bids by requester");
            }

            return null;
        }
    }


    /**
     * Searches for and deletes a bid
     */
    public static class deleteBid extends AsyncTask<String,Void,Void>{
        // Deletes one Bid (to delete more than one bid run this function on a array list)
        // Input the bid id

        /**
         * Deletes a bid from the database.
         *
         * @param givenBid a string representing the bid to be searched for
         * @return returns null
         */
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

    /**
     * Verifies the settings for the database and Elastic Search controller
     */
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