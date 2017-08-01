/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton.connectionSettings;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Adapters.RecyclerAdapter;
import com.salas.javiert.magicmirror.Resources.SwipeHelper.helper.OnStartDragListener;
import com.salas.javiert.magicmirror.Resources.SwipeHelper.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javi6 on 7/2/2017.
 */

public class ConnectionFragment extends Fragment implements OnStartDragListener {
    //TODO: Make these non-static
    final static int countOfIndex = 1; //This is zero based
    private static int indexOfListOnScreen = 0;
    private static List<Pair<Integer, Integer>> pairList = generateListPair();
    private static List<Boolean> dataSetModified = generateBooleanList();
    private List<List<connectionSettings>> dataSets = generateDataSetList(); // This is stupid but the only way I know to get this to work
    private RecyclerView mRecyclerView;
    private List<connectionSettings> myConnectionSetting;
    private ItemTouchHelper mItemTouchHelper;

    private static List<Boolean> generateBooleanList() {
        List<Boolean> myList = new ArrayList<>();
        while (myList.size() < countOfIndex + 1)
            myList.add(false);
        return myList;
    }

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

    private static List<Pair<Integer, Integer>> generateListPair() {
        List<Pair<Integer, Integer>> myListOfPairs = new ArrayList<>();
        //TODO Get this from sharedPerferences or resources
        myListOfPairs.add(new Pair<Integer, Integer>(0, 2));
        myListOfPairs.add(new Pair<Integer, Integer>(2, 4));
        return myListOfPairs;
    }

    private List<List<connectionSettings>> generateDataSetList() {
        List<List<connectionSettings>> myList = new ArrayList<>();
        List<connectionSettings> myListCeption = new ArrayList<>();
        while (myList.size() < countOfIndex + 1)
            myList.add(myListCeption);
        return myList;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_connection, container, false);

        //Required for adding buttons to the ToolBar
        setHasOptionsMenu(true);

