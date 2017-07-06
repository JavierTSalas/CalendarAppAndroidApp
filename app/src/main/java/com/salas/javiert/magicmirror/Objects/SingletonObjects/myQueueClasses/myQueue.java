package com.salas.javiert.magicmirror.Objects.SingletonObjects.myQueueClasses;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.TextHttpResponseHandler;
import com.salas.javiert.magicmirror.Objects.helperObjects.sendToServerObject;
import com.salas.javiert.magicmirror.Resources.DatabaseRestClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javi6 on 6/9/2017.
 */

public class myQueue {

    private static myQueue instance = null;

    private ArrayList<myQueueTask> QueueTaskList = new ArrayList();

    //This is the finalized form of the data that will be sent
    private ArrayList<sendToServerObject> actions = new ArrayList();

    protected myQueue() {
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

    //Find the correct position in QueueTaskList and appends it
    public void addQueueItem(myQueueItem Item) {
        int Position = findCorrectQueueTask(Item);
        QueueTaskList.get(Position).append(Item);
    }

    //Find the correct position in QueueTaskList and removes it
    public void removeQueueItem(myQueueItem item) {
        int position = findCorrectQueueTask(item);
        QueueTaskList.get(position).remove(item);
        cleanList();
    }

    //Removes the myQueueItem from a myQueueTask
    private void removeFromQueueTask(myQueueTask myTask, myQueueItem item) {
        for (int index = 0; index < myTask.getMyTaskList().size(); index++) {

        }
    }

    //Returns the index of QueueTaskList that has the same enums as Item
    private int findCorrectQueueTask(myQueueItem item) {

        if (QueueTaskList == null) {
            QueueTaskList = new ArrayList<>();
        }
        for (int index = 0; index < QueueTaskList.size(); index++)
            if (QueueTaskList.get(index).MatchingEnums(item)) {
                return index;
            }
        addQueueTask(new myQueueTask(item));
        return (QueueTaskList.size() - 1); //Minus one since the size of a list is n+1 indexes
    }

    //Not used
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

    //TODO: Get this to work
    public synchronized void loadMyQueue(Context context) {
       /* SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        //TODO: Make this read "Please add an item to the queue" the queue is empty
        String json = appSharedPrefs.getString("QUEUE_DATA", "");
        Log.d("Loading", json);

        Type type = new TypeToken<List<myQueueTask>>() {}.getType();
        ArrayList<myQueueTask> QUEUE_DATA_FROM_PREFERENCES = gson.fromJson(json, type);
        myQueue.getInstance().setList(QUEUE_DATA_FROM_PREFERENCES);

        myQueue.getInstance().setInstance(gson.fromJson(json, myQueue.class));
        Log.d("SharedPref", "Reading Queue to SharedPreferences");*/
    }

    //TODO: Get this to work
    public synchronized void saveMyQueue(Context context) {
/*

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();

        ArrayList<myQueueTask> QUEUE_DATA_FROM_PREFERENCES = myQueue.getInstance().getList();
        String json = gson.toJson(QUEUE_DATA_FROM_PREFERENCES);

        //String json = gson.toJson(myQueue.getInstance());
        prefsEditor.putString("QUEUE_DATA", json);
        Log.d("Saving", json);
        prefsEditor.commit();
        Log.d("SharedPref", "Saving Queue to SharedPreferences");*/
    }

    //Return task count
    public int getTaskCount() {
        int runningCount = 0;
        for (int myQueueTaskCount = 0; myQueueTaskCount < QueueTaskList.size(); myQueueTaskCount++)
            for (int myQueueItemCount = 0; myQueueItemCount < QueueTaskList.get(myQueueTaskCount).getObjectList().size(); myQueueItemCount++)
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

    //If an myQueueTask is empty in myQueueTasList then it is deleted
    private void cleanList() {
        for (int index = 0; index < QueueTaskList.size(); index++) {
            if (QueueTaskList.get(index).getMyTaskList().size() == 0)
                QueueTaskList.remove(index);
        }
    }

    //Remove the myQueueTask that matches the passed myQueueTask
    public void removeQueueTask(List<myQueueTask> myQueueTaskList) {
        //This function takes an array of myQueueTask and deletes each Item in the myQueue
        //We do it this way since the elements we want to delete may not be all of the elements in myQueueTask
        List<myQueueItem> itemsToRemove = new ArrayList<>();

        //Get all of the tasks
        for (int index = 0; index < myQueueTaskList.size(); index++) {
            for (int task = 0; task < myQueueTaskList.get(index).getMyTaskList().size(); task++)
                itemsToRemove.add(myQueueTaskList.get(index).getMyTaskList().get(task));
        }
        //Remove the elements
        for (myQueueItem item : itemsToRemove)
            getInstance().removeQueueItem(item);
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
