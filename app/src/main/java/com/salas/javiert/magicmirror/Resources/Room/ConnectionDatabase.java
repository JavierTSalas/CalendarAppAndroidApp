/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.salas.javiert.magicmirror.Resources.Room.connection.Converters.ConnectionConverter;
import com.salas.javiert.magicmirror.Resources.Room.connection.Dao.ConnectionDao;
import com.salas.javiert.magicmirror.Resources.Room.connection.Entities.connectionDataBaseItem;

/**
 * Created by javi6 on 7/16/2017.
 */

@Database(entities = {connectionDataBaseItem.class}, version = 1)
@TypeConverters(ConnectionConverter.class)
public abstract class ConnectionDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "connection-db";

    public abstract ConnectionDao connectionDao();
}
