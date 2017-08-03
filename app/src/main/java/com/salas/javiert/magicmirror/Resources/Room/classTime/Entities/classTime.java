/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.classTime.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by Javier on 8/2/2017.
 */

@Entity(tableName = "classes")
public class classTime {
    @PrimaryKey
    private int id;
    private int class_id, day_of_week, room;
    private String building;
    private Date time_start, time_end;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Date getTime_start() {
        return time_start;
    }

    public void setTime_start(Date time_start) {
        this.time_start = time_start;
    }

    public Date getTime_end() {
        return time_end;
    }

    public void setTime_end(Date time_end) {
        this.time_end = time_end;
    }
}
