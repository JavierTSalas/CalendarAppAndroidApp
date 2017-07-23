/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Views.DataBindingTest.DatabaseFragment.Fragment;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.salas.javiert.magicmirror.BR;

/**
 * Created by javi6 on 7/19/2017.
 */

public class fragmentDataBindingObject extends BaseObservable {
    private String ColumnID, TextView1, TextView2;
    private Boolean isLoading;

    public fragmentDataBindingObject(String columnID, String textView1, String textView2, Boolean loading) {
        ColumnID = columnID;
        TextView1 = textView1;
        TextView2 = textView2;
        isLoading = loading;

    }

    @Bindable
    public Boolean getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(Boolean isLoading) {
        this.isLoading = isLoading;
        notifyPropertyChanged(BR.isLoading);
    }

    @Bindable
    public String getTextView1() {
        return TextView1;
    }

    public void setTextView1(String s) {
        this.ColumnID = s;
        notifyPropertyChanged(BR.textView1);
    }

    @Bindable
    public String getTextView2() {
        return TextView2;
    }

    public void setTextView2(String s) {
        this.ColumnID = s;
        notifyPropertyChanged(BR.textView2);
    }

    @Bindable
    public int getColumnId() {
        return Integer.getInteger(ColumnID);

    }

    public void setColumnID(String s) {
        this.ColumnID = s;
        notifyPropertyChanged(BR.columnId);
    }
}
