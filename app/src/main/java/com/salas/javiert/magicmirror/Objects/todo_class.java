package com.salas.javiert.magicmirror.Objects;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by javi6 on 6/10/2017.
 */

public class todo_class {

    public CharSequence todo_name;
    private Integer todo_id;
    private Boolean school, done;
    private Date due;

    public todo_class(int todo_id, boolean school, boolean done, String todo_name, String due) {
        this.todo_id = todo_id;
        this.school = school;
        this.done = done;
        this.todo_name = todo_name;
        SimpleDateFormat DateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.due = DateFormatDate.parse(due);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject ReturnObject = new JSONObject();
        if (todo_id != null)
            ReturnObject.put("todo_id", todo_id);
        if (todo_name != null)
            ReturnObject.put("todo_name", todo_name);
        SimpleDateFormat DateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
        if (due != null)
            ReturnObject.put("due", DateFormatDate.format(due));
        if (done != null)
            ReturnObject.put("done", done);
        if (school != null)
            ReturnObject.put("school", school);

        Log.d("todo_class", "Created a JSONObject with todo_id" + todo_id);
        return ReturnObject;
    }
    //TODO: Do this
}
