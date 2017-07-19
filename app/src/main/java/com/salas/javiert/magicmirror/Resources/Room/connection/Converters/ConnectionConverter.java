/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.connection.Converters;

import android.arch.persistence.room.TypeConverter;

import com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton.connectionSettings;
import com.salas.javiert.magicmirror.Resources.Room.connection.Entities.connectionDataBaseItem;


/**
 * Created by javi6 on 7/18/2017.
 */

public class ConnectionConverter {


    @TypeConverter
    public static connectionSettings connToDbItem(connectionDataBaseItem conn) {
        return conn == null ? null : new connectionSettings(conn);
    }

    @TypeConverter
    public static connectionDataBaseItem connDbItemToConn(connectionSettings connSettings) {
        return connSettings == null ? null : new connectionDataBaseItem(connSettings);
    }


}
