/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.assignments.savedEvent.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.salas.javiert.magicmirror.Resources.Room.assignments.savedEvent.Entities.savedEvent;

import java.util.List;

/**
 * Created by javi6 on 8/4/2017.
 */
@Dao
public interface savedEventDao {
    @Query("SELECT * FROM saved_events")
    List<savedEvent> getAll();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEvent(savedEvent... event);

    @Delete
    void deleteEvent(savedEvent event);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<savedEvent> events);
}
