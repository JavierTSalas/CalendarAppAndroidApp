/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Adapters;

import android.databinding.DataBindingUtil;
import android.os.CountDownTimer;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salas.javiert.magicmirror.Objects.bindableObjects.bindableAssignment;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Views.DataBindingTest.DatabaseFragment.Fragment.RecyclerViewClasses.childHandler;
import com.salas.javiert.magicmirror.databinding.RecyclerviewRowCalendarDataboundBinding;

import java.util.Date;
import java.util.List;

/**
 * Created by javi6 on 8/12/2017.
 */

public class calendarFragmentReyclerAdapter extends RecyclerView.Adapter<calendarFragmentReyclerAdapter.bindingEventViewHolder> {

    private List<bindableAssignment> dataBaseItemList;

    public calendarFragmentReyclerAdapter(List<bindableAssignment> myList) {
        this.dataBaseItemList = myList;
    }


    @Override
    public calendarFragmentReyclerAdapter.bindingEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View returnView = layoutInflater.inflate(R.layout.recyclerview_row_calendar_databound, parent, false);
        return new bindingEventViewHolder(returnView);
    }

    @Override
    public void onBindViewHolder(final calendarFragmentReyclerAdapter.bindingEventViewHolder holder, int position) {
        holder.bind(dataBaseItemList.get(position));
        holder.setHandler(new childHandler());
        // All of the magic happens here

        final long nowInMs = (new Date()).getTime();
        final long dueTimeInMs = (dataBaseItemList.get(position)).getDueTime();
        final long timeAssigned = (dataBaseItemList.get(position)).getAssignedTime();
        final long timeUserHadToCompleteAnAssignment = (dueTimeInMs - timeAssigned);
        final long timeUserHasLeft = (dueTimeInMs - nowInMs);
        final long progressMax = 100;
        holder.mBinding.pbAssignmneDaysLeft.setMax((int) progressMax);

        Log.d("datadump", nowInMs + " " + dueTimeInMs + " " + timeAssigned + " " + timeUserHadToCompleteAnAssignment + " " + timeUserHasLeft + " ");

        // This is probably scrapped but keeping it for now
        CountDownTimer countDownTimer = new CountDownTimer(timeUserHasLeft, 500) {
            // 500 means, onTick function will be called at every 500 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                int progress = (int) (progressMax - (leftTimeInMilliseconds * progressMax / timeUserHadToCompleteAnAssignment));
                Log.d("progress", progress + " " + leftTimeInMilliseconds + "/" + timeUserHadToCompleteAnAssignment);
                Log.d("progress", progress + " out of " + progressMax);
                holder.mBinding.pbAssignmneDaysLeft.setProgress(progress * 50);

                if (leftTimeInMilliseconds > 1000 * 60 * 60 * 24) { // If more than a day
                    int days = (int) Math.floor(leftTimeInMilliseconds / (1000 * 60 * 60 * 24));
                    holder.mBinding.tvAssignmentCount.setText(String.valueOf(days));
                    holder.mBinding.tvAssignmentIncrement.setText("Days");
                } else if (leftTimeInMilliseconds > 1000 * 60 * 60 * 2) { // If more than an two hours but less than a day
                    int hours = (int) Math.floor(leftTimeInMilliseconds / (1000 * 60 * 60));
                    holder.mBinding.tvAssignmentCount.setText(String.valueOf(hours));
                    holder.mBinding.tvAssignmentIncrement.setText("Hours");
                } else { // If less than two hours show the user
                    int minutes = (int) Math.floor(leftTimeInMilliseconds / (1000 * 60));
                    holder.mBinding.tvAssignmentCount.setText(String.valueOf(minutes));
                    holder.mBinding.tvAssignmentIncrement.setText("Minutes");
                }
                // format the textview to show the easily readable format
                //holder.mBinding.pbAssignmneDaysLeft.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.OVERLAY));
            }

            @Override
            public void onFinish() {
                holder.mBinding.tvAssignmentCount.setText("!");
                holder.mBinding.tvAssignmentIncrement.setText("DUE");
            }
        };



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
