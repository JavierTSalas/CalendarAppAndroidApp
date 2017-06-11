package com.salas.javiert.magicmirror.Objects;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by javi6 on 6/10/2017.
 */

public class occur_class {
    public Integer reoccur_id;
    public CharSequence name;
    public Integer days_between;
    public Integer days_of_week;
    public Integer times;
    public Date end_date;
    public Date start_date;
    public Date next_occurance;

    public occur_class(Integer reoccur_id, CharSequence name, Integer days_between, Integer days_of_week, Integer times, Date end_date, Date start_date, Date next_occurance) {
        this.reoccur_id = reoccur_id;
        this.name = name;
        this.days_between = days_between;
        this.days_of_week = days_of_week;
        this.times = times;
        this.end_date = end_date;
        this.start_date = start_date;
        this.next_occurance = next_occurance;

        Log.d("Classes", "occur_class: built");
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject ReturnObject = new JSONObject();
        if (reoccur_id != null)
            ReturnObject.put("reoccur_id", reoccur_id);
        if (name != null)
            ReturnObject.put("name", name);
        if (days_between != null)
            ReturnObject.put("days_between", days_between);
        if (days_of_week != null)
            ReturnObject.put("days_of_week", days_of_week);
        if (times != null)
            ReturnObject.put("times", times);
        SimpleDateFormat DateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
        if (end_date != null)
            ReturnObject.put("end_date", DateFormatDate.format(end_date));
        if (start_date != null)
            ReturnObject.put("start_date", DateFormatDate.format(start_date));
        if (next_occurance != null)
            ReturnObject.put("next_occurance", DateFormatDate.format(next_occurance));

        Log.d("occur_class", "Created a JSONObject with reoccur_id" + reoccur_id);
        return ReturnObject;
    }

    @Override
    public String toString() {
        return "occur_class{" +
                "reoccur_id=" + reoccur_id +
                ", name=" + name +
                ", days_between=" + days_between +
                ", days_of_week=" + days_of_week +
                ", times=" + times +
                ", end_date=" + end_date +
                ", start_date=" + start_date +
                ", next_occurance=" + next_occurance +
                '}';
    }
}
