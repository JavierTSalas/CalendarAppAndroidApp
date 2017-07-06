/*
 * Copyright (c) 2017. Javier Salas
 * This class is the child for each row
 * This class interfaces with each myQueueTask
 */

package com.salas.javiert.magicmirror.Views.CheckExpandableRecyclerView.DependentViews;

import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.salas.javiert.magicmirror.R;
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder;


/**
 * Created by javi6 on 6/26/2017.
 */

@SuppressWarnings("ResourceType")
public class myCheckableChildViewHolder extends CheckableChildViewHolder {
    private CheckedTextView ctvName;
    private TextView tvTimeLeft;


    public myCheckableChildViewHolder(View itemView) {
        super(itemView);
        ctvName = (CheckedTextView) itemView.findViewById(R.id.tvCheckable);
        tvTimeLeft = (TextView) itemView.findViewById(R.id.tvTimeLeftCheckable);
    }

    @Override
    public Checkable getCheckable() {
        return ctvName;
    }

    public void showCheckbox() {
        //Use this trick to get a system checkbox
        int[] attrs = {android.R.attr.listChoiceIndicatorMultiple};
        ctvName.setCheckMarkDrawable(ctvName.getContext().obtainStyledAttributes(attrs).getDrawable(0));
    }

    public void hideCheckbox() {
        ctvName.setCheckMarkDrawable(null);
    }

    public void setCtvName(String Text) {
        ctvName.setText(Text);
    }

    public void showTimeLeft() {
        tvTimeLeft.setVisibility(View.VISIBLE);
    }

    public void hideTimeLeft() {
        tvTimeLeft.setVisibility(View.INVISIBLE);
    }

    public void setTimeLeft(String s) {
        tvTimeLeft.setText(s);
    }
}
