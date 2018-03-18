package com.example.android.funkytasks;

import android.app.Activity;
import android.support.test.filters.SmallTest;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by jimi on 2018-03-15.
 */

public class CreateTaskTest extends ActivityInstrumentationTestCase2<CreateTaskActivity> {
    private Solo solo;
    private Task createdTask;

    private Task testingUS010101 = new Task("Testing create task","test","qwerty123");


    public CreateTaskTest() {
        super(CreateTaskActivity.class);


    }
    public void testStart() throws Exception{
        Activity activity = getActivity();

    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }
    @SmallTest
    public void testCreateTaskActivity() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", CreateTaskActivity.class);
        solo.enterText((EditText) solo.getView(R.id.AddTitle), testingUS010101.getTitle());
        solo.enterText((EditText) solo.getView(R.id.AddDescription), testingUS010101.getDescription());
        View fab = getActivity().findViewById(R.id.fab);
        solo.clickOnView(fab);
        solo.assertCurrentActivity("Wrong activity", MainMenuActivity.class);
//        ElasticSearchController.GetTask getTask= new ElasticSearchController.GetTask();
//        getTask.execute(testingUS010101.getId());
//        try{
//            createdTask=getTask.get();
//            Log.e("Get test task",getTask.get().getTitle());
//        }
//        catch (Exception e){
//            Log.e("Get test task","failed");
//        }
//        if(!createdTask.equals(testingUS010101)){
//            Log.e("Testing Case US.01.01.01","Failed");
//        }
    }





}




