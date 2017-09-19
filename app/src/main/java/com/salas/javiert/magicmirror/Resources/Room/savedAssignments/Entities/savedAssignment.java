/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.savedAssignments.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.salas.javiert.magicmirror.Objects.bindableObjects.bindableAssignment;

/**
 * Created by javi6 on 8/4/2017.
 */

@Entity(tableName = "saved_assignments")
public class savedAssignment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int classId;
    private long dueTime, CompletionTime, assignedTime;
    private String name, desc;
    private boolean completed;

    public savedAssignment(bindableAssignment item) {
        this.id = item.getId();
        this.classId = item.getClassId();
        this.dueTime = item.getDueTime();
        this.assignedTime = item.getAssignedTime();
        this.name = item.getName();
        this.completed = item.isCompleted();
        this.desc = item.getDesc();
    }

    public savedAssignment() {

    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public long getDueTime() {
        return dueTime;
    }

    public void setDueTime(long dueTime) {
        this.dueTime = dueTime;
    }

    public long getCompletionTime() {
        return CompletionTime;
    }

    public void setCompletionTime(long completionTime) {
        CompletionTime = completionTime;
    }

    public long getAssignedTime() {
        return assignedTime;
    }

    public void setAssignedTime(long assignedTime) {
        this.assignedTime = assignedTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
