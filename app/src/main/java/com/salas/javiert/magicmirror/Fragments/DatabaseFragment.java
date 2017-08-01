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
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private List<Pair<Integer, Integer>> pairList = Generator.generateListPair();
    private List<Boolean> dataSetModified = Generator.generateBooleanList();
    private List<List<serverAddressItem>> dataSets = Generator.generateDataSetList();


    // TODO find a better way
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

        //Required for adding buttons to the ToolBar
        setHasOptionsMenu(true);

        // Set up our data
        configDataBind(mBinding);


        /*
        mBinding.bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearList();
            }


        });
        */
        mBinding.bQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAction();
            }
        });
        mBinding.bAction.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Integer[] myTaskParams = {16, null, null};
                changeRecyclerData myTask = new changeRecyclerData();
                myTask.execute(myTaskParams);
                // TODO find a better way to swipe between views
                return false;
            }
        });


        mAdapter = new dataBindingAdapter(new ArrayList<serverAddressItem>());
        mBinding.Recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mBinding.Recycler.setAdapter(mAdapter);

/*
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdpater) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                //super.onSwiped(viewHolder, i);
                Log.d("SWIPE", String.valueOf(i));
                Integer[] myTaskParams = {i, null, null};
                DatabaseFragment.changeRecyclerData myTask = new DatabaseFragment.changeRecyclerData();
                myTask.execute(myTaskParams);

                // I don't know why these magic numbers work
                // I just swiped the screen a lot and these were the values I found
                if (i == 32)
                    Log.d("SWIPE", "Left to right");
                if (i == 16)
                    Log.d("SWIPE", "Right to left");

            }
        };
*/

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_connections, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //Called when a menu option is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_connection_load:
                Toast.makeText(this.getContext(), "Action load selected", Toast.LENGTH_SHORT)
                        .show();
                refreshContentsOfRecyclerView();
                break;
            case R.id.action_connection_save:
                Toast.makeText(this.getContext(), "Action save selected", Toast.LENGTH_SHORT)
                        .show();

                saveFromRecyclerView(mAdapter.haveChangesBeenMade());
                break;

            default:
                break;
        }

        return true;
    }

    // Fetch the data from SharedPreferences and reset the adapter
    // Use this when dataset is the same
    private void refreshContentsOfRecyclerView() {
        final serverAddressDatabaseCreator creator = serverAddressDatabaseCreator.getInstance(getContext());
        new AsyncTask<Void, Void, Void>() {
            List<serverAddressItem> myTempList;
            Pair<Integer, Integer> myPair;


            @Override
            protected void onPreExecute() {
                if (creator.isDatabaseCreated().getValue().equals(Boolean.FALSE))
                    creator.createDb(getContext());
                myPair = pairList.get(indexOfListOnScreen);
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                myTempList = creator.getDatabase().serverAddressDao().getConnections(myPair.first, myPair.second);
                mAdapter.setItems(myTempList);
                return null;
            }
        }.execute();

        Toast.makeText(getActivity(), "Reloading RecyclerView Content with old content",
                Toast.LENGTH_LONG).show();

    }

    // Config our bound data
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

    private void clearList() {
        List<serverAddressItem> myList = new ArrayList<>();
        mAdapter.setItems(myList);
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

    // Fetches the data from room if there is none, then create default (this is done elsewhere but I thought it should be mentioned)
    private List<serverAddressItem> initData(Pair<Integer, Integer> myPair) {
        //return myConnectionSingleton.getInstance().loadFromPreferences(getContext(), myPair);
        return null; // TODO this
    }

    // Save the items that we changed and set
    private void saveFromRecyclerView(boolean createNewArrayAtIndex) {
        List<serverAddressItem> updatedItems = mAdapter.getModifiedItems();
        if (createNewArrayAtIndex) {
            dataSetModified.set(indexOfListOnScreen, true); // TODO We don't need this unless we want users to have multiple pages
            dataSets.set(indexOfListOnScreen, new ArrayList<serverAddressItem>());
        }


        myConnectionSingleton.getInstance().saveToRoom(getContext(), updatedItems);
    }

    // Generate placeholder data
    protected static class Generator {

        // Used in serverAdddressDao().getConnections(Integer floor, Integer ceiling)
        private static List<Pair<Integer, Integer>> generateListPair() {
            List<Pair<Integer, Integer>> myListOfPairs = new ArrayList<>();
            // Pair n,m are used in the following manner:
            // n is the first index in which room will fetch
            // m is the last index which room will fetch
            // Room is 0 based
            // (0,1) will return 2 indexes
            // (2,6) will return 5 indexes
            myListOfPairs.add(new Pair<Integer, Integer>(0, 1));
            myListOfPairs.add(new Pair<Integer, Integer>(2, 6));
            return myListOfPairs;
        }

        // Produces datasets for our data to be 'cached' so that we do not need to fetch it every time we change indexes
        // The logic for making sure this operators correctly 
        // i.e overwritten and reset indexes when needed are in changeRecyclerData
        private static List<List<serverAddressItem>> generateDataSetList() {
            List<List<serverAddressItem>> myList = new ArrayList<>();
            List<serverAddressItem> myListCeption = new ArrayList<>();
            while (myList.size() < countOfIndex + 1)
                myList.add(myListCeption);
            return myList;
        }

        // The result of this will be used to determine if there was a change in the index of dataset 
        // TODO Make sure we need this - I don't think we do
        private static List<Boolean> generateBooleanList() {
            List<Boolean> myList = new ArrayList<>();
            while (myList.size() < countOfIndex + 1)
                myList.add(false);
            return myList;
        }
    }

    // This Task toggles between the server fields and server pages
    // Separating this to make users entering sever address more reliable
    private class changeRecyclerData extends AsyncTask<Integer, Void, Void> {

        List<serverAddressItem> items;
        Pair<Integer, Integer> myPair;
        serverAddressDatabaseCreator creator;
        serverAddressDatabase database;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            creator = serverAddressDatabaseCreator.getInstance(getContext());
            // If there is no database, then make one
            if (creator.isDatabaseCreated().getValue().equals(Boolean.FALSE))
                creator.createDb(getContext());
            myPair = pairList.get(indexOfListOnScreen);

            // Save our data to dataSets so we do not need to load it again, if there have been changes
            // then the dataSet will be set to a new ArrayList so that when we go back to attempt 
            // to view the data it will be fetched when needed
            saveFromRecyclerView(mAdapter.haveChangesBeenMade());

            // Show the user that we are doing some work
            mBinding.setIsLoading(true);
        }

        @Override
        protected Void doInBackground(Integer... params) {

            // This has to be done in background since our database might not be initialized yet 
            database = creator.getDatabase();

            // TODO not swap but toggle lists... maybe? Users might want another list for whatever they want doubt it though
            if (params[0].equals(16))
                swapLeft();
            if (params[0].equals(32))
                swapRight();

            // Try to get the data if we have it already
            // if we don't have a populated list in the index then fetch the list from room and set it
            if (dataSets.get(indexOfListOnScreen).size() == 0) {
                Log.d("SwapTask", "Index in dataSets not found, fetching list form SharedPreferences and adding it to dataSets");

                // Fetch the list
                List<serverAddressItem> list = database.serverAddressDao().getConnections(myPair.first, myPair.second);
                // Set it
                dataSets.set(indexOfListOnScreen, list);


            }
            // Get the items that we are going to populate on screen
            items = dataSets.get(indexOfListOnScreen);
            Log.d("SwapTask", "Swapping lists");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // We are done loading
            mBinding.setIsLoading(false);
            // Show the user their items
            mAdapter.setItems(items);
        }
    }

}
