/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.connection.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton.connectionSettings;

/**
 * Created by javi6 on 7/16/2017.
 */

@Entity(tableName = "connection")
public class connectionDataBaseItem {
    @PrimaryKey
    private int id;
    private String name;
    private String subtitle;
    private Boolean connectionSuccessful;
    private Boolean lockStatus;

    public connectionDataBaseItem(int id, String name, String subtitle, Boolean connectionSuccessful, Boolean lockStatus) {
        this.id = id;
        this.name = name;
        this.subtitle = subtitle;
        this.connectionSuccessful = connectionSuccessful;
        this.lockStatus = lockStatus;
    }

    public connectionDataBaseItem(connectionSettings connSettings) {
        this.id = connSettings.getId();
        this.name = connSettings.getTitle();
        this.subtitle = connSettings.getSubtext();
        this.connectionSuccessful = connSettings.getConnectionSuccessful();
        this.lockStatus = connSettings.getisLocked();
    }

    public connectionDataBaseItem(Integer index) {
        this.id = index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Boolean getConnectionSuccessful() {
        return connectionSuccessful;
    }

    public void setConnectionSuccessful(Boolean connectionSuccessful) {
        this.connectionSuccessful = connectionSuccessful;
    }

    public Boolean getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Boolean lockStatus) {
        this.lockStatus = lockStatus;
    }

}
