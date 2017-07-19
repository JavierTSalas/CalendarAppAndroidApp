/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Views.LiveDataTest;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Room.connection.Entities.connectionDataBaseItem;

import java.util.List;

/**
 * Created by javi6 on 7/18/2017.
 */

public class LiveDataAdapter extends RecyclerView.Adapter<LiveDataAdapter.RecyclerViewHolder> {

    private List<connectionDataBaseItem> connectionDataBaseItems;

    public LiveDataAdapter(List<connectionDataBaseItem> myList) {
        this.connectionDataBaseItems = myList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row_settings_class, parent, false));
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        connectionDataBaseItem item = connectionDataBaseItems.get(position);
        holder.itemTextView.setText(item.getName());
        holder.nameTextView.setText(item.getSubtitle());
    }

    @Override
    public int getItemCount() {
        return connectionDataBaseItems.size();
    }

    public void setItems(List<connectionDataBaseItem> connectionDataBaseItems) {
        this.connectionDataBaseItems = connectionDataBaseItems;
        notifyItemRangeChanged(0, connectionDataBaseItems.size());
        Log.d("LiveDataAdapter", "Changing data");
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTextView;
        private EditText nameTextView;

        RecyclerViewHolder(View view) {
            super(view);
            itemTextView = (TextView) view.findViewById(R.id.tvSettingsTitle);
            nameTextView = (EditText) view.findViewById(R.id.etSettingsSubtext);
        }
    }
}
