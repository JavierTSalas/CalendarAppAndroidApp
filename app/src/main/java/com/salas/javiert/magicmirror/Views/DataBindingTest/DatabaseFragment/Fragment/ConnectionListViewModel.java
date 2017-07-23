/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Views.DataBindingTest.DatabaseFragment.Fragment;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.salas.javiert.magicmirror.Resources.Room.serverAddress.Entities.serverAddressItem;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabase;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabaseCreator;

import java.util.List;

/**
 * Created by javi6 on 7/18/2017.
 */

public class ConnectionListViewModel extends AndroidViewModel {

    private LiveData<List<serverAddressItem>> myLiveList;

    public ConnectionListViewModel(Application application) {
        super(application);

        final serverAddressDatabaseCreator appDatabase = serverAddressDatabaseCreator.getInstance(application.getApplicationContext());

        appDatabase.createDb(this.getApplication());

        /*
        myLiveList = Transformations.switchMap(appDatabase.isDatabaseCreated(),
                new Function<Boolean, LiveData<List<serverAddressItem>>>() {
            @Override
            public LiveData<List<serverAddressItem>> apply(Boolean isDbCreated) {
                if (!Boolean.TRUE.equals(isDbCreated)) {// Not needed here, but watch out for null
                    //noinspection unchecked
                    return null;
                } else {
                    //noinspection ConstantConditions
                    return appDatabase.getDatabase().connectionDao().getAll();
                }
            }
        });
        */
    }

    public void setMyLiveList(LiveData<List<serverAddressItem>> LiveList) {
        myLiveList = LiveList;
        Log.d("myLiveList", "has been changed");
    }

    public LiveData<List<serverAddressItem>> getLiveData() {
        return myLiveList;
    }

    public void deleteItem(serverAddressItem item) {
        final serverAddressDatabaseCreator serverAddressDatabaseCreator = com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabaseCreator.getInstance(this.getApplication());
        new deleteAsyncTask(serverAddressDatabaseCreator.getDatabase()).execute(item);
    }

    private static class deleteAsyncTask extends AsyncTask<serverAddressItem, Void, Void> {

        private serverAddressDatabase db;

        deleteAsyncTask(serverAddressDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final serverAddressItem... params) {
            db.serverAddressDao().deleteIndex(params[0]);
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
