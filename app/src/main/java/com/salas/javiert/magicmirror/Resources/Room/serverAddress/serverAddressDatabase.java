/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.serverAddress;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.salas.javiert.magicmirror.Resources.Room.serverAddress.Converters.serverAddressConverter;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.Dao.serverAddressDao;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.Entities.serverAddressItem;

/**
 * Created by javi6 on 7/16/2017.
 */

@Database(entities = {serverAddressItem.class}, version = 1)
@TypeConverters(serverAddressConverter.class)
public abstract class serverAddressDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "connection-db";

    public abstract serverAddressDao serverAddressDao();
}
