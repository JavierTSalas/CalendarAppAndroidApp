/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.classTime.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.salas.javiert.magicmirror.Resources.Room.classTime.Entities.classTime;

import java.util.List;

/**
 * Created by Javier on 8/2/2017.
 */

@Dao
public interface classTimeDao {
    @Query("SELECT * FROM classes")
    public List<classTime> getAll();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateClasses(classTime... classes);

    @Delete
    void deleteClass(classTime foo);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<classTime> classTime);
}
