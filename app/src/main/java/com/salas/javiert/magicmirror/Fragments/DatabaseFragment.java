/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Fragments;

import android.arch.lifecycle.LifecycleFragment;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton.connectionSettings;
import com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton.myConnectionSingleton;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.Entities.serverAddressItem;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabase;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabaseCreator;
import com.salas.javiert.magicmirror.Views.DataBindingTest.DatabaseFragment.Fragment.RecyclerViewClasses.dataBindingAdapter;
import com.salas.javiert.magicmirror.databinding.LayoutFragmentDatabaseBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javi6 on 7/16/2017.
 */

public class DatabaseFragment extends LifecycleFragment {

    // Variables for swapping the RecyclerViews
    final static int countOfIndex = 1; //This is zero based
    private static int indexOfListOnScreen = 0;
    private LayoutFragmentDatabaseBinding mBinding;
    private dataBindingAdapter mAdapter;
    private RecyclerView mRecycler;
    private List<Pair<Integer, Integer>> pairList = Generator.generateListPair();
    private List<Boolean> dataSetModified = Generator.generateBooleanList();
    private List<List<connectionSettings>> dataSets = Generator.generateDataSetList();

    public static void swapLeft() {
        // Wrap around
        if (indexOfListOnScreen == 0) {
            indexOfListOnScreen = countOfIndex;
        } else {
            indexOfListOnScreen--;
        }

    }

    public static void swapRight() {
        // Wrap around
        if (indexOfListOnScreen == countOfIndex) {
            indexOfListOnScreen = 0;
        } else {
            indexOfListOnScreen++;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Find the databinding elements and inflate the layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_database, container, false);
        // Get the view so we can still use findViewById
        View view = mBinding.getRoot();

        configDataBind(mBinding);

        mBinding.bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearList();
            }


        });
        mBinding.bQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAction();
            }
        });

        mRecycler = mBinding.Recycler;
        mAdapter = new dataBindingAdapter(new ArrayList<serverAddressItem>());
        mRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mRecycler.setAdapter(mAdapter);


        return view;
    }

    private void clearList() {
        List<serverAddressItem> myList = new ArrayList<>();
        mAdapter.setItems(myList);
    }
    //Fill the string arrays from the resources, we need context to use getResources()

    private void configDataBind(LayoutFragmentDatabaseBinding mBinding) {
        int ColumdID = 1;
        String Tv1 = "Please enter a column number";
        String Tv2 = "Please enter a action number";
        String b1 = "SAVE";
        String b2 = "QUERY";


        mBinding.setColumnID(String.valueOf(ColumdID));
        mBinding.setTextView1Name(Tv1);
        mBinding.setTextView2Name(Tv2);
        mBinding.setIsLoading(true);
        mBinding.bAction.setText(b1);
        mBinding.bQuery.setText(b2);
    }

    private void queryAction() {
        final serverAddressDatabaseCreator serverAddressDatabaseCreator = com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabaseCreator.getInstance(this.getContext());

        new AsyncTask<Void, Void, Void>() {

            List<serverAddressItem> fetchedList;

            @Override
            protected void onPreExecute() {
                if (!serverAddressDatabaseCreator.isDatabaseCreated().getValue())
                    serverAddressDatabaseCreator.createDb(getContext());
                mBinding.setIsLoading(true);
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                serverAddressDatabase db = serverAddressDatabaseCreator.getDatabase();
                fetchedList = db.serverAddressDao().getConnections(0, Integer.valueOf(mBinding.getColumnID()));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.setItems(fetchedList);
                mBinding.setIsLoading(false);
            }
        }.execute();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    // Fetches the data from room if there is none, then create default (this is done elsewhere but I thought it should be mentioned)
    private List<connectionSettings> initData(Pair<Integer, Integer> myPair) {
        return myConnectionSingleton.getInstance().loadFromPreferences(getContext(), myPair);
    }

    private List<Integer> compareListsForChanges(List<connectionSettings> myConnectionSetting, List<connectionSettings> myTempList) {
        List<Integer> indexDifferent = new ArrayList<>();
        Log.d("CompareList", "Comparing list of size:" + myConnectionSetting.size() + " and " + myTempList.size());
        for (int i = 0; i < myConnectionSetting.size(); i++)
            if (!myConnectionSetting.get(i).isSameAs(myTempList.get(i)))
                indexDifferent.add(i);
        return indexDifferent;
    }

    private void saveFromRecyclerView(boolean createNewArrayAtIndex) {
        List<serverAddressItem> updatedItems = mAdapter.getConnectionSettings();
        if (createNewArrayAtIndex) {
            dataSetModified.set(indexOfListOnScreen, true);
            dataSets.set(indexOfListOnScreen, new ArrayList<connectionSettings>());
        }


        myConnectionSingleton.getInstance().saveToRoom(getContext(), updatedItems);
    }


    protected static class Generator {
        private static List<Pair<Integer, Integer>> generateListPair() {
            List<Pair<Integer, Integer>> myListOfPairs = new ArrayList<>();
            //TODO Get this from sharedPerferences or resources
            myListOfPairs.add(new Pair<Integer, Integer>(0, 2));
            myListOfPairs.add(new Pair<Integer, Integer>(2, 4));
            return myListOfPairs;
        }

        private static List<List<connectionSettings>> generateDataSetList() {
            List<List<connectionSettings>> myList = new ArrayList<>();
            List<connectionSettings> myListCeption = new ArrayList<>();
            while (myList.size() < countOfIndex + 1)
                myList.add(myListCeption);
            return myList;
        }

        private static List<Boolean> generateBooleanList() {
            List<Boolean> myList = new ArrayList<>();
            while (myList.size() < countOfIndex + 1)
                myList.add(false);
            return myList;
        }
    }
}
