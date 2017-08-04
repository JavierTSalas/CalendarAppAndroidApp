/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.classTime;

import android.content.Context;

import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Room.classTime.Entities.classTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Javier on 8/2/2017.
 */

public class classTimeDatabaseInitUtil {
    private static List<String> DefaultClassList;

    //Fill the string arrays from the resources, we need context to use getResources()
    private static void setUpStringArrays(Context context) {
        DefaultClassList = Arrays.asList(context.getResources().getStringArray(R.array.class_list_default));
    }

    public static void initializeDb(classTimeDatabase db, Context context) {
        setUpStringArrays(context);
        List<classTime> dataBaseItems = generateDefaultData();
        saveToDB(db, dataBaseItems);
    }

    private static void saveToDB(classTimeDatabase db, List<classTime> dataBaseItems) {
        db.beginTransaction();
        try {
            db.classTimeDao().insertAll(dataBaseItems);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private static List<classTime> generateDefaultData() {
        List<classTime> mList = new ArrayList<>();
        for (int i = 0; i < DefaultClassList.size(); i++)
            mList.add(
                    new classTime());
        return mList;
    }
}
