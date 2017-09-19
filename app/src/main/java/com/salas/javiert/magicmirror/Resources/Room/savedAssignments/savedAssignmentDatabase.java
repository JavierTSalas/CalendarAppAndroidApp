/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.savedAssignments;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.salas.javiert.magicmirror.Resources.Room.savedAssignments.Dao.savedAssignmentDao;
import com.salas.javiert.magicmirror.Resources.Room.savedAssignments.Entities.savedAssignment;

/**
 * Created by javi6 on 8/4/2017.
 */

@Database(entities = {savedAssignment.class}, version = 1)
public abstract class savedAssignmentDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "saved_assignments-db";

    public abstract savedAssignmentDao savedAssignmentDao();

}
