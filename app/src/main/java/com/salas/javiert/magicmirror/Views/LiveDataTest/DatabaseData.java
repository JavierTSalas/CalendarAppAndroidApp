/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Views.LiveDataTest;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by javi6 on 7/19/2017.
 */

public class DatabaseData extends BaseObservable {
    private String ColumnID, TextView1, TextView2;

    public DatabaseData(String columnID, String textView1, String textView2) {
        ColumnID = columnID;
        TextView1 = textView1;
        TextView2 = textView2;
    }

    @Bindable
    public String getTextView1() {
        return TextView1;
    }

    public void setTextView1(String s) {
        this.ColumnID = s;
        notifyPropertyChanged(BR.TextView1Name);
    }

    @Bindable
    public String getTextView2() {
        return TextView2;
    }

    public void setTextView2(String s) {
        this.ColumnID = s;
        notifyPropertyChanged(BR.TextView2Name);
    }

    @Bindable
    public int getColumnId() {
        return Integer.getInteger(ColumnID);

    }

    public void setColumnID(String s) {
        this.ColumnID = s;
        notifyPropertyChanged(BR.ColumnID);
    }
}
