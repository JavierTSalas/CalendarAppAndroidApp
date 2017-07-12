/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;
import com.salas.javiert.magicmirror.Objects.SingletonObjects_REMOVE_ME.myConnectionSingleton.connectionSettings;
import com.salas.javiert.magicmirror.Objects.SingletonObjects_REMOVE_ME.myConnectionSingleton.myConnectionSingleton;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Adapters.RecyclerAdapter;
import com.salas.javiert.magicmirror.Resources.DatabaseRestClient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by javi6 on 7/2/2017.
 */

public class ConnectionFragment extends Fragment {
    final static int countOfIndex = 2;
    public static boolean isAddressServerFeildDataFilled, isDoingWork;
    public static int indexOfListOnScreen;
    public static List<Integer> listOfIndexesOfData = generateEmptyListCountingTo();
    public static List<Pair<Integer, Integer>> pairList = generateListPair();
    ImageView ivCloud;
    TextView tvConnectionStatus;
    RecyclerView mRecyclerView;
    List<connectionSettings> myConnectionSetting;
    List<List<connectionSettings>> ListOfDataShowing = generateEmptyListsOfListconnectionSettings();
    Boolean connectionSuccessful = false; // TODO: Save this

    private static List<Integer> generateEmptyListCountingTo() {
        List<Integer> countingList = new ArrayList<>();
        for (int i = 0; i < countOfIndex; i++)
            countingList.add(i);
        return countingList;
    }

    public static void swapLeft() {
        if (indexOfListOnScreen == 0) {
            indexOfListOnScreen = countOfIndex;
        } else {
            indexOfListOnScreen--;
        }

    }

    public static void swapRight() {
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
        myListOfPairs.add(new Pair<Integer, Integer>(3, 7));
        return myListOfPairs;
    }

    private List<List<connectionSettings>> generateEmptyListsOfListconnectionSettings() {
        List<List<connectionSettings>> myList = new ArrayList<>();
        for (int i = 0; i < indexOfListOnScreen; i++)
            myList.add(new ArrayList<connectionSettings>());

        return myList;
    }

    public String getPingURL() {
        final String pingURL = "outputtodo.php"; //TODO: This
        return pingURL;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_connection, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.settingsRecyclerView);
        myConnectionSetting = initData(pairList.get(indexOfListOnScreen)); // Load from sharedPreferences
        RecyclerAdapter mAdpater = new RecyclerAdapter(view.getContext(), myConnectionSetting);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerView.setAdapter(mAdpater);

        final SwipeDetector swipeDetector = new SwipeDetector();
        final swapRecyclerLists swapRecyclerViewsTask = new swapRecyclerLists();

        RecyclerView.OnItemTouchListener listener = new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                if (swipeDetector.swipeDetected()) {

                    Log.d("swapRecyclerLists", "ACTION PRESSED");
                    // The mess around the task prevents more than one task running
                    switch (swipeDetector.getAction()) {


                        case LR: {
                            if (swapRecyclerViewsTask.getStatus() == AsyncTask.Status.FINISHED) {
                                // My AsyncTask is done and onPostExecute was called
                                Integer[] myTaskInts = {0, null, null};
                                swapRecyclerViewsTask.execute(myTaskInts);
                            } else if (swapRecyclerViewsTask.getStatus() == AsyncTask.Status.PENDING) {
                                swapRecyclerViewsTask.execute();
                            } else {
                                Log.d("swapRecyclerLists", "Preventing another task running");

                            }
                        }
                        break;

                        case RL: {
                            {
                                if (swapRecyclerViewsTask.getStatus() == AsyncTask.Status.FINISHED) {
                                    // My AsyncTask is done and onPostExecute was called
                                    Integer[] myTaskInts = {1, null, null};
                                    swapRecyclerViewsTask.execute(myTaskInts);
                                } else if (swapRecyclerViewsTask.getStatus() == AsyncTask.Status.PENDING) {
                                    swapRecyclerViewsTask.execute();
                                } else {
                                    Log.d("swapRecyclerLists", "Preventing another task running");

                                }
                            }
                            break;
                        }
                    }
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        };


