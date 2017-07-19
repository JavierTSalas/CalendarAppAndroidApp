/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Views.LiveDataTest;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.salas.javiert.magicmirror.Resources.Room.ConnectionDatabase;
import com.salas.javiert.magicmirror.Resources.Room.DatabaseCreator;
import com.salas.javiert.magicmirror.Resources.Room.connection.Entities.connectionDataBaseItem;

import java.util.List;

/**
 * Created by javi6 on 7/18/2017.
 */

public class ConnectionListViewModel extends AndroidViewModel {

    private LiveData<List<connectionDataBaseItem>> myLiveList;

    public ConnectionListViewModel(Application application) {
        super(application);

        final DatabaseCreator appDatabase = DatabaseCreator.getInstance(application.getApplicationContext());
        myLiveList = Transformations.switchMap(appDatabase.isDatabaseCreated(), new Function<Boolean, LiveData<List<connectionDataBaseItem>>>() {
            @Override
            public LiveData<List<connectionDataBaseItem>> apply(Boolean isDbCreated) {
                if (!isDbCreated) {
                    //noinspection unchecked
                    return null;
                } else {
                    //noinspection ConstantConditions
                    return appDatabase.getDatabase().connectionDao().getAll();
                }
            }
        });

    }

    public void setMyLiveList(LiveData<List<connectionDataBaseItem>> myLiveList) {
        this.myLiveList = myLiveList;
    }

    public LiveData<List<connectionDataBaseItem>> getLiveData() {
        return myLiveList;
    }

    public void deleteItem(connectionDataBaseItem item) {
        final DatabaseCreator databaseCreator = DatabaseCreator.getInstance(this.getApplication());
        new deleteAsyncTask(databaseCreator.getDatabase()).execute(item);
    }

    private static class deleteAsyncTask extends AsyncTask<connectionDataBaseItem, Void, Void> {

        private ConnectionDatabase db;

        deleteAsyncTask(ConnectionDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final connectionDataBaseItem... params) {
            db.connectionDao().deleteIndex(params[0]);
            return null;
        }

    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;


        public Factory(@NonNull Application application) {
            mApplication = application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ConnectionListViewModel(mApplication);
        }
    }
}
