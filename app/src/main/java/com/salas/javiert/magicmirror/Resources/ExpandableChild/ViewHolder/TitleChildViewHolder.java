package com.salas.javiert.magicmirror.Resources.ExpandableChild.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.salas.javiert.magicmirror.R;

/**
 * Created by javi6 on 6/4/2017.
 */

public class TitleChildViewHolder extends ChildViewHolder {
    public TextView tvTitle, tvTime;

    public TitleChildViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tvChild);
        tvTime = (TextView) itemView.findViewById(R.id.tvTime);
    }
}
