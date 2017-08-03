/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Room.classTime;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Creates the {@link serverAddressDatabase} asynchronously, exposing a LiveData object to notify of creation.
 */

/**
 * Created by Javier on 8/2/2017.
 */

public class classTimeDatabaseCreator {  // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static classTimeDatabaseCreator sInstance;
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();
    private final AtomicBoolean mInitializing = new AtomicBoolean(true);
    private classTimeDatabase mDb;

    private classTimeDatabaseCreator() {
        mIsDatabaseCreated.setValue(false);
    }

    public synchronized static classTimeDatabaseCreator getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new classTimeDatabaseCreator();
                }
            }
        }
        return sInstance;
    }

    /**
     * Used to observe when the database initialization is done
     */
    public LiveData<Boolean> isDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    @Nullable
    public classTimeDatabase getDatabase() {
        return mDb;
    }

    /**
     * Creates or returns a previously-created database.
     * <p>
     * Although this uses an AsyncTask which currently uses a serial executor, it's thread-safe.
     */
    public void createDb(Context context) {

        Log.d("serverAddressDBCreator", "Creating DB from " + Thread.currentThread().getName());

        if (!mInitializing.compareAndSet(true, false)) {
            return; // Already initializing
        }

        // If there is no database then make one
        if (mIsDatabaseCreated.getValue() == false)
            new AsyncTask<Context, Void, Void>() {

                @Override
                protected Void doInBackground(Context... params) {
                    Log.d("serverAddressDBCreator",
                            "Starting bg job " + Thread.currentThread().getName());

                    Context context = params[0].getApplicationContext();

                    // Reset the database to have new data on every run.
                    // context.deleteDatabase(serverAddressDatabase.DATABASE_NAME);

                    // Build the database!
                    classTimeDatabase db = Room.databaseBuilder(context.getApplicationContext(),
                            classTimeDatabase.class, classTimeDatabase.DATABASE_NAME).build();

                    // Add some data to the database
                    classTimeDatabaseInitUtil.initializeDb(db, context);
                    Log.d("serverAddressDBCreator",
                            "DB was populated in thread " + Thread.currentThread().getName());

                    mDb = db;
                    return null;
                }

                @Override
                protected void onPostExecute(Void ignored) {
                    // Now on the main thread, notify observers that the db is created and ready.
                    mIsDatabaseCreated.setValue(true);
                }
            }.execute(context.getApplicationContext());
    }

}
