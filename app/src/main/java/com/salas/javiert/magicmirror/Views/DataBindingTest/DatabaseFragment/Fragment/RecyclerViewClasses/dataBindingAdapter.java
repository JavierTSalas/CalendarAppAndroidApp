/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Views.DataBindingTest.DatabaseFragment.Fragment.RecyclerViewClasses;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.Entities.serverAddressItem;

import java.util.List;

/**
 * Created by javi6 on 7/18/2017.
 */

public class dataBindingAdapter extends RecyclerView.Adapter<bindingViewHolder<serverAddressItem>> {

    private List<serverAddressItem> dataBaseItemList;

    public dataBindingAdapter(List<serverAddressItem> myList) {
        this.dataBaseItemList = myList;
    }

    @Override
    public bindingViewHolder<serverAddressItem> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View returnView = layoutInflater.inflate(R.layout.recyclerview_row_livedata, parent, false);
        return new bindingViewHolder(returnView);
    }

    @Override
    public void onBindViewHolder(bindingViewHolder<serverAddressItem> holder, int position) {
    }

    @Override
    public int getItemCount() {
        return dataBaseItemList.size();
    }

    public void setItems(final List<serverAddressItem> argumentList) {
        if (dataBaseItemList == null) {
            dataBaseItemList = argumentList;
            notifyItemRangeChanged(0, dataBaseItemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return dataBaseItemList.size();
                }

                @Override
                public int getNewListSize() {
                    return argumentList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return dataBaseItemList.get(oldItemPosition).getId() == argumentList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    serverAddressItem oldItem = dataBaseItemList.get(oldItemPosition);
                    serverAddressItem newItem = argumentList.get(newItemPosition);
                    return oldItem.getId() == newItem.getId()
                            && oldItem.getName() == newItem.getName()
                            && oldItem.getSubtitle() == newItem.getSubtitle()
                            && oldItem.getConnectionSuccessful() == newItem.getConnectionSuccessful()
                            && oldItem.getLockStatus() == newItem.getLockStatus();
                }
            });
            dataBaseItemList = argumentList;
            result.dispatchUpdatesTo(this);
        }
        Log.d("dataBindingAdapter", "Changing data");
    }

}
