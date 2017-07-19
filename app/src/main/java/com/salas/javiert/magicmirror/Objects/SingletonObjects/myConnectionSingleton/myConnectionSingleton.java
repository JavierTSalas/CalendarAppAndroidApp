/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.TinyDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by javi6 on 7/3/2017.
 */

public class myConnectionSingleton {

    private static List<connectionSettings> myList = new ArrayList();
    private static myConnectionSingleton instance = null;
    private String hostPort;
    private List<String> ConnectionData, ConnectionDataDefault;
    private String directory;
    private String TAG = "connectionSingleton";

    // Literally does nothing
    private myConnectionSingleton() {

    }

    //Everytime you need an instance, call this
    public static myConnectionSingleton getInstance() {
        if (instance == null)
            instance = new myConnectionSingleton();

        return instance;
    }

    public synchronized List<connectionSettings> loadFromPreferences(Context context, Pair<Integer, Integer> myPair) {
        // Setup the ararys
        setUpStringArrays(context);
        // Get the items that we need Pair<n,m> index n - (m - 1)  of string array from resources
        return readConnectionPreferences(context, myPair);

    }

    public synchronized void saveToPrefences(Context context, List<connectionSettings> dataToBeSaved) {
        if (dataToBeSaved != null && dataToBeSaved.size() > 0) {
            // Seperate into list that we can store
            // We don't really want to use GSON for this as we would be storing the views which is useless to us next time we launch the app
            List<String> connectionNameList = getconnectionNameList(dataToBeSaved);
            List<String> connectionSubstringList = getconnectionSubstringList(dataToBeSaved);
            List<Boolean> connectionStatusList = getconnectionStatusList(dataToBeSaved);
            List<Boolean> connectionSubstringLockStauts = getconnectionSubstringLockStauts(dataToBeSaved);

            Log.d(TAG, "Saving " + dataToBeSaved.size() + " items");

            //Setup the ararys
            setUpStringArrays(context);
            saveConnectionPreferences(context, connectionNameList, connectionSubstringList, connectionStatusList, connectionSubstringLockStauts);
        }
    }

    // Split the list of connectionSettings to the list that we want to save
    private List<String> getconnectionNameList(List<connectionSettings> dataToBeSaved) {
        List<String> returnList = new ArrayList<>();
        for (connectionSettings data : dataToBeSaved)
            returnList.add(data.getTitle());
        return returnList;
    }

