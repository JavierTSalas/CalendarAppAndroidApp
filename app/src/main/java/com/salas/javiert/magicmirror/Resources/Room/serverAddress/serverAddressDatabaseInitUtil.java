/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.serverAddress;

import android.content.Context;

import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.Entities.serverAddressItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by javi6 on 7/18/2017.
 */

class serverAddressDatabaseInitUtil {
    private static List<String> ConnectionData;
    private static List<String> ConnectionDataDefault;

    //Fill the string arrays from the resources, we need context to use getResources()
    private static void setUpStringArrays(Context context) {
        ConnectionData = Arrays.asList(context.getResources().getStringArray(R.array.connection_data));
        ConnectionDataDefault = Arrays.asList(context.getResources().getStringArray(R.array.connection_data_defaults));
    }

    public static void initializeDb(serverAddressDatabase db, Context context) {
        setUpStringArrays(context);
        List<serverAddressItem> dataBaseItems = generateDefaultData();
        saveToDB(db, dataBaseItems);
    }

    private static void saveToDB(serverAddressDatabase db, List<serverAddressItem> dataBaseItems) {
        db.beginTransaction();
        try {
            db.serverAddressDao().insertAll(dataBaseItems);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private static List<serverAddressItem> generateDefaultData() {
        List<serverAddressItem> mList = new ArrayList<>();
        for (int i = 0; i < ConnectionData.size(); i++)
            mList.add(
                    new serverAddressItem(
                            i,
                            ConnectionData.get(i),
                            ConnectionDataDefault.get(i),
                            false,
                            false
                    ));
        return mList;
    }
}
