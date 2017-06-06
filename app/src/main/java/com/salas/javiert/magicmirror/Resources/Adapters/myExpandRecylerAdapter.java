package com.salas.javiert.magicmirror.Resources.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.salas.javiert.magicmirror.Objects.assignment_class;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.ParentViewClass;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.ViewHolder.TitleChildViewHolder;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.ViewHolder.TitleParentViewHolder;

import java.util.List;

/**
 * Created by javi6 on 6/4/2017.
 */

public class myExpandRecylerAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder, TitleChildViewHolder> {

    LayoutInflater mInflater;
    assignment_class myAssignment;

    public myExpandRecylerAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_parent, viewGroup, false);
        return new TitleParentViewHolder(view);
    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_child, viewGroup, false);
        return new TitleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(TitleParentViewHolder titleParentViewHolder, int i, Object o) {
        ParentViewClass title = (ParentViewClass) o;
        titleParentViewHolder.tvParent.setText(title.getTitle());

    }

    @Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHolder, int i, Object o) {

        myAssignment = (assignment_class) o;
        titleChildViewHolder.tvTitle.setText(myAssignment.ass_name);
        titleChildViewHolder.tvTime.setText(myAssignment.TimeLeft());

        titleChildViewHolder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Log.d("onClick", "Child: " + myAssignment.toString());
            }
        });

    }
}
