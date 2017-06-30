/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.salas.javiert.magicmirror.Objects.myQueueClasses.myQueue;
import com.salas.javiert.magicmirror.Objects.myQueueClasses.myQueueItem;
import com.salas.javiert.magicmirror.Objects.myQueueClasses.myQueueTask;
import com.salas.javiert.magicmirror.Objects.myQueueClasses.mySendQueue;
import com.salas.javiert.magicmirror.Objects.sendToServerObject;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.CheckExpandableRecyclerView.DependentViews.myMultiCheckExpandGroup;
import com.salas.javiert.magicmirror.Resources.CheckExpandableRecyclerView.myCheckableChildRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.listeners.OnCheckChildClickListener;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javi6 on 6/29/2017.
 */

public class DoneCheckFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<myMultiCheckExpandGroup> myDataList;

    private myCheckableChildRecyclerViewAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment, container, false);
        //Required for adding buttons to the ToolBar
        setHasOptionsMenu(true);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView_Queue);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        myDataList = initData();
        adapter = new myCheckableChildRecyclerViewAdapter(myDataList);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        adapter.setChildClickListener(new OnCheckChildClickListener() {
            @Override
            public void onCheckChildCLick(View v, boolean checked, CheckedExpandableGroup group, int childIndex) {
                if (checked)
                    mySendQueue.getInstance().addQueueItem((myQueueItem) group.getItems().get(childIndex));
                if (!checked)
                    mySendQueue.getInstance().removeQueueItem((myQueueItem) group.getItems().get(childIndex));
            }
        });

        return view;
    }

    //Send the data
    private void SendAll() {
        List<myQueueTask> tasksToRemove = new ArrayList<>();
        for (int myQueueTaskCount = 0; myQueueTaskCount < mySendQueue.getInstance().getList().size(); myQueueTaskCount++) {
            //Send a new sendToServerObject for each myQueueList
            new sendToServerObject(mySendQueue.getInstance().getList().get(myQueueTaskCount)).send(getContext());
            tasksToRemove.add(mySendQueue.getInstance().getList().get(myQueueTaskCount));
        }

        //Remove the data that we just sent
        myQueue.getInstance().removeQueueTask(tasksToRemove);
        mySendQueue.getInstance().removeQueueTask(tasksToRemove);

        adapter.clearChoices();



    /*    //Reset the List
        myCheckableChildRecyclerViewAdapter mAdapter = new myCheckableChildRecyclerViewAdapter(initData());
        mRecyclerView.swapAdapter(mAdapter, true);
*/

    }

    private List<myMultiCheckExpandGroup> initData() {
        // Create the List<parentObjects> that will be returned
        List<myMultiCheckExpandGroup> parentObjects = new ArrayList<>();

        // Initialize outside so we can use it more than once
        myMultiCheckExpandGroup mExpand;

        // For each ParentView
        for (int i = 0; i < myQueue.getInstance().getList().size(); i++) {
            // Create a ExpandGroup that will hold the children of each Parent (The assignment occur_name and due date)
            // ExpandGroup takes a String for the Title and a List<? extends ExpandableGroup>
            String title = myQueue.getInstance().getList().toString();
            List<?> myList = myQueue.getInstance().getList().get(i).getMyTaskList();

            mExpand = new myMultiCheckExpandGroup(title, myList);

            parentObjects.add(mExpand);
        }

        //We finished populating the parents so we can return the list
        return parentObjects;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_send, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //Called when a menu option is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                //If the checkboxes are visible then send the data
                if (adapter.isCheckBoxVisible()) {
                    SendAll();
                    //Then we hide the checkboxes
                    adapter.setVisible(false);
                    adapter.notifyDataSetChanged();

                    //If we aren't showing it then we don't want to send anything
                    //We also want to clear the choices the user set while scrolling
                } else {
                    adapter.clearChoices();
                    adapter.setVisible(true);
                    adapter.notifyDataSetChanged();
                }
                break;

            default:
                break;
        }

        return true;
    }

}
