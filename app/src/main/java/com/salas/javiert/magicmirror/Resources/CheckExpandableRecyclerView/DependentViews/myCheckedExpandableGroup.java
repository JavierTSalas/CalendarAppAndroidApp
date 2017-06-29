/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.CheckExpandableRecyclerView.DependentViews;

import android.os.Parcel;

import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;

import java.util.List;

/**
 * Created by javi6 on 6/26/2017.
 */

public class myCheckedExpandableGroup extends CheckedExpandableGroup {
    private String myTitle;
    private List myList;
    protected myCheckedExpandableGroup(Parcel in) {
        super(in);
    }

    public myCheckedExpandableGroup(String title, List items) {
        super(title, items);
        myTitle = title;

        myList = items;
    }

    public String getMyTitle() {
        return myTitle;
    }

    public List getMyList() {
        return myList;
    }

    @Override
    public void checkChild(int childIndex) {
        super.checkChild(childIndex);
    }

    @Override
    public void unCheckChild(int childIndex) {
        super.unCheckChild(childIndex);
    }

    @Override
    public boolean isChildChecked(int childIndex) {
        return super.isChildChecked(childIndex);
    }

    @Override
    public void clearSelections() {
        super.clearSelections();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void onChildClicked(int childIndex, boolean checked) {

    }
}