    private List<Boolean> getconnectionSubstringLockStauts(List<connectionSettings> dataToBeSaved) {
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
    private synchronized List<connectionSettings> readConnectionPreferences(Context context, Pair<Integer, Integer> myPair) {
        TinyDB appSharedPrefs = new TinyDB(context);
        myList = new ArrayList<>();

        Log.d(TAG, "First :" + myPair.first + " Second :" + myPair.second);


        // If both of our pair values are 0 then return the whole list
        if ((myPair.first == 0 && myPair.second == 0)) {
            // Fill myList
            for (int i = 0; i < ConnectionData.size(); i++) {
                String subText = appSharedPrefs.getString(ConnectionData.get(i));
                // If value is not found and we are returned a default value
                if (TinyDB.isDefaultValue(subText)) {
                    subText = ConnectionDataDefault.get(i);
                }
                // This defaults to false so we do not need to check for a default value
                Boolean connectionStauts = appSharedPrefs.getBoolean(ConnectionData.get(i) + "_ConnectionStatus");
                Boolean lockStatus = appSharedPrefs.getBoolean(ConnectionData.get(i) + "_LockStatus");
                connectionSettings connectionSettings = new connectionSettings(ConnectionData.get(i), subText, connectionStauts, lockStatus);
                myList.add(connectionSettings);
                Log.d(TAG, connectionSettings.toString());
            }
        } else {
            // For the Pair <n,m> in list indexOfListOnScreen
            // n is the starting location of the index of the list from SharedPreferences();
            // m is the count of item that will be returned
            int Start = myPair.first;
            int Count = myPair.second;
            for (int i = Start; i < Start + Count; i++) {
                String subText = appSharedPrefs.getString(ConnectionData.get(i));
                // If value is not found and we are returned a default value
                if (TinyDB.isDefaultValue(subText)) {
                    subText = ConnectionDataDefault.get(i);
                }
                // This defaults to false so we do not need to check for a default value
                Boolean connectionStauts = appSharedPrefs.getBoolean(ConnectionData.get(i) + "_ConnectionStatus");
                Boolean lockStatus = appSharedPrefs.getBoolean(ConnectionData.get(i) + "_LockStatus");
                connectionSettings connectionSettings = new connectionSettings(ConnectionData.get(i), subText, connectionStauts, lockStatus);
                Log.d(TAG, "Adding to mylist index "
                        + i + " "
                        + ConnectionData.get(i) + " - "
                        + connectionSettings.toString()
                        + " from SharedPreferences");
                myList.add(connectionSettings);
            }

        }
        Log.d(TAG, "Read " + myList.size() + " from SharedPreferences");
        for (int i = 0; i < myList.size(); i++) {
            Log.d(TAG, myList.get(i).toString());
        }
        return myList;
    }

    // Save connectionPreferences using SharedPreferences
    private synchronized void saveConnectionPreferences(Context context, List<String> connectionNameList, List<String> connectionSubstringList, List<Boolean> connectionStatusList, List<Boolean> connectionSubstringLockStauts) {
        TinyDB prefsEditor = new TinyDB(context);

        // Save the data in the format
        // <string name="ConnectionData.get(i)">connectionSubstringList.get(i)</string>
        int start = firstCollision(ConnectionData, connectionNameList);
        int end = lastCollision(start, ConnectionData, connectionNameList);

        Log.d(TAG, "Saved " + String.valueOf(end - start + 1) + " from SharedPreferences");

        for (int i = start; i < end + 1; i++) {
            // If we are at the right index for the element then we set anchor since our data is saved + read linearly
            prefsEditor.putString(ConnectionData.get(i), connectionSubstringList.get(i - start));
            prefsEditor.putBoolean(ConnectionData.get(i) + "_ConnectionStatus", connectionStatusList.get(i - start));
            prefsEditor.putBoolean(ConnectionData.get(i) + "_LockStatus", connectionSubstringLockStauts.get(i - start));
            Log.d(TAG, "Saving index " + i + " "
                    + ConnectionData.get(i) + " - "
                    + connectionNameList.get(i - start) + " "
                    + connectionSubstringList.get(i - start));
        }


    }


    // Return the index of connectionData where the string value of the last index of connectionNameList is found
    // Returns start if it is not is found
    private int lastCollision(int start, List<String> connectionData, List<String> connectionNameList) {
        for (int j = start; j < connectionData.size(); j++)
            if (connectionData.get(j).equals(connectionNameList.get(connectionNameList.size() - 1)))
                return j;
        return start;
    }

    // Returns the first index where a String in connectionData and connectionNameList match
    // Returns 0 if no match
    private int firstCollision(List<String> connectionData, List<String> connectionNameList) {
        for (String base : connectionData)
            for (String compare : connectionNameList)
                if (base.equals(compare))
                    return connectionData.indexOf(base);

        return 0;
    }

    // Load the components needed to construct the URL
    public void updateUrlFromPreferences(Context context) {
        TinyDB appSharedPrefs = new TinyDB(context);

        setUpStringArrays(context);

        String hostPort = appSharedPrefs.getString(ConnectionData.get(0));
        if (TinyDB.isDefaultValue(hostPort)) {
            this.setHostPort(ConnectionDataDefault.get(0), context);
        } else {
            this.setHostPort(hostPort, context);
        }
        String directory = appSharedPrefs.getString(ConnectionData.get(1));
        if (TinyDB.isDefaultValue(directory)) {
            this.setDirectory(ConnectionDataDefault.get(1), context);
        } else {
            this.setDirectory(directory, context);
        }
    }

    // Save the components needed to construct the URL
    public void saveURLToPreference(Context context) {
        TinyDB prefsEditor = new TinyDB(context);
        prefsEditor.putString("Host:Port", (getHostPort() == null) ? "" : getHostPort());
        prefsEditor.putString("Directory", (getDirectory() == null) ? "" : getDirectory());

    }

    private String getHostPort() {
        return hostPort;
    }

    private String getDirectory() {
        return directory;
    }

    public void setHostPort(String host, Context context) {
        this.hostPort = host;
        TinyDB prefsEditor = new TinyDB(context);

        prefsEditor.putString(ConnectionData.get(0), (host == null) ? "" : host);
        saveURLStatus(context, false);
    }

    public void setDirectory(String directory, Context context) {
        this.directory = directory;
        TinyDB prefsEditor = new TinyDB(context);

        prefsEditor.putString(ConnectionData.get(1), (directory == null) ? "" : directory);
        saveURLStatus(context, false);
    }

    public String getURL() {
        return "http://" + getHostPort() +
                ((getDirectory() == null && getDirectory().length() > 0) ? "" : "/" + getDirectory() + "/");
    }

    // Saves the serverAdressFields to Boolean
    public synchronized void saveURLStatus(Context context, Boolean connectedSuccessfully) {
        TinyDB prefsEditor = new TinyDB(context);


        prefsEditor.putBoolean("Host:Port" + "_ConnectionStatus", connectedSuccessfully);
        prefsEditor.putBoolean("Host:Port" + "_LockStatus", connectedSuccessfully);
        prefsEditor.putBoolean("Directory" + "_ConnectionStatus", connectedSuccessfully);
        prefsEditor.putBoolean("Directory" + "_LockStatus", connectedSuccessfully);
        Log.d("saveUrlStatus", connectedSuccessfully.toString());
    }

    //Fill the string arrays from the resources, we need context to use getResources()
    private void setUpStringArrays(Context context) {
        ConnectionData = Arrays.asList(context.getResources().getStringArray(R.array.connection_data));
        ConnectionDataDefault = Arrays.asList(context.getResources().getStringArray(R.array.connection_data_defaults));
    }


}
