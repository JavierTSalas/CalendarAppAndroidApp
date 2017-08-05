/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.assignments.savedEvent.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by javi6 on 8/4/2017.
 */


@Entity(tableName = "saved_events")
public class savedEvent {
    @PrimaryKey()
    private int id;
    private int savedAsssignmentId;
    private int color;
    private long eventTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSavedAsssignmentId() {
        return savedAsssignmentId;
    }

    public void setSavedAsssignmentId(int savedAsssignmentId) {
        this.savedAsssignmentId = savedAsssignmentId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }
}
