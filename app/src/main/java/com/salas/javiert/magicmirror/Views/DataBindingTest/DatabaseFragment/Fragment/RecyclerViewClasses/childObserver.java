/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Views.DataBindingTest.DatabaseFragment.Fragment.RecyclerViewClasses;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.salas.javiert.magicmirror.Resources.Room.serverAddress.Entities.serverAddressItem;


/**
 * Created by javi6 on 7/26/2017.
 */

public class childObserver extends BaseObservable {
    private int id;
    private String name, subtitle;
    private Boolean connectionSuccessful, lockStatus;

    public childObserver(int id, String title, String subtitle, Boolean connectionSuccessful, Boolean lockStatus) {
        this.id = id;
        this.name = title;
        this.subtitle = subtitle;
        this.connectionSuccessful = connectionSuccessful;
        this.lockStatus = lockStatus;
    }

    public childObserver(serverAddressItem item) {
        this.id = item.getId();
        this.name = item.getName();
        this.subtitle = item.getSubtitle();
        this.connectionSuccessful = item.getConnectionSuccessful();
        this.lockStatus = item.getLockStatus();
    }


    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyChange(); //TODO Not this
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyChange();
    }

    @Bindable
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        notifyChange();
    }

    @Bindable
    public Boolean getConnectionSuccessful() {
        return connectionSuccessful;
    }

    public void setConnectionSuccessful(Boolean connectionSuccessful) {
        this.connectionSuccessful = connectionSuccessful;
        notifyChange();
    }

    @Bindable
    public Boolean getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Boolean lockStatus) {
        this.lockStatus = lockStatus;
        notifyChange();
    }


}
