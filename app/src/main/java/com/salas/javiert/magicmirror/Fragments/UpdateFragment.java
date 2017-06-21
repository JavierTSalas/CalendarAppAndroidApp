package com.salas.javiert.magicmirror.Fragments;

import android.os.AsyncTask;
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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.salas.javiert.magicmirror.DatabaseRestClient;
import com.salas.javiert.magicmirror.Objects.assignment_class;
import com.salas.javiert.magicmirror.Objects.class_class;
import com.salas.javiert.magicmirror.Objects.myQueue;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Adapters.myExpandRecylerAdapter;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.ParentViewClass;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.TitleCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Admin [http://www.android4devs.com/2015/06/navigation-view-material-design-support.html] on 04-06-2015 [5/26/2017] .
 */
public class UpdateFragment extends Fragment {

    RecyclerView mRecyclerView;
    JSONArray jArray_assignments, jArray_classes;
    class_class[] bigarray = new class_class[6];
    UpdateFragment.FetchAssignments myTask;
    List<assignment_class> ClassList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_layout, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        myTask = new UpdateFragment.FetchAssignments();
        myTask.execute();


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((myExpandRecylerAdapter) mRecyclerView.getAdapter()).onSaveInstanceState(outState);

    }

    private void updateQueueCount() {

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);

        // get menu from navigationView
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem nav_camara = menu.findItem(R.id.nav_Queue);

        // set new title to the MenuItem
        nav_camara.setTitle("Queue(" + myQueue.getInstance().getTaskCount() + ")");

    }

    @Override
    public void onDetach() {
        //If we a no longer viewing this fragment we should cancel the AsyncTask
        // myTask connects to the database and fetches and populates the RecyclerView
        if (myTask != null && myTask.getStatus() == AsyncTask.Status.RUNNING) {
            myTask.cancel(true);
        }
        updateQueueCount();
        Log.d("Fragments", "UpdateFragment has been closed. Canceling AsyncTask()");
        super.onDetach();
    }

    private void PopulateRecyclerView() {
        Log.d("Async", "On postExec, populating Recyclerview");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        // Prepare the adapter
        myExpandRecylerAdapter adapter = new myExpandRecylerAdapter(getContext(), initData());
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        Log.d("UF", adapter.toString());


        mRecyclerView.swapAdapter(adapter, true);

        // Set the adapter
        mRecyclerView.setAdapter(adapter);
    }

    private List<ParentObject> initData() {
        // Get the titleCreator but we need to pass it a List<String> for it to create parents of
        TitleCreator titleCreator = TitleCreator.get(getContext(), ClassList);

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

            // Create the childList that will hold the children of each Parent (The assignment name and due date)
            List<Object> childList = new ArrayList<>();
            childList.addAll(bigarray[i].getList());
            title.setChildObjectList(childList);

            parentObject.add(title);
        }

        //We finished populating the parents so we can return the list
        return parentObject;
    }

    private void FillListStringOfClasses() throws JSONException {
        {
            DatabaseRestClient.get("classlist.php", null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try {
                        jArray_classes = new JSONArray(response.getString("Classes"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    populateClassListFromJSON();
                    // Connection successful, writing in log for debugging purposes
                    Log.d("Query", "Successfully loaded " + (jArray_classes.length()) + " indexes from classlist.php");

                }


                private void populateClassListFromJSON() {
                    try {
                        for (int i = 0; i < jArray_classes.length(); i++) {
                            JSONObject jObject = jArray_classes.getJSONObject(i);
                            ClassList.add(new assignment_class(jObject.getString("class_name"), jObject.getInt("class_id")));
                            Log.d("ClassList", jObject.getString("class_name") + " inserted into List<>");
                        }
                    } catch (JSONException e) {
                        Log.d("ERR", e.toString());
                    }
                }


                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                    // Pull out the first event on the public timeline
                    // If the response is JSONObject instead of expected JSONArray

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("CONNECTION", "onFailure: ");
                }
            });
        }

    }

    public void FillBigArrayFromInternet() throws JSONException {
        DatabaseRestClient.get("outputassignment.php", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    jArray_assignments = new JSONArray(response.getString("Assignments"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                populateBigArray();
                // Do something with the response
                Log.d("Query", "Successfully loaded " + (jArray_assignments.length()) + " indexes from outputassignment.php");

            }


            private void populateBigArray() {
                try {
                    for (int i = 0; i < jArray_assignments.length(); i++) {
                        JSONObject jObject = jArray_assignments.getJSONObject(i);


                        assignment_class assignment = new assignment_class(jObject.getInt("ass_id"), jObject.getString("ass_name"), jObject.getInt("class_id"), jObject.getString("date_assigned"), jObject.getString("due"), jObject.getString("done"), jObject.getInt("weight"));

                        Log.d("Fetched", jObject.getString("ass_name"));

                        InsertIntoBigArray(assignment.class_id, assignment);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // This functions insets a assignment_class into it's proper class_class which has a List<assignment_class> that have the same class_id as index number in BigArray
            // class_id = 0 => bigArray[0];
            private void InsertIntoBigArray(int indexForBigArray, assignment_class assignment) {
                bigarray[indexForBigArray].Append(assignment);
                Log.d("BigArray", "[" + indexForBigArray + "]=" + assignment.ass_name);
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                // If the response is JSONObject instead of expected JSONArray

            }
        });
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
    Initialize ClassList to populate
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
            ClassList = new ArrayList<>();
            startUpBigArray();
        }


        @Override
        protected Void doInBackground(Void... params) {


            try {
                FillBigArrayFromInternet();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                FillListStringOfClasses();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            PopulateRecyclerView();
        }
    }


}
