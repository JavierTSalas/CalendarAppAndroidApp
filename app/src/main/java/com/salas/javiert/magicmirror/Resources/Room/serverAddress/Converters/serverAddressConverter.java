/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.serverAddress.Converters;

import android.arch.persistence.room.TypeConverter;

import com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton.connectionSettings;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.Entities.serverAddressItem;


/**
 * Created by javi6 on 7/18/2017.
 */

public class serverAddressConverter {


    @TypeConverter
    public static connectionSettings connToDbItem(serverAddressItem conn) {
        return conn == null ? null : new connectionSettings(conn);
    }

    @TypeConverter
    public static serverAddressItem connDbItemToConn(connectionSettings connSettings) {
        return connSettings == null ? null : new serverAddressItem(connSettings);
    }


}
