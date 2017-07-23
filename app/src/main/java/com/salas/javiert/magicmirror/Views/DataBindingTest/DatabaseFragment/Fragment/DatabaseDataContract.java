/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Views.DataBindingTest.DatabaseFragment.Fragment;

/**
 * Created by javi6 on 7/19/2017.
 */

public interface DatabaseDataContract {
    interface Presenter {
        void onShowData(fragmentDataBindingObject databaseData);
    }

    interface View {
        void showData(fragmentDataBindingObject databaseData);
    }
}
