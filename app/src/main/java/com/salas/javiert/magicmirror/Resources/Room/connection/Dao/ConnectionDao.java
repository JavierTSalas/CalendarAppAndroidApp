/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.connection.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.salas.javiert.magicmirror.Resources.Room.connection.Entities.connectionDataBaseItem;

import java.util.List;

/**
 * Created by javi6 on 7/16/2017.
 */

@Dao
public interface ConnectionDao {
    @Query("SELECT * FROM connection")
    LiveData<List<connectionDataBaseItem>> getAll();

    @Query("UPDATE connection SET connectionSuccessful = :bool WHERE id IN (:ids)")
    void setConnectionStatus(int[] ids, boolean bool);

    @Query("UPDATE connection SET connectionSuccessful = :bool WHERE id = :id")
    void setConnectionStatus(int id, boolean bool);

    @Query("UPDATE connection SET lockStatus = :bool WHERE id IN (:ids)")
    void setLockStatus(int[] ids, boolean bool);

    @Query("UPDATE connection SET lockStatus = :bool WHERE id = :id")
    void setLockStatus(int id, boolean bool);

    @Query("UPDATE connection SET name = :name ,subtitle = :subtitle WHERE id = :id")
    void setStrings(String name, String subtitle, Integer id);

    @Query("SELECT * FROM connection WHERE id BETWEEN :floor AND :ceiling")
    LiveData<List<connectionDataBaseItem>> getConnections(Integer floor, Integer ceiling);

    @Query("SELECT * FROM connection WHERE id = :selection")
    LiveData<List<connectionDataBaseItem>> getIndex(Integer selection);

    @Delete
    void deleteIndex(connectionDataBaseItem selection);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<connectionDataBaseItem> connectionDataBaseItems);

}
