/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Objects.SingletonObjects_REMOVE_ME.myConnectionSingleton;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;

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
    private String host;
    private Integer port;
    private List<String> ConnectionData, ConnectionDataDefault;
    private String directory;

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

    public List<connectionSettings> loadFromPreferences(Context context, Pair<Integer, Integer> myPair) {
        // Setup the ararys
        setUpStringArrays(context);
        // Get the items that we need Pair<n,m> index n - (m - 1)  of string array from resources
        return readConnectionPreferences(context, myPair);

    }

    public void saveToPrefences(Context context, List<connectionSettings> dataToBeSaved) {
        // Seperate into list that we can store
        // We don't really want to use GSON for this as we would be storing the views which is useless to us next time we launch the app
        List<String> connectionSubstringList = getconnectionSubstringList(dataToBeSaved);
        List<Boolean> connectionStatusList = getconnectionStatusList(dataToBeSaved);
        List<Boolean> connectionSubstringLockStauts = getconnectionSubstringLockStauts(dataToBeSaved);


        //Setup the ararys
        setUpStringArrays(context);
        saveConnectionPreferences(context, connectionSubstringList, connectionStatusList, connectionSubstringLockStauts);
    }

    private List<Boolean> getconnectionSubstringLockStauts(List<connectionSettings> dataToBeSaved) {
        List<String> returnList = new ArrayList<>();
        List<Boolean> mReturnList = new ArrayList<>();
        for (connectionSettings data : dataToBeSaved)
            mReturnList.add(data.getisLocked());
        return mReturnList;
    }

    private List<Boolean> getconnectionStatusList(List<connectionSettings> dataToBeSaved) {
        List<Boolean> returnList = new ArrayList<>();
        for (connectionSettings data : dataToBeSaved)
            returnList.add(data.getConnectionSuccessful());
        return returnList;
    }

    private List<String> getconnectionSubstringList(List<connectionSettings> dataToBeSaved) {
        List<String> returnList = new ArrayList<>();
        for (connectionSettings data : dataToBeSaved)
            returnList.add(data.getSubtext());
        return returnList;
    }

    // Read connectionPreferences using SharedPreferences
    private List<connectionSettings> readConnectionPreferences(Context context, Pair<Integer, Integer> myPair) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        myList = new ArrayList<>();


        // If either of our pair are less than 0 then return the whole list
        if (myPair.first < 0 || myPair.second < 0) {
            // Fill myList
            for (int i = 0; i < ConnectionData.size(); i++) {
                String subText = appSharedPrefs.getString(ConnectionData.get(i), ConnectionDataDefault.get(i));
                Boolean connectionStauts = appSharedPrefs.getBoolean(ConnectionData.get(i) + "_ConnectionStatus", false);
                Boolean lockStatus = appSharedPrefs.getBoolean(ConnectionData.get(i) + "_LockStatus", false);
                connectionSettings connectionSettings = new connectionSettings(ConnectionData.get(i), subText, connectionStauts, lockStatus);
                myList.add(connectionSettings);
                Log.d("Reading", connectionSettings.toString());
            }
        } else {
            // For the Pair <n,m> in list indexOfListOnScreen
            // n is the starting location of the index of the list from SharedPreferences();
            int Start = myPair.first;
            int End = myPair.first;
            for (int i = Start; i < Start + End + 1; i++) {
                String subText = appSharedPrefs.getString(ConnectionData.get(i), ConnectionDataDefault.get(i));
                Boolean connectionStauts = appSharedPrefs.getBoolean(ConnectionData.get(i) + "_ConnectionStatus", false);
                Boolean lockStatus = appSharedPrefs.getBoolean(ConnectionData.get(i) + "_LockStatus", false);
                connectionSettings connectionSettings = new connectionSettings(ConnectionData.get(i), subText, connectionStauts, lockStatus);
                myList.add(connectionSettings);
            }

        }

        return myList;
    }


    // Save connectionPreferences using SharedPreferences
    private void saveConnectionPreferences(Context context, List<String> connectionSubstringList, List<Boolean> connectionStatusList, List<Boolean> connectionSubstringLockStauts) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        // Save the data in the format
        // <string name="ConnectionData.get(i)">connectionSubstringList.get(i)</string>
        for (int i = 0; i < ConnectionData.size(); i++) {
            prefsEditor.putString(ConnectionData.get(i), connectionSubstringList.get(i));
            prefsEditor.putBoolean(ConnectionData.get(i) + "_ConnectionStatus", connectionStatusList.get(i));
            prefsEditor.putBoolean(ConnectionData.get(i) + "_LockStatus", connectionSubstringLockStauts.get(i));
            prefsEditor.commit();
        }

    }


    // Load the components needed to construct the URL
    public void loadURLToPreference(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        setUpStringArrays(context);

        this.setHost(appSharedPrefs.getString(ConnectionData.get(0), ConnectionDataDefault.get(0)), context);
        this.setPort(Integer.getInteger(appSharedPrefs.getString(ConnectionData.get(1), ConnectionDataDefault.get(1))), context);
        this.setDirectory(appSharedPrefs.getString(ConnectionData.get(2), ConnectionDataDefault.get(2)), context);
    }

    // Save the components needed to construct the URL
    public void saveURLToPreference(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Host", (getHost() == null) ? "" : getHost().toString());
        prefsEditor.putString("Port", (getPort() == null) ? "" : getPort().toString());
        prefsEditor.putString("Directory", (getDirectory() == null) ? "" : getDirectory().toString());
        prefsEditor.commit();

    }

    private String getHost() {
        return host;
    }

    public void setHost(String host, Context context) {
        this.host = host;
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        prefsEditor.putString(ConnectionData.get(0), (host == null) ? "" : host);
        saveURLStatus(context, false);
        prefsEditor.commit();
    }

    private Integer getPort() {
        return port;
    }

    public void setPort(Integer port, Context context) {
        this.port = port;
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        prefsEditor.putInt(ConnectionData.get(1), (port == null) ? Integer.valueOf("") : port);
        saveURLStatus(context, false);
        prefsEditor.commit();
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory, Context context) {
        this.directory = directory;
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        prefsEditor.putString(ConnectionData.get(2), (directory == null) ? "" : directory);
        saveURLStatus(context, false);
        prefsEditor.commit();
    }

    public String getURL() {
        return "http://" + getHost() +
                ((getPort() == null) ? "" : ":" + getPort()) +
                ((getDirectory() == null) ? "" : "/" + getDirectory() + "/");
    }

    // Saves the serverAdressFields to Boolean
    public void saveURLStatus(Context context, Boolean connectedSuccessfully) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        prefsEditor.putBoolean(ConnectionData.get(0) + "_ConnectionStatus", connectedSuccessfully);
        prefsEditor.putBoolean(ConnectionData.get(0) + "_LockStatus", connectedSuccessfully);
        prefsEditor.putBoolean(ConnectionData.get(1) + "_ConnectionStatus", connectedSuccessfully);
        prefsEditor.putBoolean(ConnectionData.get(1) + "_LockStatus", connectedSuccessfully);
        prefsEditor.putBoolean(ConnectionData.get(2) + "_ConnectionStatus", connectedSuccessfully);
        prefsEditor.putBoolean(ConnectionData.get(2) + "_LockStatus", connectedSuccessfully);
        Log.d("SaveURLStatus", connectedSuccessfully.toString());
        prefsEditor.commit();
    }

    //Fill the string arrays from the resources, we need context to use getResources()
    private void setUpStringArrays(Context context) {
        ConnectionData = Arrays.asList(context.getResources().getStringArray(R.array.connection_data));
        ConnectionDataDefault = Arrays.asList(context.getResources().getStringArray(R.array.connection_data_defaults));
    }


}
