/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.salas.javiert.magicmirror.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by javi6 on 7/3/2017.
 */

public class myConnectionSingleton {

    private static List<connectionSettings> myList = new ArrayList();
    private static myConnectionSingleton instance = null;
    private List<String> ConnectionData, ConnectionDataDefault;

    // Literally does nothing
    private myConnectionSingleton() {

    }

    //Everytime you need an instance, call this
    public static myConnectionSingleton getInstance() {
        if (instance == null)
            instance = new myConnectionSingleton();

        return instance;
    }

    public List<connectionSettings> getmyList() {
        return myList;
    }

    public void loadFromPreferences(Context context) {
        //Setup the ararys
        setUpStringArrays(context);
        readPreferences(context);
    }

    public void saveToPrefences(Context context, List<String> dataToBeSaved) {
        //Setup the ararys
        setUpStringArrays(context);
        savePreferences(context, dataToBeSaved);
    }

    private void readPreferences(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        myList = new ArrayList<>();
        for (int i = 0; i < ConnectionData.size(); i++) {
            String stringToStore = appSharedPrefs.getString(ConnectionData.get(i), ConnectionDataDefault.get(i));
            myList.add(new connectionSettings(ConnectionData.get(i), stringToStore));
        }
    }

    private void savePreferences(Context context, List<String> dataToBeSaved) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        for (int i = 0; i < ConnectionData.size(); i++) {
            prefsEditor.putString(ConnectionData.get(i), dataToBeSaved.get(i));
            prefsEditor.commit();
        }

    }

    //Set up so we can use it, we need context to use getResources()
    private void setUpStringArrays(Context context) {
        ConnectionData = Arrays.asList(context.getResources().getStringArray(R.array.connection_data));
        ConnectionDataDefault = Arrays.asList(context.getResources().getStringArray(R.array.connection_data_defaults));
    }


}
