package com.salas.javiert.magicmirror.Objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.salas.javiert.magicmirror.DatabaseRestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by javi6 on 6/9/2017.
 */

public class myQueue {

    private static myQueue instance = null;

    private ArrayList<myQueueTask> QueueTaskList = new ArrayList();

    //This is the finalized form of the data that will be sent
    private ArrayList<information> actions = new ArrayList();

    private myQueue() {
    }

    //Everytime you need an instance, call this
    public static myQueue getInstance() {
        if (instance == null)
            instance = new myQueue();

        return instance;
    }

    public void addQueueTask(myQueueTask Task) {
        QueueTaskList.add(Task);
    }

    public void addQueueItem(myQueueItem Item) {
        int Position = findCorrectQueueTask(Item);
        QueueTaskList.get(Position).append(Item);
    }

    private int findCorrectQueueTask(myQueueItem item) {
        for (int i = 0; i < QueueTaskList.size(); i++)
            if (QueueTaskList.get(i).MatchingEnums(item)) {
                Log.d("myQueue", "Was able to find a QueueTask with matching enums." + i + " Appending item");
                return i;
            }
        addQueueTask(new myQueueTask(item));
        Log.d("myQueue", "Was not able to find a QueueTask with matchig enums. Creating new QueueTask with item");
        return (QueueTaskList.size() - 1); //Minus one since the size of a list is n+1 indexes
    }

    //Populates the actions list with a information.java class that is unique (Due to the nature of myQueueTask) in action and table
    //The information class contains the url that it should be sent to as well as the StringEntity
    private void CreateStringEntitiy(myQueueTask Task) throws UnsupportedEncodingException, JSONException {
        String QueueURLDestination, QueueAction;
        QueueURLDestination = null;
        QueueAction = null;
        if (Task.getMyTable() == myQueueItem.TABLE.TODO)
            QueueURLDestination = "interfacetodo.php";
        if (Task.getMyTable() == myQueueItem.TABLE.ASSIGNMENTS)
            QueueURLDestination = "interfaceassignments.php";
        if (Task.getMyTable() == myQueueItem.TABLE.OCCUR)
            QueueURLDestination = "interfaceoccur.php";

        if (Task.getMyMode() == myQueueItem.MODE.ADD)
            QueueAction = "ADD";
        if (Task.getMyMode() == myQueueItem.MODE.REMOVE)
            QueueAction = "REMOVE";
        if (Task.getMyMode() == myQueueItem.MODE.EDIT)
            QueueAction = "EDIT";
        if (Task.getMyMode() == myQueueItem.MODE.SET_DONE)
            QueueAction = "SET_DONE";


        JSONObject ParamObject = new JSONObject();
        ParamObject.put(QueueAction, Task.getJsonList());
        StringEntity myEntitiy = new StringEntity(ParamObject.toString());
        myEntitiy.setContentEncoding("UTF-8");
        myEntitiy.setContentType("application/json");
        actions.add(new information(QueueURLDestination, myEntitiy));

    }


    public void SendInformation(Context context) {
        TextHttpResponseHandler response = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.d("suc", responseString);
            }
        };

        for (int i = 0; i < actions.size(); i++)
            DatabaseRestClient.post(context, actions.get(i).getUrl(), actions.get(i).getMyEntity(), "application/x-www-form-urlencoded", response);
    }

    public List<String> createListString() {
        List<String> myStringList = new ArrayList<String>();
        if (QueueTaskList.size() < 0) {


            for (int index = 0; index < QueueTaskList.size(); index++) {
                myStringList.add(QueueTaskList.get(index).toString());
            }

        } else {
            myStringList.add("Nothing in queue");
        }
        return myStringList;

    }

    public void loadMyQueue(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        //TODO: Make this read "Please add an item to the queue"
        String json = appSharedPrefs.getString("QUEUE_DATA", "");
        Log.d("Loading", json);
        Type type = new TypeToken<List<myQueueTask>>() {
        }.getType();
        ArrayList<myQueueTask> QUEUE_DATA_FROM_PREFERENCES = gson.fromJson(json, type);
        myQueue.getInstance().setList(QUEUE_DATA_FROM_PREFERENCES);
        Log.d("SharedPref", "Reading Queue to SharedPreferences");
    }


    public void saveMyQueue(Context context) {


        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        ArrayList<myQueueTask> QUEUE_DATA_FROM_PREFERENCES = com.salas.javiert.magicmirror.Objects.myQueue.getInstance().getList();
        String json = gson.toJson(QUEUE_DATA_FROM_PREFERENCES);
        prefsEditor.putString("QUEUE_DATA", json);
        Log.d("Saving", json);
        prefsEditor.commit();
        Log.d("SharedPref", "Saving Queue to SharedPreferences");
    }

    public int getTaskCount() {
        int runningCount = 0;
        for (int i = 0; i < QueueTaskList.size(); i++)
            for (int j = 0; j < QueueTaskList.get(i).getObjectList().size(); j++)
                runningCount++;

        Log.d("myQueue", "Count of tasks: " + runningCount);
        return runningCount;
    }

    public ArrayList<myQueueTask> getList() {
        return QueueTaskList;
    }

    public void setList(ArrayList<myQueueTask> newList) {
        this.QueueTaskList = newList;
    }


    /*

        JSONArray Assignments = new JSONArray();
        Assignments.put(newAssign.toJSONObject());

        ^^This part is done by myQueueTask class

        vv This part is done by this class vv

        JSONObject ParamObject = new JSONObject();
        ParamObject.put("Assignments", Assignments);


        vv A loop will do this part vv


        StringEntity myEntitiy = new StringEntity(ParamObject.toString());
        myEntitiy.setContentEncoding("UTF-8");
        myEntitiy.setContentType("application/json");


        DatabaseRestClient.post(this, "input.php", myEntitiy, "application/x-www-form-urlencoded", response);


     */

}
