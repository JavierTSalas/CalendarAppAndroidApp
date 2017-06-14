package com.salas.javiert.magicmirror.Objects;

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
    // This list will contain the list of the JSONObjects
    private JSONArray myList = new JSONArray();
    private ArrayList<Object> myObjectList = new ArrayList<Object>() {
    };

    public myQueueTask(myQueueItem Item) {
        myMode = Item.getMyMode();
        myTable = Item.getMyTable();
    }

    public boolean MatchingEnums(myQueueItem Item) {
        return myMode == Item.getMyMode() && myTable == Item.getMyTable();
    }

    public myQueueItem.MODE getMyMode() {
        return myMode;
    }

    public myQueueItem.TABLE getMyTable() {
        return myTable;
    }

    public JSONArray getJsonList() {
        return myList;
    }

    public List<?> getObjectList() {
        return myObjectList;
    }

    public void append(myQueueItem Item) {
        if (MatchingEnums(Item)) {
            myList.put(Item.myJSONObject);
            myObjectList.add(Item.getO());
        } else {
            Log.d("myQueueTask", "Append failed Enum mismatch");
        }
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

        return myModeString + "__" + myTableString + '(' + myObjectList.size() + ')';

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

        vv This part uses a information.java object vv
        DatabaseRestClient.post(this, "input.php", myEntitiy, "application/x-www-form-urlencoded", response);


     */
}
