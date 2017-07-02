package com.salas.javiert.magicmirror.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.salas.javiert.magicmirror.DatabaseRestClient;
import com.salas.javiert.magicmirror.Objects.assignment_class;
import com.salas.javiert.magicmirror.Objects.class_class;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Adapters.mExpandListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Admin [http://www.android4devs.com/2015/06/navigation-view-material-design-support.html] on 04-06-2015 [5/26/2017] .
 */
public class AssignmentsFragment extends Fragment {

    JSONArray jArray_assignments, jArray_classes;
    class_class[] bigarray = new class_class[6];
    ExpandableListView mExpandView;
    List<String> ListStirngOfClasses;
    Map<String, List<assignment_class>> myMap;
    ExpandableListAdapter mListAdapter;
    FetchAssignments myTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_fragment_asignments, container, false);
        mExpandView = (ExpandableListView) view.findViewById(R.id.ExpandableView);
        myTask = new FetchAssignments();
        myTask.execute();


        return view;
    }

    @Override
    public void onDetach() {
        //If we a no longer viewing this fragment we should cancel the AsyncTask
        // myTask connects to the database and fetches and populates the RecyclerView
        if (myTask != null && myTask.getStatus() == AsyncTask.Status.RUNNING) {
            myTask.cancel(true);
        }
        Log.d("Fragments", "AssignmentFragment has been closed. Canceling AsyncTask()");
        super.onDetach();
    }

    private void PopulatemExpandView() {
        Log.d("Async", "On postExec, populating ExpandView");
        mListAdapter = new mExpandListAdapter(getContext(), ListStirngOfClasses, myMap);
        mExpandView.setAdapter(mListAdapter);
        mExpandView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getContext(), (ListStirngOfClasses.get(groupPosition) + " : " + myMap.get(ListStirngOfClasses.get(groupPosition)).get(childPosition)), Toast.LENGTH_LONG).show();

                return false;
            }
        });

    }

    private void FillmyMapUsingListStringAndBigArray() {
        Log.d("Async", "On postExec, filling myMap using List<String> and BigArray");


        for (int i = 0; i < bigarray.length; i++) {
            myMap.put(ListStirngOfClasses.get(bigarray[i].getClassID()), bigarray[i].getList());
            Log.d("myMapPut", ListStirngOfClasses.get(bigarray[i].getClassID()) + "-" + bigarray[i].getList().toString());
        }

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
                    populateList();
                    // Do something with the response
                    Log.d("Query", "Successfully loaded " + (jArray_classes.length()) + " indexes from classlist.php");

                }


                private void populateList() {
                    try {
                        for (int i = 0; i < jArray_classes.length(); i++) {
                            JSONObject jObject = jArray_classes.getJSONObject(i);
                            ListStirngOfClasses.add(jObject.getString("class_name"));
                            Log.d("ExpList", jObject.getString("class_name") + " inserted into List<string>");
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
                startUpBigArray();
                populateBigArray();
                // Do something with the response
                Log.d("Query", "Successfully loaded " + (jArray_assignments.length()) + " indexes from outputassignment.php");

            }

            private void startUpBigArray() {
                for (int i = 0; i < bigarray.length; i++) {
                    bigarray[i] = new class_class(i);
                }
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

    private class FetchAssignments extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            ListStirngOfClasses = new ArrayList<>();
            myMap = new HashMap<>();
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
            FillmyMapUsingListStringAndBigArray();
            PopulatemExpandView();
        }
    }
}
