package com.salas.javiert.magicmirror.Resources.ExpandableChild.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.salas.javiert.magicmirror.R;

/**
 * Created by javi6 on 6/4/2017.
 */

public class TitleParentViewHolder extends ParentViewHolder {
    public TextView tvParent;


    public TitleParentViewHolder(View itemView) {
        super(itemView);
        tvParent = (TextView) itemView.findViewById(R.id.tvParent);
    }
}