        myConnectionSetting = initData(pairList.get(indexOfListOnScreen)); // Load from sharedPreferences
        RecyclerAdapter mAdpater = new RecyclerAdapter(view.getContext(), myConnectionSetting, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.settingsRecyclerView);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdpater);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdpater) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                //super.onSwiped(viewHolder, i);
                Log.d("SWIPE", String.valueOf(i));
                Integer[] myTaskParams = {i, null, null};
                swapRecyclerLists myTask = new swapRecyclerLists();
                myTask.execute(myTaskParams);

                // I don't know why these magic numbers work
                // I just swiped the screen a lot and these were the values I found
                if (i == 32)
                    Log.d("SWIPE", "Left to right");
                if (i == 16)
                    Log.d("SWIPE", "Right to left");

            }
        };

        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        return view;
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
                Toast.makeText(this.getContext(), "Action readFromSharedPerferences selected", Toast.LENGTH_SHORT)
                        .show();
                refreshContentsOfRecyclerView();
                break;
            case R.id.action_connection_save:
                Toast.makeText(this.getContext(), "Action save selected", Toast.LENGTH_SHORT)
                        .show();

                RecyclerAdapter mAdapter = (RecyclerAdapter) mRecyclerView.getAdapter();
                saveFromRecyclerView(mAdapter.haveChangesBeenMade());
                break;

            default:
                break;
        }

        return true;
    }

    private void saveFromRecyclerView(boolean nullifyIndex) {
        List<connectionSettings> updatedItems = ((RecyclerAdapter) mRecyclerView.getAdapter()).getConnectionSettings();
        if (nullifyIndex) {
            dataSetModified.set(indexOfListOnScreen, true);
            dataSets.set(indexOfListOnScreen, new ArrayList<connectionSettings>());
        }
        //myConnectionSingleton.getInstance().saveToPrefences(getContext(), updatedItems); // FIXME: 7/31/2017 
    }

    // Fetch the data from SharedPreferences and reset the adapter
    // Use this when dataset is the same
    private void refreshContentsOfRecyclerView() {

        List<connectionSettings> myTempList = initData(pairList.get(indexOfListOnScreen));

        // Get a list of indexes that need updating, only update those since we want the others to stay the same
        List<Integer> indexesThatNeedUpdating = compareListsForChanges(myConnectionSetting, myTempList);
        for (int index : indexesThatNeedUpdating) {
            mRecyclerView.getAdapter().notifyItemChanged(index);
            Log.d("Redrawing", "Child number " + String.valueOf(index));
        }
        Toast.makeText(getActivity(), "Reloading RecyclerView Content with old content",
                Toast.LENGTH_LONG).show();

    }

    // Fetch the data from SharedPreferences and reset the adapter
    // Use this when dataset was changed and needs to be reset
    private void redrawRecyclerView() {

        List<connectionSettings> myTempList = initData(pairList.get(indexOfListOnScreen));
        RecyclerAdapter mAdapter = new RecyclerAdapter(getContext(), myTempList, this);
        mRecyclerView.swapAdapter(mAdapter, false);

        // Get a list of indexes that need updating, only update those since we want the others to stay the same
        for (int index = 0; index < myTempList.size(); index++) {
            mRecyclerView.getAdapter().notifyItemChanged(index);
            Log.d("Redrawing", "Child number " + String.valueOf(index));
        }
        Toast.makeText(getActivity(), "Reloading RecyclerView Content with new content , number:" + indexOfListOnScreen,
                Toast.LENGTH_LONG).show();

    }

    private List<Integer> compareListsForChanges(List<connectionSettings> myConnectionSetting, List<connectionSettings> myTempList) {
        List<Integer> indexDifferent = new ArrayList<>();
        Log.d("CompareList", "Comparing list of size:" + myConnectionSetting.size() + " and " + myTempList.size());
        for (int i = 0; i < myConnectionSetting.size(); i++)
            if (!myConnectionSetting.get(i).isSameAs(myTempList.get(i)))
                indexDifferent.add(i);
        return indexDifferent;
    }

    // Fetches the data from the preferences if there is none, then create default
    private List<connectionSettings> initData(Pair<Integer, Integer> myPair) {
        return null; //myConnectionSingleton.getInstance().loadFromPreferences(getContext(), myPair); // FIXME: 7/31/2017 
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

    }

    // TODO: Make this non-static, but there's only one instance of it so it every running so it shouldn't matter
    // This class handles swipe detection for us
    protected static class SwipeDetector implements View.OnTouchListener {

        private static final String logTag = "SwipeDetector";
        private static final int MIN_DISTANCE = 100;
        private float downX, downY, upX, upY;
        private Action mSwipeDetected = Action.None;

        public boolean swipeDetected() {
            return mSwipeDetected != Action.None;
        }

        public Action getAction() {
            return mSwipeDetected;
        }

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    downY = event.getY();
                    mSwipeDetected = Action.None;
                    return false; // allow other events like Click to be processed
                }
                case MotionEvent.ACTION_MOVE: {
                    upX = event.getX();
                    upY = event.getY();

                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    // horizontal swipe detection
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // left or right
                        if (deltaX < 0) {
                            Log.d(logTag, "Swipe Left to Right");
                            mSwipeDetected = Action.LR;
                            return true;
                        }
                        if (deltaX > 0) {
                            Log.d(logTag, "Swipe Right to Left");
                            mSwipeDetected = Action.RL;
                            return true;
                        }
                    } else

                        // vertical swipe detection
                        if (Math.abs(deltaY) > MIN_DISTANCE) {
                            // top or down
                            if (deltaY < 0) {
                                Log.d(logTag, "Swipe Top to Bottom");
                                mSwipeDetected = Action.TB;
                                return false;
                            }
                            if (deltaY > 0) {
                                Log.d(logTag, "Swipe Bottom to Top");
                                mSwipeDetected = Action.BT;
                                return false;
                            }
                        }
                    return true;
                }
            }
            return false;
        }

        public enum Action {
            LR, // Left to Right
            RL, // Right to Left
            TB, // Top to bottom
            BT, // Bottom to Top
            None // when no action was detected
        }
    }

    private class swapRecyclerLists extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RecyclerAdapter mAdapter = (RecyclerAdapter) mRecyclerView.getAdapter();
            saveFromRecyclerView(mAdapter.haveChangesBeenMade());

        }

        @Override
        protected Void doInBackground(Integer... params) {

            // Set the indexOfListOnScreen
            if (params[0].equals(16))
                swapLeft();
            if (params[0].equals(32))
                swapRight();

            // Try to get the data if we have it already loaded
            if (dataSets.get(indexOfListOnScreen).size() > 0) {
                myConnectionSetting = dataSets.get(indexOfListOnScreen);
            } else {
                // Reset the list of the data so we can fill it
                myConnectionSetting = new ArrayList<>(initData(pairList.get(indexOfListOnScreen)));
                dataSets.set(indexOfListOnScreen, myConnectionSetting);
                Log.d("SwapTask", "Index in dataSets not found, fetching list form SharedPreferences and adding it to dataSets");
            }
            Log.d("SwapTask", "Swapping lists");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("SwapTask", "Redrawing RecyclerView");
            redrawRecyclerView();
        }
    }
}
