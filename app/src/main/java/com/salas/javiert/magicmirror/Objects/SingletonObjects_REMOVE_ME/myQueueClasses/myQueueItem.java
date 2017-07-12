package com.salas.javiert.magicmirror.Objects.SingletonObjects_REMOVE_ME.myQueueClasses;

import android.util.Log;

import com.salas.javiert.magicmirror.Objects.helperObjects.assignment_class;
import com.salas.javiert.magicmirror.Objects.helperObjects.occur_class;
import com.salas.javiert.magicmirror.Objects.helperObjects.todo_class;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by javi6 on 6/10/2017.
 */

public class myQueueItem {
    public JSONObject myJSONObject;
    private Object o;
    private MODE myMode;
    private TABLE myTable;

    public myQueueItem(Object o) throws JSONException {
        this.o = o;
        myTable = DeduceTable(o);
        myMode = MODE.NULL;
        myJSONObject = setJsonObject(o);
    }

    public MODE getMyMode() {
        return myMode;
    }

    public TABLE getMyTable() {
        return myTable;
    }

    public Object getO() {
        return o;
    }

    public JSONObject getMyJSONObject() {
        return myJSONObject;
    }

    //Return list, Logs if there is a error
    private JSONObject setJsonObject(Object o) throws JSONException {
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

    public String toString() {
        if (myTable == TABLE.ASSIGNMENTS)
            return ((assignment_class) o).ass_name.toString();
        if (myTable == TABLE.OCCUR)
            return ((occur_class) o).occur_name.toString();
        if (myTable == TABLE.TODO)
            return ((todo_class) o).todo_name.toString();
        return null;
    }

    //Possible actions that the myQueueTask will do
    public enum MODE {
        EDIT, ADD, REMOVE, SET_DONE, NULL
    }

    public enum TABLE {
        ASSIGNMENTS, OCCUR, TODO, NULL
    }

}
