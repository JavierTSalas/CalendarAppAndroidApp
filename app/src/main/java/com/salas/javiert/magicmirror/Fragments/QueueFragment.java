package com.salas.javiert.magicmirror.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.salas.javiert.magicmirror.Objects.class_class;
import com.salas.javiert.magicmirror.Objects.myQueue;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Adapters.RecylerAdapter;
import com.salas.javiert.magicmirror.Resources.Adapters.myExpandRecylerAdapter;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.ParentViewClass;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.TitleCreator;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javi6 on 6/12/2017.
 */

public class QueueFragment extends Fragment {

    RecyclerView mRecyclerView;
    JSONArray jArray_assignments, jArray_classes;
    class_class[] bigarray = new class_class[6];
    QueueFragment.FetchAssignments myTask;
    List<String> ListOfTitleStrings;
    private RecylerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_layout, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
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
    public void onDetach() {
        //If we a no longer viewing this fragment we should cancel the AsyncTask
        // myTask connects to the database and fetches and populates the RecyclerView
        if (myTask != null && myTask.getStatus() == AsyncTask.Status.RUNNING) {
            myTask.cancel(true);
        }
        Log.d("Fragments", "UpdateFragment has been closed. Canceling AsyncTask()");
        super.onDetach();
    }

    private void PopulateRecyclerView() {
        Log.d("Async", "On postExec, populating Recyclerview");
        adapter = new RecylerAdapter(getActivity(), myQueue.getInstance().getList());
        mRecyclerView.setAdapter(adapter);
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

            // Create the childList that will hold the children of each Parent (The asssignment occur_name and due date)
            List<Object> childList = new ArrayList<>();
            try {
                childList.addAll(myQueue.getInstance().getList().get(i).getObjectList());
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
                ListOfTitleStrings.add(myQueue.getInstance().getList().get(i).toString());
                Log.d("ListOfTitleStrings", myQueue.getInstance().getList().get(i).toString() + " inserted into List<>");
            }
        }

    }

    private void startUpBigArray() {
        // Set up bigarray so that we don't get nullpointerexceptions
        // The benefit of doing this is that we already have the assignments grouped by class_id
        for (int i = 0; i < bigarray.length; i++) {
            bigarray[i] = new class_class(i);
        }
    }

    /* This AsyncTask does all of the work
    It's lifecycle is as follows:
    ---------onPreExecute---------
    Initialize ListOfTitleStrings to populate
    Call startUpBigArray() to populate it with class_class
    ---------doInBackground---------
    Calls FillBigArrayFormInternet - See function
    Calls FillListStringOfClasses - See function
    ---------onPostExecute---------
    Calls PopulateRecyclerView do all the work for the RecyclerView
    */
    private class FetchAssignments extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            ListOfTitleStrings = new ArrayList<>();
            startUpBigArray();
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
