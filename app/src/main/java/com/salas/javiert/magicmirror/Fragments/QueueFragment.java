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

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.salas.javiert.magicmirror.Objects.myQueueClasses.myQueue;
import com.salas.javiert.magicmirror.Objects.myQueueClasses.myQueueTask;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Adapters.myExpandRecylerAdapter;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.ParentViewClass;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.TitleCreator;

import java.util.ArrayList;
import java.util.List;

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
        View view = inflater.inflate(R.layout.queue_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView_Queue);
        myTask = new QueueFragment.FetchAssignments();
        myTask.execute();


        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((myExpandRecylerAdapter) mRecyclerView.getAdapter()).onSaveInstanceState(outState);

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        // Prepare the adapter
        myExpandRecylerAdapter adapter = new myExpandRecylerAdapter(getContext(), initData());
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        // Set the adapter
        mRecyclerView.setAdapter(adapter);
    }

    private List<ParentObject> initData() {
        // Get the titleCreator but we need to pass it a List<String> for it to create parents of
        TitleCreator titleCreator = TitleCreator.get(getContext(), ListOfTitleStrings);

        // Get the List of ParentViewClasses
        List<ParentViewClass> titles = titleCreator.getAll();

        // Create the List<parentObject> that will be returned
        List<ParentObject> parentObject = new ArrayList<>();

        // Initalize outside so we can use it more than once
        ParentViewClass title;

        // For each ParentView
        for (int i = 0; i < titles.size(); i++) {
            // We need to populate these ParentViewClasses one at a time so lets work on the i index
            title = titles.get(i);

            // Create the childList that will hold the children of each Parent (The assignment occur_name and due date)
            List<Object> childList = new ArrayList<>();
            try {
                childList.addAll(myQueue.getInstance().getList().get(i).getMyTaskList());
                Log.d("childList", "myQueue.getInstance().getList().get(i).getMyTaskList() " + "of size " + myQueue.getInstance().getList().get(i).getObjectList().size() + " inserted into childList");
                Log.d("childList", myQueue.getInstance().getList().get(i).getMyTaskList().get(0).getClass().toString());
            } catch (IndexOutOfBoundsException e) {
                Log.d("QueueFragment", "Nothing in queue, catching " + e.toString());
            }
            title.setChildObjectList(childList);

            parentObject.add(title);
        }

        //We finished populating the parents so we can return the list
        return parentObject;
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
