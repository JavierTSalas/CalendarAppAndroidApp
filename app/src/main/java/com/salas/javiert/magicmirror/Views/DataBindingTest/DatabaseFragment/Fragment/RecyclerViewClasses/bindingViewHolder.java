/*
 * Copyright (c) 2017. Javier Salas
 * Generic dataBinding for ViewHolder
 * source : https://stackoverflow.com/documentation/android/169/recyclerview/18296/recyclerview-with-databinding#t=20170723011950244771
 */

package com.salas.javiert.magicmirror.Views.DataBindingTest.DatabaseFragment.Fragment.RecyclerViewClasses;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by javi6 on 7/22/2017.
 */

public class bindingViewHolder<T> extends RecyclerView.ViewHolder {

    private final T binding;

    public bindingViewHolder(View itemView) {
        super(itemView);
        this.binding = DataBindingUtil.bind(itemView);
    }


    public T getBinding() {
        return binding;
    }
}