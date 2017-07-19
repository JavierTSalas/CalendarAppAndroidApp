/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Views.LiveDataTest;

import android.content.Context;

/**
 * Created by javi6 on 7/19/2017.
 */

public class DatabasePresenter implements DatabaseDataContract.Presenter {

    private DatabaseDataContract.View view;
    private Context context;

    public DatabasePresenter(DatabaseDataContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void onShowData(DatabaseData databaseData) {
        view.showData(databaseData);
    }
}
