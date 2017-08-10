/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Objects.bindableObjects;

import android.databinding.BaseObservable;

import com.salas.javiert.magicmirror.Resources.Room.assignments.savedAssignments.Entities.savedAssignment;

/**
 * Created by javi6 on 8/6/2017.
 */

public class bindableAssignment extends BaseObservable {
    private int id;
    private int classId, reminderID;
    private long dueTime, CompletionTime, assignedTime;
    private String name;
    private boolean completed;

    public bindableAssignment(savedAssignment item) {
        this.id = item.getId();
        this.classId = item.getClassId();
        this.dueTime = item.getDueTime();
        this.assignedTime = item.getAssignedTime();
        this.name = item.getName();
        this.completed = this.isCompleted();
    }

    public bindableAssignment() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyChange(); //TODO Not this
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
        notifyChange(); //TODO Not this
    }

    public long getDueTime() {
        return dueTime;
    }

    public void setDueTime(long dueTime) {
        this.dueTime = dueTime;
        notifyChange(); //TODO Not this
    }

    public long getCompletionTime() {
        return CompletionTime;
    }

    public void setCompletionTime(long completionTime) {
        CompletionTime = completionTime;
        notifyChange(); //TODO Not this
    }

    public long getAssignedTime() {
        return assignedTime;
    }

    public void setAssignedTime(long assignedTime) {
        this.assignedTime = assignedTime;
        notifyChange(); //TODO Not this
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyChange(); //TODO Not this
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        notifyChange(); //TODO Not this
    }

}
