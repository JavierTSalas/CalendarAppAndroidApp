/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.assignments.savedEvent;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.salas.javiert.magicmirror.Resources.Room.assignments.savedEvent.Dao.savedEventDao;
import com.salas.javiert.magicmirror.Resources.Room.assignments.savedEvent.Entities.savedEvent;

/**
 * Created by javi6 on 8/4/2017.
 */

@Database(entities = {savedEvent.class}, version = 1)
public abstract class savedEventDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "saved_events-db";

    public abstract savedEventDao savedEventDao();

}
