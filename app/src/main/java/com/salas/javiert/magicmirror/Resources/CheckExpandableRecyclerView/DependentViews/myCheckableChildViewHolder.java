/*
 * Copyright (c) 2017. Javier Salas
 * This class is the child for each row
 * This class interfaces with each myQueueTask
 */

package com.salas.javiert.magicmirror.Resources.CheckExpandableRecyclerView.DependentViews;

import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;

import com.salas.javiert.magicmirror.R;
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder;

/**
 * Created by javi6 on 6/26/2017.
 */

public class myCheckableChildViewHolder extends CheckableChildViewHolder {
    private CheckedTextView checkedTextView;


    public myCheckableChildViewHolder(View itemView) {
        super(itemView);
        checkedTextView = (CheckedTextView) itemView.findViewById(R.id.tvCheckable);
    }

    @Override
    public Checkable getCheckable() {
        return checkedTextView;
    }


    public void setCheckedTextView(String Text) {
        checkedTextView.setText(Text);
    }

}
