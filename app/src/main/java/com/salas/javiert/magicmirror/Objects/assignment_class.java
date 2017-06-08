package com.salas.javiert.magicmirror.Objects;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by javi6 on 5/27/2017.
 */

public class assignment_class {
    public Integer ass_id;
    public CharSequence ass_name;
    public Integer class_id;
    public Date date_assigned;
    public Date due;
    public Boolean done;
    public Integer weight;

    public assignment_class(Integer a1, CharSequence a2, Integer a3, String a4, String a5, String a6, Integer a7) {
        this.ass_id = a1;
        this.ass_name = a2;
        this.class_id = a3;

        // We have to convert a4 to Date as it is passed as sting
        SimpleDateFormat parser_date = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.date_assigned = parser_date.parse(a4);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //We have to convert a5 to Date as it is passed as a string
        SimpleDateFormat parser_datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.due = parser_datetime.parse(a5);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.done = Boolean.parseBoolean(a6);
        this.weight = a7;

        Log.d("Classes", "assignment_class: built");
    }

    public assignment_class() {
        Log.d("Classes", "assignment_class: built");
    }

    //TODO: Make this return a Date so that a countdown can be initialized by the ChildView. Furthermore passing this as a string as a date can be changed easier  if need be```````````
    public assignment_class(String s) {
        this.ass_name = s;
        Log.d("Classes", "assignment_class: built");
    }

    public assignment_class(String s, int i) {
        this.ass_name = s;
        this.class_id = i;
        Log.d("Classes", "assignment_class: built");
    }

    public static long hoursBetween(Date Due) {
        Calendar DueDate = Calendar.getInstance();
        DueDate.setTime(Due);
        Calendar RightNow = Calendar.getInstance();

        long daysBetween = 0;
        while (RightNow.before(DueDate)) {
            RightNow.add(Calendar.MINUTE, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    @Override
    public String toString() {
        return "assignment_class{" +
                "ass_id=" + ass_id +
                ", ass_name=" + ass_name +
                ", class_id=" + class_id +
                ", date_assigned=" + date_assigned +
                ", due=" + due +
                ", done=" + done +
                ", weight=" + weight +
                '}';
    }

    public String TimeLeft() {

        try {
            due.getTime();
        } catch (Exception e) {
            return "N/A";
        }

        long MinutesUntilDue = hoursBetween(due);

        double HoursLeft, DaysLeft = 0;

        HoursLeft = Math.floor(MinutesUntilDue / 60);
        DaysLeft = Math.floor(HoursLeft / 24);

        //If there is more than a day left we want to display in the format
        // 1d2hr
        if (DaysLeft > 0) {
            return ((int) DaysLeft) + "d" + ((int) HoursLeft % 24) + "hr";
        }

        //If there is more than a hour left but less than a day we want to display in the format
        // 3hr4min
        if (HoursLeft > 0) {
            return ((int) HoursLeft) + "hr" + ((int) (MinutesUntilDue - DaysLeft * 24 * 60 - HoursLeft * 60)) + "min";
        }

        // Due date has already passed
        if (MinutesUntilDue == 0)
            return "PAST DUE";
        return null;
    }
}
