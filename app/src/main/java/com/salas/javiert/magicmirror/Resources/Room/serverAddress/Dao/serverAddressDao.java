/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.serverAddress.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.salas.javiert.magicmirror.Resources.Room.serverAddress.Entities.serverAddressItem;

import java.util.List;

/**
 * Created by javi6 on 7/16/2017.
 */

@Dao
public interface serverAddressDao {
    @Query("SELECT * FROM connection")
    List<serverAddressItem> getAll();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void setConnectionDataBaseItem(serverAddressItem... item);

    @Query("SELECT * FROM connection WHERE id BETWEEN :floor AND :ceiling")
    List<serverAddressItem> getConnections(Integer floor, Integer ceiling);

    @Query("SELECT * FROM connection WHERE id = :selection")
    LiveData<List<serverAddressItem>> getIndex(Integer selection);

    @Delete
    void deleteIndex(serverAddressItem selection);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<serverAddressItem> serverAddressItems);

}
