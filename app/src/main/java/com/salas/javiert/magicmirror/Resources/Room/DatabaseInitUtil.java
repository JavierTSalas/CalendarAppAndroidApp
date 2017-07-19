/*
 * Copyright (c) 2017. Javier Salas
 * This class initializes the database if there is not one
 */

package com.salas.javiert.magicmirror.Resources.Room;

import android.content.Context;

import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Room.connection.Entities.connectionDataBaseItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by javi6 on 7/18/2017.
 */

class DatabaseInitUtil {
    private static List<String> ConnectionData;
    private static List<String> ConnectionDataDefault;

    //Fill the string arrays from the resources, we need context to use getResources()
    private static void setUpStringArrays(Context context) {
        ConnectionData = Arrays.asList(context.getResources().getStringArray(R.array.connection_data));
        ConnectionDataDefault = Arrays.asList(context.getResources().getStringArray(R.array.connection_data_defaults));
    }

    public static void initializeDb(ConnectionDatabase db, Context context) {
        setUpStringArrays(context);
        List<connectionDataBaseItem> dataBaseItems = generateDefaultData();
        saveToDB(db, dataBaseItems);
    }

    private static void saveToDB(ConnectionDatabase db, List<connectionDataBaseItem> dataBaseItems) {
        db.beginTransaction();
        try {
            db.connectionDao().insertAll(dataBaseItems);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private static List<connectionDataBaseItem> generateDefaultData() {
        List<connectionDataBaseItem> mList = new ArrayList<>();
        for (int i = 0; i < ConnectionData.size(); i++)
            mList.add(
                    new connectionDataBaseItem(
                            i,
                            ConnectionData.get(i),
                            ConnectionDataDefault.get(i),
                            false,
                            false
                    ));
        return mList;
    }
}
