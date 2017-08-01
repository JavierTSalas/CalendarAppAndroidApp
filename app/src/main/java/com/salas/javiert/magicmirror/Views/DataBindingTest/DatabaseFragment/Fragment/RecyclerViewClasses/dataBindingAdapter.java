/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Views.DataBindingTest.DatabaseFragment.Fragment.RecyclerViewClasses;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton.myConnectionSingleton;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.Entities.serverAddressItem;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by javi6 on 7/18/2017.
 */

public class dataBindingAdapter extends RecyclerView.Adapter<bindingViewHolder> {

    private List<serverAddressItem> dataBaseItemList;
    private ArrayList<childObserver> modifiedFields = new ArrayList<>();


    public dataBindingAdapter(List<serverAddressItem> myList) {
        this.dataBaseItemList = myList;
    }

    @Override
    public bindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View returnView = layoutInflater.inflate(R.layout.recyclerview_row_databind, parent, false);
        return new bindingViewHolder(returnView);
    }

    @Override
    public void onBindViewHolder(final bindingViewHolder holder, int position) {
        holder.bind(new childObserver((dataBaseItemList.get(position))));
        holder.setHandler(new childHandler());

        // For locking and unlocking the text
        holder.getmBinding().tvSettingsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.toggleLockEditText();
                modifiedConnection(holder.getmBinding().getData());
            }
        });


        holder.getmBinding().etSettingsSubtext.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // When the done button is pressed
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    // Save if we need to
                    if (holder.isServerAddressField())
                        if (holder.getmBinding().getData().getName().equals("Host:Port"))
                            myConnectionSingleton.getInstance().setHostPort(holder.getmBinding().getData().getName(), holder.getmBinding().tvSettingsTitle.getContext());
                    if (holder.getmBinding().getData().getName().equals("Directory"))
                        myConnectionSingleton.getInstance().setDirectory(holder.getmBinding().getData().getName(), holder.getmBinding().tvSettingsTitle.getContext());

                    myConnectionSingleton.getInstance().saveURLToPreference(holder.getmBinding().tvSettingsTitle.getContext());

                    // Try the connection
                    holder.tryConnection(holder.getmBinding().tvSettingsTitle.getContext());

                    Log.d(TAG, "Attempting connection");


                    modifiedConnection(holder.getmBinding().getData());


                    return true;
                }
                return false;

            }


        });


        holder.getmBinding().etSettingsSubtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If we are dealing with a Host:Port or a Directory field then we should save it
                if (holder.isServerAddressField()) {
                    if (holder.getmBinding().getData().getName().equals("Host:Port"))
                        myConnectionSingleton.getInstance().setHostPort(holder.getmBinding().getData().getSubtitle(), holder.getmBinding().tvSettingsTitle.getContext());
                    if (holder.getmBinding().getData().getName().equals("Directory"))
                        myConnectionSingleton.getInstance().setDirectory(holder.getmBinding().getData().getSubtitle(), holder.getmBinding().tvSettingsTitle.getContext());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


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
                            && oldItem.getName().equals(newItem.getName())
                            && oldItem.getSubtitle().equals(newItem.getSubtitle())
                            && oldItem.getConnectionSuccessful() == newItem.getConnectionSuccessful()
                            && oldItem.getLockStatus() == newItem.getLockStatus();
                }
            });
            dataBaseItemList = argumentList;
            result.dispatchUpdatesTo(this);
        }
        Log.d("dataBindingAdapter", "Changing data");
    }

    private void modifiedConnection(childObserver child) {
        // Save our changed fields
        if (!isNotInUpdatedFeilds(child)) {
            modifiedFields.add(child);
            Log.d("modifiedFields", "adding");
        } else {
            updateModifiedFeilds(child);
            Log.d("modifiedFields", "updating");
        }
    }

    private void updateModifiedFeilds(childObserver child) {
        for (int i = 0; i < modifiedFields.size(); i++)
            if (modifiedFields.get(i).getName().equals(child.getName())) {
                modifiedFields.set(i, child);
                return;
            }
    }

    private boolean isNotInUpdatedFeilds(childObserver child) {
        for (childObserver children : modifiedFields)
            if (children.getName().equals(child.getName()))
                return true;
        return false;
    }

    public List<serverAddressItem> getModifiedItems() {
        return dataBaseItemList;
    }

    public boolean haveChangesBeenMade() {
        return (modifiedFields != null && modifiedFields.size() > 0);
    }
}
