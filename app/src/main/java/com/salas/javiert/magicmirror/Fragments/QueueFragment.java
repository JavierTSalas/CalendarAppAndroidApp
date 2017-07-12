package com.salas.javiert.magicmirror.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.salas.javiert.magicmirror.Objects.SingletonObjects_REMOVE_ME.myQueueClasses.myQueue;
import com.salas.javiert.magicmirror.Objects.SingletonObjects_REMOVE_ME.myQueueClasses.myQueueTask;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Views.ExpandableRecyclerView.DependentViews.ExpandGroup;
import com.salas.javiert.magicmirror.Views.ExpandableRecyclerView.myExpandRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

//import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

/**
 * Created by javi6 on 6/12/2017.
 */

public class QueueFragment extends Fragment {

    RecyclerView mRecyclerView;
    QueueFragment.FetchAssignments myTask;
    ArrayList<myQueueTask> ListOfTitleStrings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_queue, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView_Queue);
        myTask = new QueueFragment.FetchAssignments();
        myTask.execute();


        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((myExpandRecyclerAdapter) mRecyclerView.getAdapter()).onSaveInstanceState(outState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //TODO: Save the stuff that was just added in the queue,


        //Load form Preferences
        myQueue.getInstance().loadMyQueue(this.getContext());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {


            //Load form Preferences
            myQueue.getInstance().loadMyQueue(this.getContext());
        }
    }

    @Override
    public void onDetach() {
        //If we a no longer viewing this fragment we should cancel the AsyncTask
        // myTask is an AsyncTask that connects to the database and fetches the sendToServerObject that populates the RecyclerView
        if (myTask != null && myTask.getStatus() == AsyncTask.Status.RUNNING) {
            myTask.cancel(true);
        }
        mRecyclerView.destroyDrawingCache();
        Log.d("Fragments", "QueueFragment has been closed. Canceling AsyncTask()");
        updateQueueNavCount();
        myQueue.getInstance().saveMyQueue(this.getContext());
        super.onDetach();
    }

    private void updateQueueNavCount() {

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);

        // get menu from navigationView
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem navQueue = menu.findItem(R.id.nav_Queue);

        // set new title to the MenuItem
        int count = 0;


        try {
            count = myQueue.getInstance().getTaskCount();
        } catch (NullPointerException e) {
            Log.d("updateQueueCount", "No Tasks in queue, catching:" + e.toString());
        }
        navQueue.setTitle("Queue(" + count + ")");
    }

    private void PopulateRecyclerView() {
        Log.d("Async", "On postExec, populating Recyclerview");
        // Set the LayoutManager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        // Prepare the adapter
        myExpandRecyclerAdapter adapter = new myExpandRecyclerAdapter(initData());

        // Set the adapter
        mRecyclerView.setAdapter(adapter);
    }

    private List<ExpandGroup> initData() {
        // Create the List<parentObjects> that will be returned
        List<ExpandGroup> parentObjects = new ArrayList<>();

        // Initialize outside so we can use it more than once
        ExpandGroup mExpand;

        try {
            // For each ParentView
            for (int i = 0; i < myQueue.getInstance().getList().size(); i++) {
                // Create a ExpandGroup that will hold the children of each Parent (The assignment occur_name and due date)
                // ExpandGroup takes a String for the Title and a List<? extends ExpandableGroup>
                String Title = myQueue.getInstance().getList().get(i).toString();
                List<?> myChildList = myQueue.getInstance().getList().get(i).getMyTaskList();
                mExpand = new ExpandGroup(Title, myChildList);
                Log.d("childList", "myQueue.getInstance().getList().get(i).getMyTaskList() " + "of size " + myQueue.getInstance().getList().get(i).getObjectList().size() + " inserted into mExpand");
                Log.d("childList", myQueue.getInstance().getList().get(i).getMyTaskList().get(0).getClass().toString());

                parentObjects.add(mExpand);
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("QueueFragment", "Nothing in queue, catching " + e.toString());
        }
        //We finished populating the parents so we can return the list
        return parentObjects;
    }

    private void FillListStringOfClasses() {
        {
            for (int i = 0; i < myQueue.getInstance().getList().size(); i++) {
                ListOfTitleStrings.add(myQueue.getInstance().getList().get(i));
                Log.d("ListOfTitleStrings", myQueue.getInstance().getList().get(i).toString() + " inserted into List<> (" + i + ") out of " + myQueue.getInstance().getList().size());
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_add:
                // User chose the Add button
                // as a favorite...
                createAddDialog();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void createAddDialog() {
    }

    /* This AsyncTask does all of the work
    It's lifecycle is as follows:
    ---------onPreExecute---------
    Initialize ListOfTitleStrings to populate
    ---------doInBackground---------
    Calls FillListStringOfClasses - See function
    ---------onPostExecute---------
    Calls PopulateRecyclerView do all the work for the RecyclerView
    */
    private class FetchAssignments extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            ListOfTitleStrings = new ArrayList<>();

        }


        @Override
        protected Void doInBackground(Void... params) {


            FillListStringOfClasses();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            PopulateRecyclerView();
        }
    }


}
