package com.salas.javiert.magicmirror.Resources.ExpandableChild.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.salas.javiert.magicmirror.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

//import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

/**
 * Created by javi6 on 6/4/2017.
 */

public class TitleParentViewHolder extends GroupViewHolder {
    public TextView tvParent;


    public TitleParentViewHolder(View itemView) {
        super(itemView);
        tvParent = (TextView) itemView.findViewById(R.id.tvParent);
    }

    public void setText(ExpandableGroup group) {
        tvParent.setText(group.getTitle());
    }
}
