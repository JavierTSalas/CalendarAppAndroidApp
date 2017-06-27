/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.CheckExpandableRecyclerView;

import android.view.View;
import android.widget.Checkable;

import com.salas.javiert.magicmirror.R;
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder;

/**
 * Created by javi6 on 6/26/2017.
 */

public class myCheckableChildViewHolder extends CheckableChildViewHolder {
    private android.widget.CheckBox CheckBox;


    public myCheckableChildViewHolder(View itemView) {
        super(itemView);
        CheckBox = (android.widget.CheckBox) itemView.findViewById(R.id.checkBox);
    }

    @Override
    public Checkable getCheckable() {
        return CheckBox;
    }
}
