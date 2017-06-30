package com.salas.javiert.magicmirror.Objects.myQueueClasses;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javi6 on 6/9/2017.
 */

public class myQueueTask {


    // This describes what the myQueueTask will do
    private myQueueItem.MODE myMode;
    private myQueueItem.TABLE myTable;
    private ArrayList<Object> myObjectList = new ArrayList<>();
    private ArrayList<myQueueItem> myTaskList = new ArrayList<>();


    public myQueueTask(myQueueItem Item) {
        myMode = Item.getMyMode();
        myTable = Item.getMyTable();

    }

    public boolean MatchingEnums(myQueueItem Item) {
        return myMode == Item.getMyMode() && myTable == Item.getMyTable();
    }

    public ArrayList<myQueueItem> getMyTaskList() {
        return myTaskList;
    }

    public myQueueItem.MODE getMyMode() {
        return myMode;
    }

    public myQueueItem.TABLE getMyTable() {
        return myTable;
    }

    public JSONArray getJSONArray() {
        // Since our JSONArray information is strictly dependent on the contents of the myTaskList we should generate it everytime it is called to ensure
        // that it reflects the content of that list
        JSONArray myJSONArray = new JSONArray();
        for (myQueueItem task : this.getMyTaskList()) {
            myJSONArray.put(task.getMyJSONObject());
        }
        return myJSONArray;
    }

    public List<?> getObjectList() {
        return myObjectList;
    }

    public void append(myQueueItem Item) {
        if (MatchingEnums(Item)) {
            myObjectList.add(Item.getO());
            myTaskList.add(Item);
            Log.d("myQueueTask", "Append successful! New size:" + myObjectList.size());
        } else {
            Log.d("myQueueTask", "Append failed Enum mismatch");
        }
    }

    public void remove(myQueueItem item) {
        if (myTaskList.contains(item))
            myTaskList.remove(myTaskList.indexOf(item));
        if (myObjectList.contains(item.getO()))
            myObjectList.remove(myObjectList.indexOf(item.getO()));

    }


    @Override
    public String toString() {
        String myModeString, myTableString;
        myModeString = null;
        myTableString = null;

        if (myTable == myQueueItem.TABLE.TODO)
            myTableString = "interfacetodo.php";
        if (myTable == myQueueItem.TABLE.ASSIGNMENTS)
            myTableString = "interfaceassignments.php";
        if (myTable == myQueueItem.TABLE.OCCUR)
            myTableString = "interfaceoccur.php";

        if (myMode == myQueueItem.MODE.ADD)
            myModeString = "ADD";
        if (myMode == myQueueItem.MODE.REMOVE)
            myModeString = "REMOVE";
        if (myMode == myQueueItem.MODE.EDIT)
            myModeString = "EDIT";
        if (myMode == myQueueItem.MODE.SET_DONE)
            myModeString = "SET_DONE";

        return myModeString + "__" + myTableString + '(' + myTaskList.size() + ')';

    }

    //TODO: Throw error when enum is NULL


    /*

        JSONArray Assignments = new JSONArray();
        ^^This part is done by myQueueItem class

        Assignments.put(newAssign.toJSONObject());

        ^^This part is done by myQueueTask class

        vv This part is done by myQueue class vv

        JSONObject ParamObject = new JSONObject();
        ParamObject.put("Assignments", Assignments);
        StringEntity myEntitiy = new StringEntity(ParamObject.toString());
        myEntitiy.setContentEncoding("UTF-8");
        myEntitiy.setContentType("application/json");

        vv This part uses a sendToServerObject.java object vv
        DatabaseRestClient.post(this, "input.php", myEntitiy, "application/x-www-form-urlencoded", response);


     */
}
