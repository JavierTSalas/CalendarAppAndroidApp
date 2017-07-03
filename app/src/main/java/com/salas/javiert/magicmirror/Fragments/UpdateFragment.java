package com.salas.javiert.magicmirror.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.salas.javiert.magicmirror.DatabaseRestClient;
import com.salas.javiert.magicmirror.Objects.assignment_class;
import com.salas.javiert.magicmirror.Objects.class_class;
import com.salas.javiert.magicmirror.Objects.myQueueClasses.myQueue;
import com.salas.javiert.magicmirror.Objects.myQueueClasses.myQueueItem;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.ExpandableRecyclerView.DependentViews.ExpandGroup;
import com.salas.javiert.magicmirror.Resources.ExpandableRecyclerView.myExpandRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Admin [http://www.android4devs.com/2015/06/navigation-view-material-design-support.html] on 04-06-2015 [5/26/2017] .
 */
public class UpdateFragment extends Fragment {

    static DatePickerDialog.OnDateSetListener date;
    static Dialog dialog;
    static boolean DoneFlag = false;
    private static Calendar myCalendar;
    RecyclerView mRecyclerView;
    JSONArray jArray_assignments, jArray_classes;
    class_class[] bigarray = new class_class[6];
    UpdateFragment.FetchAssignments myTask;
    List<assignment_class> ClassList;

    public static void createAddDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addassignment);
        dialog.show();


        // Set the textView at the top to the ass_name to indicate which assignment is being modified

        //Initialize the Spinner
        final Spinner DropDown = (Spinner) dialog.findViewById(R.id.spDropDown);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(context, R.array.class_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        DropDown.setAdapter(staticAdapter);

        DropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Initialize for DatePicker
        myCalendar = Calendar.getInstance();

        // Adapter
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //Set textview
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                ((TextView) dialog.findViewById(R.id.tvDate)).setText(sdf.format(myCalendar.getTime()));
            }

        };


        final TextView etDate = (TextView) dialog.findViewById(R.id.tvDate);


        // Default to today
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        ((TextView) dialog.findViewById(R.id.tvDate)).setText(sdf.format(myCalendar.getTime()));


        // OnClick will open the dialogue for DatePicker
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(dialog.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final TextView etTime = (TextView) dialog.findViewById(R.id.etTime);
        //Default time
        etTime.setText("00:00");


        // OnClick will open the dialogue for TimePicker
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(dialog.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // Set the time format to 00:00
                        //If it works it isn't stupid
                        if (selectedHour < 10) {
                            etTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else if (selectedMinute < 10) {
                            etTime.setText(selectedHour + ":" + "0" + selectedMinute);
                        } else if (selectedHour < 10 && selectedMinute < 10) {
                            etTime.setText("0" + selectedHour + "0" + selectedMinute);
                        } else {
                            etTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        final Button Done = (Button) dialog.findViewById(R.id.bDone);

        Done.setText("NOT DONE");
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DoneFlag) {
                    Done.setText("DONE");
                    DoneFlag = true;
                } else {
                    Done.setText("NOT DONE");
                    DoneFlag = true;
                }
            }
        });

        Button Cancel = (Button) dialog.findViewById(R.id.bCancel);
        Button Confirm = (Button) dialog.findViewById(R.id.bConfirm);
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int ass_id = 0; // We actually want null so that mySQL announcements
                String ass_name = ((EditText) dialog.findViewById(R.id.etName)).getText().toString();
                int class_id = DropDown.getSelectedItemPosition();


                // We have to convert etDate to Date
                String due = etDate.getText() + " " + etTime.getText() + ":00";


                SimpleDateFormat assigned_date_format = new SimpleDateFormat("yyyy-MM-dd");
                String date_assigned = assigned_date_format.format(Calendar.getInstance().getTime());


                Boolean done = (((Button) dialog.findViewById(R.id.bDone)).getText() == "DONE");
                int weight = Integer.valueOf(((EditText) dialog.findViewById(R.id.etWeight)).getText().toString());


                myQueueItem item = null;
                try {
                    Log.d("date_assigned", date_assigned.toString());
                    Log.d("due", due.toString());
                    assignment_class newAssign = new assignment_class(null, ass_name, class_id, date_assigned, due, done.toString(), weight);
                    item = new myQueueItem(newAssign);
                    Log.d("NewItem", newAssign.toString());
                    //TODO:set the date_assigned to now
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                item.setMode("ADD");

                //Add the item to our queue
                myQueue.getInstance().addQueueItem(item);
                dialog.dismiss();

                //Save the queue that we just created
                myQueue.getInstance().saveMyQueue(dialog.getContext());


            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_update, container, false);
        //Required for adding buttons to the ToolBar
        setHasOptionsMenu(true);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        //Start up our AsyncTask - See bottom of the file for desc of the task
        myTask = new UpdateFragment.FetchAssignments();
        myTask.execute();


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((myExpandRecyclerAdapter) mRecyclerView.getAdapter()).onSaveInstanceState(outState);

    }

    @Override
    public void onDetach() {
        // If we a no longer viewing this fragment we should cancel the AsyncTask
        // myTask connects to the database and fetches and populates the RecyclerView
        if (myTask != null && myTask.getStatus() == AsyncTask.Status.RUNNING) {
            myTask.cancel(true);
        }
        updateQueueCount();
        Log.d("Fragments", "UpdateFragment has been closed. Canceling AsyncTask()");
        super.onDetach();
    }

    //Called when a menu option is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Toast.makeText(this.getContext(), "Action selected", Toast.LENGTH_SHORT)
                        .show();
                createAddDialog(this.getContext());

                break;

            default:
                break;
        }

        return true;
    }

    private void updateQueueCount() {

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);

        // get menu from navigationView
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem nav_camara = menu.findItem(R.id.nav_Queue);

        // set new title to the MenuItem
        int count = 0;


        try {
            count = myQueue.getInstance().getTaskCount();
        } catch (NullPointerException e) {
            Log.d("updateQueueCount", "No Tasks in queue, catching:" + e.toString());
        }
        nav_camara.setTitle("Queue(" + count + ")");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Lets update this now
        updateQueueCount();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void PopulateRecyclerView() {
        Log.d("Async", "On postExec, populating Recyclerview");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        // Prepare the adapter
        myExpandRecyclerAdapter adapter = new myExpandRecyclerAdapter(initData());


        //mRecyclerView.swapAdapter(adapter, true);

        // Set the adapter
        mRecyclerView.setAdapter(adapter);
    }

    private List<ExpandGroup> initData() {
        // Create the List<parentObjects> that will be returned
        List<ExpandGroup> parentObjects = new ArrayList<>();

        // Initialize outside so we can use it more than once
        ExpandGroup mExpand;

        // For each ParentView
        for (int i = 0; i < bigarray.length; i++) {
            // Create a ExpandGroup that will hold the children of each Parent (The assignment occur_name and due date)
            // ExpandGroup takes a String for the Title and a List<? extends ExpandableGroup>
            String title = ClassList.get(i).ass_name.toString();
            List<?> myList = bigarray[i].getList();

            mExpand = new ExpandGroup(title, myList);

            parentObjects.add(mExpand);
        }

        //We finished populating the parents so we can return the list
        return parentObjects;
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