        mRecyclerView.setOnTouchListener(swipeDetector);
        mRecyclerView.addOnItemTouchListener(listener);
        mRecyclerView.setHasFixedSize(true);

        //Required for adding buttons to the ToolBar
        setHasOptionsMenu(true);

        // The 'status'
        tvConnectionStatus = (TextView) view.findViewById(R.id.tvConnectedToServer);

        // The image view
        ivCloud = (ImageView) view.findViewById(R.id.ivConnection);
        ivCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tryConnection(getContext());
            }
        });

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
                reloadRecyclerView();
                break;
            case R.id.action_connection_save:
                Toast.makeText(this.getContext(), "Action save selected", Toast.LENGTH_SHORT)
                        .show();
                saveFromRecyclerView();
                break;

            default:
                break;
        }

        return true;
    }

    private void saveFromRecyclerView() {
        myConnectionSingleton.getInstance().saveToPrefences(getContext(), ((RecyclerAdapter) mRecyclerView.getAdapter()).getConnectionSettings());
    }

    // Fetch the data from SharedPreferences and reset the adapter
    private void reloadRecyclerView() {

        List<connectionSettings> myTempList = initData(pairList.get(indexOfListOnScreen));

        // Get a list of indexes that need updating, only update those since we want the others to stay the same
        List<Integer> indexesThatNeedUpdating = compareListsForChanges(myConnectionSetting, myTempList);
        for (int index : indexesThatNeedUpdating) {
            mRecyclerView.getAdapter().notifyItemChanged(index);
            Log.d("Redrawing", "Child number " + String.valueOf(index));
        }

    }

    private List<Integer> compareListsForChanges(List<connectionSettings> myConnectionSetting, List<connectionSettings> myTempList) {
        List<Integer> indexDifferent = new ArrayList<>();
        for (int i = 0; i < myConnectionSetting.size(); i++)
            if (!myConnectionSetting.get(i).isSameAs(myTempList.get(i)))
                indexDifferent.add(i);
        return indexDifferent;
    }

    // Fetches the data from the preferences if there is none, then create default
    private List<connectionSettings> initData(Pair<Integer, Integer> myPair) {


        return myConnectionSingleton.getInstance().loadFromPreferences(getContext(), myPair);
    }

    // If true set tvConnectionStatus to green else set to red
    private void updateConnectionStatus() {
        if (connectionSuccessful) {
            tvConnectionStatus.setTextColor(Color.GREEN);
        } else {
            tvConnectionStatus.setTextColor(Color.RED);
        }
    }

    private void tryConnection(Context context) {
        Context[] myTaskParams = {context, null, null};
        //This has do be done in a AsyncTask as to not block the main thread.
        pingTask myTask = new pingTask();
        myTask.execute(myTaskParams);
        // WHEN THE TASKS FINISHES
    }

    private void pingFunction(Context context) throws UnsupportedEncodingException {
        //Define our response handler
        TextHttpResponseHandler response = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                connectionSuccessful = false;
                Log.d("ConnectionTest", connectionSuccessful.toString());
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                connectionSuccessful = true;
                Log.d("ConnectionTest", connectionSuccessful.toString());
            }
        };
        DatabaseRestClient.post(context, getPingURL(), createPingEntity(), "application/x-www-form-urlencoded", response);
    }

    private HttpEntity createPingEntity() throws UnsupportedEncodingException {
        StringEntity myEntitiy = new StringEntity("");
        myEntitiy.setContentEncoding("UTF-8");
        myEntitiy.setContentType("application/json");
        return myEntitiy;
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

    private class pingTask extends AsyncTask<Context, Void, Void> {

        //TODO:Loading

        @Override
        protected Void doInBackground(Context... params) {
            try {
                pingFunction(params[0]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateConnectionStatus();
        }
    }

    private class swapRecyclerLists extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            if (params[0].equals(0))
                swapLeft();
            if (params[0].equals(1))
                swapRight();
            myConnectionSetting = initData(pairList.get(indexOfListOnScreen));
            reloadRecyclerView();
            Log.d("SwapTask", "Swapping lists");
            return null;
        }
    }
}
