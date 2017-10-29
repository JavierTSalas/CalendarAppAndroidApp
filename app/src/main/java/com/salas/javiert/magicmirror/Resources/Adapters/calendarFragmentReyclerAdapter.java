/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salas.javiert.magicmirror.Objects.bindableObjects.bindableAssignment;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Room.savedAssignments.Entities.savedAssignment;
import com.salas.javiert.magicmirror.Resources.Room.savedAssignments.savedAssignmentDataBaseCreator;
import com.salas.javiert.magicmirror.Resources.Util.FileDataUtil;
import com.salas.javiert.magicmirror.Views.DataBindingTest.DatabaseFragment.Fragment.RecyclerViewClasses.childHandler;
import com.salas.javiert.magicmirror.databinding.RecyclerviewRowCalendarDataboundBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by javi6 on 8/12/2017.
 */

public class calendarFragmentReyclerAdapter extends RecyclerView.Adapter<calendarFragmentReyclerAdapter.bindingEventViewHolder> {

    private final String TAG = "calendarRecycler";
    OnClickRecyclerChild mCallback;
    private List<bindableAssignment> dataBaseItemList;

    public calendarFragmentReyclerAdapter(List<bindableAssignment> myList) {
        this.dataBaseItemList = myList;
    }

    public calendarFragmentReyclerAdapter(List<bindableAssignment> dataBaseItemList, OnClickRecyclerChild mCallback) {
        this.mCallback = mCallback;
        this.dataBaseItemList = dataBaseItemList;
    }

    public calendarFragmentReyclerAdapter(Integer[] dataBaseItemList, OnClickRecyclerChild mCallback, Context context) {
        this.mCallback = mCallback;
        fetchAndSetDataBaseItemsEntries(dataBaseItemList, context);
    }

    private void fetchAndSetDataBaseItemsEntries(final Integer[] dataBaseItemList, final Context context) {
        final savedAssignmentDataBaseCreator savedAssignmentDBCreator = savedAssignmentDataBaseCreator.getInstance(context);
        new AsyncTask<Void, Void, Void>() {

            List<bindableAssignment> bindableAssignments = new ArrayList<>();

            @Override
            protected void onPreExecute() {
                if (savedAssignmentDBCreator.isDatabaseCreated().getValue() == Boolean.FALSE)
                    savedAssignmentDBCreator.createDb(context);
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // Get the list of assignment that the user has created
                for (Integer i : dataBaseItemList) {
                    bindableAssignments.add(new bindableAssignment(savedAssignmentDBCreator.getDatabase().savedAssignmentDao().getIndex(i)));
                    Log.d(TAG, i.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // Add all of events that we fetched
                setItems(bindableAssignments);
            }
        }.execute();
    }

    @Override
    public calendarFragmentReyclerAdapter.bindingEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View returnView = layoutInflater.inflate(R.layout.recyclerview_row_calendar_databound, parent, false);
        return new bindingEventViewHolder(returnView);
    }

    @Override
    public void onBindViewHolder(final calendarFragmentReyclerAdapter.bindingEventViewHolder holder, int position) {
        Log.d("calendarAdapater", dataBaseItemList.get(position).toString());
        holder.bind(dataBaseItemList.get(position));
        holder.setHandler(new childHandler());
        // All of the magic happens here

        holder.mBinding.tvAssignmentHighlight.setBackgroundColor(getColorFromClassId(holder.mBinding.getData().getId()));
        // Set the text by the imageview to the time that the assignment is due
        holder.mBinding.tvAssignmentDueTime.setText(FileDataUtil.getModifiedTime(Locale.getDefault(), holder.mBinding.getData().getDueTime()));
        holder.mBinding.tvAssignmentMM.setText(FileDataUtil.getModifiedMonthName(Locale.getDefault(), holder.mBinding.getData().getDueTime()));
        holder.mBinding.tvAssignmentDD.setText(FileDataUtil.getModifiedCalendarDay(Locale.getDefault(), holder.mBinding.getData().getDueTime()));

        holder.mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.editAssignment(new savedAssignment(holder.mBinding.getData()));
            }
        });

    }

    private int getColorFromClassId(int id) {
        // TODO get this form a database and read it on onAttach()
        List<Integer> myColorList = new ArrayList<>();
        myColorList.add(Color.RED);
        myColorList.add(Color.BLUE);
        myColorList.add(Color.YELLOW);
        myColorList.add(Color.DKGRAY);
        myColorList.add(Color.BLUE);

        return myColorList.get(id % 4);
    }


    public void setItems(final Integer[] integers, Context context) {
        fetchAndSetDataBaseItemsEntries(integers, context);
    }


    public void setItems(final List<bindableAssignment> argumentList) {
        Log.d("dataBindingAdapter", "Received a list with " + argumentList.size() + " elements");
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
                    bindableAssignment oldItem = dataBaseItemList.get(oldItemPosition);
                    bindableAssignment newItem = argumentList.get(newItemPosition);
                    return oldItem.getId() == newItem.getId()
                            && oldItem.getName().equals(newItem.getName())
                            && (oldItem.getDesc() != null && newItem.getDesc() != null) && oldItem.getDesc().equals(newItem.getDesc())
                            && oldItem.getClassId() == newItem.getClassId()
                            && oldItem.getDueTime() == newItem.getDueTime();
                }
            });
            dataBaseItemList = argumentList;
            result.dispatchUpdatesTo(this);
        }
        Log.d("dataBindingAdapter", "Changing data with " + dataBaseItemList.size() + " elements");
    }


    @Override
    public int getItemCount() {
        return (dataBaseItemList != null) ? dataBaseItemList.size() : 0;
    }

    public interface OnClickRecyclerChild {
        void editAssignment(savedAssignment savedAssignment);
    }


    // Define our custom ViewHolder for the recycler view
    public class bindingEventViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = "bindableAssignView";

        RecyclerviewRowCalendarDataboundBinding mBinding;

        public bindingEventViewHolder(View itemView) {
            super(itemView);
            this.mBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(bindableAssignment item) {
            mBinding.setData(item);
            mBinding.executePendingBindings();
        }

        public void setHandler(childHandler handler) {
            mBinding.setHandler(handler);
            mBinding.executePendingBindings();
        }

    }
}
