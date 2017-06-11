package com.salas.javiert.magicmirror.Objects;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by javi6 on 6/10/2017.
 */

public class myQueueItem {
    public JSONObject myJSONObject;
    private MODE myMode;
    private TABLE myTable;

    public myQueueItem(Object o) throws JSONException {
        myTable = DeduceTable(o);
        myMode = MODE.NULL;
        myJSONObject = getJsonObject(o);
    }

    public MODE getMyMode() {
        return myMode;
    }

    public TABLE getMyTable() {
        return myTable;
    }

    //Return list, Logs if there is a error
    public JSONObject getJsonObject(Object o) throws JSONException {
        if (myTable == TABLE.ASSIGNMENTS)
            return ((assignment_class) o).toJSONObject();
        if (myTable == TABLE.OCCUR)
            return ((occur_class) o).toJSONObject();
        if (myTable == TABLE.TODO)
            return ((todo_class) o).toJSONObject();

        return null;
    }

    //Deduces the ENUM type
    private TABLE DeduceTable(Object o) {
        if (o instanceof assignment_class) {
            Log.d("DeduceTable", "Successfully deduced object of type assignment_class");
            return TABLE.ASSIGNMENTS;
        }
        if (o instanceof occur_class) {
            Log.d("DeduceTable", "Successfully deduced object of type occur_class");
            return TABLE.OCCUR;
        }
        if (o instanceof todo_class) {
            Log.d("DeduceTable", "Successfully deduced object of type todo_class");
            return TABLE.TODO;
        }

        Log.d("DeduceTable", "Was not able to decude the type...Prepare for errors");
        return TABLE.NULL;
    }

    public void setMode(String s) {
        switch (s) {
            case "EDIT":
                this.myMode = MODE.EDIT;
                break;
            case "ADD":
                this.myMode = MODE.ADD;
                break;
            case "REMOVE":
                this.myMode = MODE.REMOVE;
                break;
            case "SET_DONE":
                this.myMode = MODE.SET_DONE;
                break;
        }

    }

    //Possible actions that the myQueueTask will do
    public enum MODE {
        EDIT, ADD, REMOVE, SET_DONE, NULL
    }

    public enum TABLE {
        ASSIGNMENTS, OCCUR, TODO, NULL
    }

}
