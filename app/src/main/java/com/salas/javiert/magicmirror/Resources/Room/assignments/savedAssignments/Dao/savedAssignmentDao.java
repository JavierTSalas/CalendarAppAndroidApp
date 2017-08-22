/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.assignments.savedAssignments.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.salas.javiert.magicmirror.Resources.Room.assignments.savedAssignments.Entities.savedAssignment;

import java.util.List;

/**
 * Created by javi6 on 8/4/2017.
 */
@Dao
public interface savedAssignmentDao {
    @Query("SELECT * FROM saved_assignments")
    List<savedAssignment> getAll();

    @Query("SELECT * FROM saved_assignments WHERE completed = :status")
    List<savedAssignment> getAllWhereCompleteIs(boolean status);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAssignment(savedAssignment... savedAssignments);

    @Delete
    void deleteAssignment(savedAssignment savedAssignments);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<savedAssignment> savedAssignments);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(savedAssignment savedAssignment);
}
