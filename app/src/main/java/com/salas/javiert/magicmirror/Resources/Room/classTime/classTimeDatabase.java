/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.classTime;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.salas.javiert.magicmirror.Resources.Room.classTime.Dao.classTimeDao;
import com.salas.javiert.magicmirror.Resources.Room.classTime.Entities.classTime;

/**
 * Created by Javier on 8/2/2017.
 */

@Database(entities = {classTime.class}, version = 1)
public abstract class classTimeDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "classes-db";

    public abstract classTimeDao classTimeDao();

}
