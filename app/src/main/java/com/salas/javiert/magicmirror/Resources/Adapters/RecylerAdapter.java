package com.salas.javiert.magicmirror.Resources.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.salas.javiert.magicmirror.Objects.assignment_class;
import com.salas.javiert.magicmirror.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by javi6 on 6/2/2017.
 */

public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.MyViewHolder> {

    List<?> data = Collections.emptyList();
    Class_type DeterminedType;
    View view;
    private LayoutInflater inflater;

    public RecylerAdapter(Context context, List<assignment_class> data) {
        DeterminedType = Class_type.NAN;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void DetermineType() {
        //Since we don't know the type, we have to deduce it. This is done by comparing the 0th element to expected types
        if (!data.isEmpty() && DeterminedType == Class_type.NAN) {
            if (data.get(0) instanceof assignment_class)
                DeterminedType = Class_type.ASSIGN_CLASS;

        }

        if (DeterminedType == Class_type.NAN)
            Log.d("RecyclerAdapter", "Encountered a unknown class type. You're probably not seeing anything so go to RecyclerAdapter.java to define what you want to view ");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DetermineType();
        switch (DeterminedType) {
            case ASSIGN_CLASS:
                view = inflater.inflate(R.layout.recycler_row, parent, false);
                Log.d("RecyclerAdapter", "Determined that the type of the list was assignment_class");
                break;
            default:

        }
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        switch (DeterminedType) {
            case ASSIGN_CLASS:
                Object object_from_list = data.get(position);
                if (!data.isEmpty())
                    if (data.get(0) instanceof assignment_class) {
                        assignment_class current = (assignment_class) object_from_list;
                        Log.d("OnBind", current.ass_name + " " + current.class_id);
                        holder.title.setText(current.ass_name);
                        holder.image.setText(current.class_id.toString());
                    }
                break;
            default:
                Log.d("RecyclerAdapter", "Encountered a unknown class type. You're probably not seeing anything so go to RecyclerAdapter.java to define what you want to view ");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public enum Class_type {
        ASSIGN_CLASS, NAN
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            //Check if the list isn't empty and that
            if (!data.isEmpty())
                if (data.get(0) instanceof assignment_class) {
                    title = (TextView) itemView.findViewById(R.id.tvListMessage);
                    image = (TextView) itemView.findViewById(R.id.tvListId);
                }
        }
    }
}
