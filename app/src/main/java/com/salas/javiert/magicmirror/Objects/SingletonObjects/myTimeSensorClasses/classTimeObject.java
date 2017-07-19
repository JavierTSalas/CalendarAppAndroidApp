/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Objects.SingletonObjects.myTimeSensorClasses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by javi6 on 7/2/2017.
 */

public class classTimeObject {
    private int id, class_id, day_of_week, room;
    private String building;
    private Date time_start, time_end;


    public classTimeObject(int id, int class_id, String day_of_week, String time_start, String time_end, String building, int room) throws ParseException {
        this.id = id;
        this.class_id = class_id;
        // Convert form String to int
        this.day_of_week = DOW_StringToInt(day_of_week);
        this.building = building;

        this.room = room;
        //Convert the strings to DateTime
        SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
        this.time_start = spf.parse(time_start);
        this.time_start = spf.parse(time_end);
    }

    public int getClass_id() {
        return class_id;
    }

    // Convert the NMTWHFS format to int
    private int DOW_StringToInt(String day_of_week) {
        /*
        All of the return format will be appended with a 1 to prevent leading zeros from being dropped
        ex:
        10000000 is no days of the week
        10101011 Monday Wednesday Friday Saturday
         */

        int runningSum = 0;
        if (day_of_week.contains("N")) {
            runningSum = +1000000;
        }
        if (day_of_week.contains("M")) {
            runningSum = +100000;
        }
        if (day_of_week.contains("T")) {
            runningSum = +10000;
        }
        if (day_of_week.contains("W")) {
            runningSum = +1000;
        }
        if (day_of_week.contains("R")) {
            runningSum = +100;
        }
        if (day_of_week.contains("F")) {
            runningSum = +10;
        }
        if (day_of_week.contains("S")) {
            runningSum = +1;
        }

        return runningSum;
    }

    //Return if the we should suggest this class to be shown
    public boolean shouldSuggest() {
        return isFifteenMinutesAfter() || isOccuring();
    }

    // Checks if the current time is 15 minutes after the current class
    private boolean isFifteenMinutesAfter() {
        Calendar DueDate = Calendar.getInstance();
        DueDate.setTime(time_end);
        Calendar RightNow = Calendar.getInstance();

        long MinutesBetween = 0;
        while (RightNow.before(DueDate)) {
            RightNow.add(Calendar.MINUTE, 1);
            MinutesBetween++;
        }
        return ((MinutesBetween <= 15));
    }

    // Checks if the class is occurring right now
    private boolean isOccuring() {
        Calendar RightNow = Calendar.getInstance();
        Date Now = RightNow.getTime();

        if (Now != time_end && Now != time_start) {
            if (Now.before(time_end) && Now.after(time_start)) {
                return true;
            }
        }

        return false;
    }
}
