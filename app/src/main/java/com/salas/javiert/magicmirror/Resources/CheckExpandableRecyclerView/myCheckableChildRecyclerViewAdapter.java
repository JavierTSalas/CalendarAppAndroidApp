/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.CheckExpandableRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salas.javiert.magicmirror.Objects.myQueueClasses.myQueueItem;
import com.salas.javiert.magicmirror.Objects.myQueueClasses.mySendQueue;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.CheckExpandableRecyclerView.DependentViews.myCheckableChildViewHolder;
import com.salas.javiert.magicmirror.Resources.CheckExpandableRecyclerView.DependentViews.myGroupViewHolder;
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by javi6 on 6/27/2017.
 */

public class myCheckableChildRecyclerViewAdapter extends CheckableChildRecyclerViewAdapter<myGroupViewHolder, myCheckableChildViewHolder> {

    public myCheckableChildRecyclerViewAdapter(List<? extends CheckedExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public myCheckableChildViewHolder onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkable_row, parent, false);

        return new myCheckableChildViewHolder(view);
    }

    @Override
    public void onBindCheckChildViewHolder(myCheckableChildViewHolder holder, int flatPosition, CheckedExpandableGroup group, int childIndex) {
        mySendQueue mySendQueue = new mySendQueue();
        final myQueueItem myQueueItem = (myQueueItem) group.getItems().get(childIndex);
        holder.setCheckedTextView(myQueueItem.toString());
    }

    @Override
    public myGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_parent, parent, false);
        return new myGroupViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(myGroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTextView(group);
    }


}
