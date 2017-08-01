/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.salas.javiert.magicmirror.Resources.Room.serverAddress.Entities.serverAddressItem;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabase;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabaseCreator;

import java.util.List;

/**
 * Created by javi6 on 7/3/2017.
 */

public class myConnectionSingleton {

    private static myConnectionSingleton instance = null;
    private String hostPort;
    private String directory;
    private String TAG = "connectionSingleton";

    // Literally does nothing
    private myConnectionSingleton() {

    }

    //Every time you need an instance, call this
    public static myConnectionSingleton getInstance() {
        if (instance == null)
            instance = new myConnectionSingleton();

        return instance;
    }

    public void saveToRoom(Context context, List<serverAddressItem> items) {
        serverAddressDatabaseCreator creator = serverAddressDatabaseCreator.getInstance(context);
        final serverAddressDatabase db = creator.getDatabase();

        if (items != null && items.size() > 0) {
            new AsyncTask<List<serverAddressItem>, Void, Void>() {

                serverAddressDatabase localdb;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    localdb = db;
                }

                @Override
                protected Void doInBackground(List<serverAddressItem>... params) {
                    localdb.serverAddressDao().insertAll(params[0]);
                    return null;
                }


            }.execute(items);
        }
    }

    // Load the components needed to construct the URL
    public void refreshURLFields(final Context context) {
        final serverAddressDatabaseCreator serverAddressDatabaseCreator = com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabaseCreator.getInstance(context);

        new AsyncTask<Object, Object, List<serverAddressItem>>() {

            List<serverAddressItem> fetchedList;

            @Override
            protected void onPreExecute() {
                if (!serverAddressDatabaseCreator.isDatabaseCreated().getValue())
                    serverAddressDatabaseCreator.createDb(context);
                super.onPreExecute();
            }

            @Override
            protected List<serverAddressItem> doInBackground(Object... params) {
                serverAddressDatabase db = serverAddressDatabaseCreator.getDatabase();
                fetchedList = db.serverAddressDao().getConnections(0, 1);

                return fetchedList;
            }

            @Override
            protected void onPostExecute(List<serverAddressItem> aVoid) {
                super.onPostExecute(aVoid);
                myConnectionSingleton.getInstance().setHostPort(aVoid.get(0).getName(), context);
                myConnectionSingleton.getInstance().setDirectory(aVoid.get(1).getName(), context);
            }
        }.execute();
    }

    // Save the components needed to construct the URL
    public void saveURLToPreference(final Context context) {
        final serverAddressDatabaseCreator serverAddressDatabaseCreator = com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabaseCreator.getInstance(context);

        new AsyncTask<Object, Object, List<serverAddressItem>>() {

            List<serverAddressItem> fetchedList;

            @Override
            protected void onPreExecute() {
                if (!serverAddressDatabaseCreator.isDatabaseCreated().getValue())
                    serverAddressDatabaseCreator.createDb(context);
                super.onPreExecute();
            }

            @Override
            protected List<serverAddressItem> doInBackground(Object... params) {
                serverAddressDatabase db = serverAddressDatabaseCreator.getDatabase();
                fetchedList = db.serverAddressDao().getConnections(0, 1);
                fetchedList.get(0).setName((getHostPort() == null) ? "" : getHostPort());
                fetchedList.get(1).setName((getDirectory() == null) ? "" : getDirectory());
                return fetchedList;
            }

            @Override
            protected void onPostExecute(List<serverAddressItem> items) {
                super.onPostExecute(items);
                serverAddressDatabase db = serverAddressDatabaseCreator.getDatabase();
                db.serverAddressDao().updateItems(items.get(0), items.get(1));
            }
        }.execute();


    }

    private String getHostPort() {
        return hostPort;
    }

    private String getDirectory() {
        return directory;
    }

    public void setHostPort(final String hostPort, final Context context) {
        this.hostPort = hostPort;
        final serverAddressDatabaseCreator serverAddressDatabaseCreator = com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabaseCreator.getInstance(context);
        new AsyncTask<Object, Object, serverAddressItem>() {

            serverAddressItem fetchedItem;

            @Override
            protected void onPreExecute() {
                if (!serverAddressDatabaseCreator.isDatabaseCreated().getValue())
                    serverAddressDatabaseCreator.createDb(context);
                super.onPreExecute();
            }

            @Override
            protected serverAddressItem doInBackground(Object... params) {
                serverAddressDatabase db = serverAddressDatabaseCreator.getDatabase();
                fetchedItem = db.serverAddressDao().getIndex(0);
                fetchedItem.setName((hostPort == null) ? "" : hostPort);
                return fetchedItem;
            }

            @Override
            protected void onPostExecute(serverAddressItem item) {
                super.onPostExecute(item);
                serverAddressDatabase db = serverAddressDatabaseCreator.getDatabase();
                db.serverAddressDao().updateItems(item);
            }
        }.execute();

        saveURLStatus(context, false);

    }

    public void setDirectory(final String directory, final Context context) {
        this.directory = directory;

        final serverAddressDatabaseCreator serverAddressDatabaseCreator = com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabaseCreator.getInstance(context);
        new AsyncTask<Object, Object, serverAddressItem>() {

            serverAddressItem fetchedItem;

            @Override
            protected void onPreExecute() {
                if (!serverAddressDatabaseCreator.isDatabaseCreated().getValue())
                    serverAddressDatabaseCreator.createDb(context);
                super.onPreExecute();
            }

            @Override
            protected serverAddressItem doInBackground(Object... params) {
                serverAddressDatabase db = serverAddressDatabaseCreator.getDatabase();
                fetchedItem = db.serverAddressDao().getIndex(1);
                fetchedItem.setName((directory == null) ? "" : directory);
                return fetchedItem;
            }

            @Override
            protected void onPostExecute(serverAddressItem item) {
                super.onPostExecute(item);
                serverAddressDatabase db = serverAddressDatabaseCreator.getDatabase();
                db.serverAddressDao().updateItems(item);
            }
        }.execute();

        saveURLStatus(context, false);
    }

    public String getURL() {
        return "http://" + getHostPort() +
                ((getDirectory() == null && getDirectory().length() > 0) ? "" : "/" + getDirectory() + "/");
    }

    // Saves the serverAddressFields to Boolean
    public void saveURLStatus(final Context context, final Boolean connectedSuccessfully) {

        final serverAddressDatabaseCreator serverAddressDatabaseCreator = com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabaseCreator.getInstance(context);
        new AsyncTask<Object, Object, List<serverAddressItem>>() {

            List<serverAddressItem> fetchedItem;

            @Override
            protected void onPreExecute() {
                if (!serverAddressDatabaseCreator.isDatabaseCreated().getValue())
                    serverAddressDatabaseCreator.createDb(context);
                super.onPreExecute();
            }

            @Override
            protected List<serverAddressItem> doInBackground(Object... params) {
                serverAddressDatabase db = serverAddressDatabaseCreator.getDatabase();
                fetchedItem = db.serverAddressDao().getConnections(0, 1);
                fetchedItem.get(0).setConnectionSuccessful(connectedSuccessfully);
                fetchedItem.get(1).setConnectionSuccessful(connectedSuccessfully);
                return fetchedItem;
            }

            @Override
            protected void onPostExecute(List<serverAddressItem> items) {
                super.onPostExecute(items);
                serverAddressDatabase db = serverAddressDatabaseCreator.getDatabase();
                db.serverAddressDao().updateItems(items.get(0), items.get(1));
            }
        }.execute();

        Log.d(TAG, connectedSuccessfully.toString());
    }

    /*
    //Fill the string arrays from the resources, we need context to use getResources()
    private void setUpStringArrays(Context context) {
        ConnectionData = Arrays.asList(context.getResources().getStringArray(R.array.connection_data));
        ConnectionDataDefault = Arrays.asList(context.getResources().getStringArray(R.array.connection_data_defaults));
    }
    public synchronized List<connectionSettings> loadFromPreferences(Context context, Pair<Integer, Integer> myPair) {
        // Setup the ararys
        setUpStringArrays(context);
        // Get the items that we need Pair<n,m> index n - (m - 1)  of string array from resources
        return readConnectionPreferences(context, myPair);

    }
    public synchronized void saveToPrefences(Context context, List<connectionSettings> dataToBeSaved) {
        if (dataToBeSaved != null && dataToBeSaved.size() > 0) {
            // Separate into list that we can store
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
    */


}
