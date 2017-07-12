/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Views.CheckExpandableRecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salas.javiert.magicmirror.Objects.SingletonObjects_REMOVE_ME.myQueueClasses.myQueueItem;
import com.salas.javiert.magicmirror.Objects.helperObjects.assignment_class;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Views.CheckExpandableRecyclerView.DependentViews.myCheckableChildViewHolder;
import com.salas.javiert.magicmirror.Views.CheckExpandableRecyclerView.DependentViews.myGroupViewHolder;
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by javi6 on 6/27/2017.
 */

public class myCheckableChildRecyclerViewAdapter extends CheckableChildRecyclerViewAdapter<myGroupViewHolder, myCheckableChildViewHolder> {

    private static boolean visible = false;

    public myCheckableChildRecyclerViewAdapter(List<? extends CheckedExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public myCheckableChildViewHolder onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_rows_checkable, parent, false);

        return new myCheckableChildViewHolder(view);

    }

    @Override
    public void onBindCheckChildViewHolder(myCheckableChildViewHolder holder, int flatPosition, CheckedExpandableGroup group, int childIndex) {
        final myQueueItem myQueueItem = (myQueueItem) group.getItems().get(childIndex);
        holder.setCtvName(myQueueItem.toString());

        //Set the visibility of the checkBox
        if (visible) {
            holder.showCheckbox();
            holder.setTimeLeft(((assignment_class) myQueueItem.getO()).TimeLeft());
            holder.showTimeLeft();
            Log.d(this.getClass().toString(), "Showing Checkboxes");
        }
        if (!visible) {
            holder.hideCheckbox();
            holder.hideTimeLeft();
            Log.d(this.getClass().toString(), "Hiding Checkboxes");
        }
    }

    @Override
    public myGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_list_parent, parent, false);
        return new myGroupViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(myGroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTextView(group);
    }

    public boolean isCheckBoxVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        myCheckableChildRecyclerViewAdapter.visible = visible;
    }
}

