/*
 * Copyright (c) 2017. Javier Salas
 * This class is the Parent that holds the children.
 * This class is responsible for interfacing with the view that will be clicked to expand the children
 */

package com.salas.javiert.magicmirror.Resources.CheckExpandableRecyclerView.DependentViews;

import android.view.View;
import android.widget.TextView;

import com.salas.javiert.magicmirror.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

/**
 * Created by javi6 on 6/27/2017.
 */

public class myGroupViewHolder extends GroupViewHolder {

    private TextView textView;

    public myGroupViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.tvParent);
    }

    public void setTextView(ExpandableGroup group) {
        textView.setText(group.getTitle());
    }
}
