/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.salas.javiert.magicmirror.Objects.SingletonObjects.myTimeSensorClasses.classTimeSensor;
import com.salas.javiert.magicmirror.Objects.bindableObjects.bindableAssignment;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Room.assignments.savedAssignments.Entities.savedAssignment;
import com.salas.javiert.magicmirror.Resources.Util.FileDataUtil;
import com.salas.javiert.magicmirror.databinding.DialogNewAssignmentEventBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * https://stackoverflow.com/questions/31606871/how-to-achieve-a-full-screen-dialog-as-described-in-material-guidelines
 * <p>
 * Created by javi6 on 8/9/2017.
 */

public class NewAssignmentFragment extends DialogFragment {

    public final static String ITEM_KEY = "myBindableAssignment";
    public final static String DATE_KEY = "myDate";
    public final static String TAG = "NewAssignmentFragment";

    DialogNewAssignmentEventBinding dataBiding;
    bindableAssignment item = new bindableAssignment();
    Date dateSeleceted = new Date();
    newAssignmentFragmentListener mCallback;
    private long extraTime;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.dialog_new_assignment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Toast.makeText(getContext(), id, Toast.LENGTH_LONG).show();

        if (id == R.id.action_save) {
            // handle confirmation button click here
            mCallback.onUserComplete(generateAssignment());
            mCallback.onUserDismiss();
            return true;
        } else if (id == android.R.id.home) {
            // handle close button click here
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Discard this assignment?");
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mCallback.onUserDismiss();
                }
            });
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (newAssignmentFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBiding = DataBindingUtil.inflate(inflater, R.layout.dialog_new_assignment_event, container, false);

        // Set our title
        dataBiding.tbDialogNewAssignmentToolbar.setTitle("New Assignment");

        ((AppCompatActivity) getActivity()).setSupportActionBar(dataBiding.tbDialogNewAssignmentToolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }

        setHasOptionsMenu(true);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Get our data from the bundle
                getDataFromBundle(getArguments());
            }

            @Override
            protected Void doInBackground(Void... params) {

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // Bind our views
                bindViews();
            }
        }.execute();

        return dataBiding.getRoot();
    }

    public void getDataFromBundle(Bundle savedInstanceState) {


        Log.d("Bundle", "Fetching items");

        // Try to get the dateSelected that was sent as a string
        String dateJson = savedInstanceState.getString(DATE_KEY, "");
        //  If the string is not "" , try to get our date for the view
        if (!dateJson.equals("")) {
            Gson gson = new Gson();
            dateSeleceted = gson.fromJson(dateJson, Date.class);
            Log.d(DATE_KEY, "Loaded from bundle" + dateJson);
            Log.d(DATE_KEY, "Loaded from bundle" + dateSeleceted.toString());
        }

        // Try to get the bindableAssignment that was sent as a string
        String savedAssignmentJSON = savedInstanceState.getString(ITEM_KEY, "");
        //  If the string is not "" , try to get our bindableAssignment for the view
        if (!savedAssignmentJSON.equals("")) {
            Gson gson = new Gson();
            item = new bindableAssignment(gson.fromJson(savedAssignmentJSON, savedAssignment.class));
            Log.d(ITEM_KEY, "Loaded from bundle" + savedAssignmentJSON);
            Log.d(ITEM_KEY, "Loaded from bundle" + item.toString());
        }
        dataBiding.setData(item);
    }

    private void bindViews() {
        // Create a Calendar object with the date from the dateSelected on the calendar
        final java.util.Calendar calendarFromDate = java.util.Calendar.getInstance();
        calendarFromDate.setTimeInMillis(dateSeleceted.getTime());


        // Get the users default Date format
        String modifiedDate = FileDataUtil.getModifiedDate(Locale.getDefault(), dateSeleceted.getTime());
        // Set default text to the selected date
        dataBiding.etDialogNewAssignmentDate.setText(modifiedDate);


        // date listener for the user to pick the date
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                java.util.Calendar selectedCalendar = java.util.Calendar.getInstance();
                // Assign the date that the user just picked
                selectedCalendar.set(year, monthOfYear, dayOfMonth);
                // Set the DueTime in our object
                dataBiding.getData().setDueTime(selectedCalendar.getTimeInMillis());
                // Get the users default Date format
                String modifiedDate = FileDataUtil.getModifiedDate(Locale.getDefault(), dateSeleceted.getTime());
                // Update the text
                dataBiding.etDialogNewAssignmentDate.setText(modifiedDate);

            }

        };

        // When the user clicks the date field
        dataBiding.etDialogNewAssignmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the arguments of calendarFromDate() to set the date on the picker to the selcted date on the calendar
                new DatePickerDialog(getContext(), dateSetListener, calendarFromDate.get(Calendar.YEAR), calendarFromDate.get(Calendar.MONTH), calendarFromDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // When the user clicks the time field
        dataBiding.etDialogNewAssignmentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // Set the time format to 00:00
                        // If it works it isn't stupid
                        if (selectedHour < 10) {
                            dataBiding.etDialogNewAssignmentTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else if (selectedMinute < 10) {
                            dataBiding.etDialogNewAssignmentTime.setText(selectedHour + ":" + "0" + selectedMinute);
                        } else if (selectedHour < 10 && selectedMinute < 10) {
                            dataBiding.etDialogNewAssignmentTime.setText("0" + selectedHour + "0" + selectedMinute);
                        } else {
                            dataBiding.etDialogNewAssignmentTime.setText(selectedHour + ":" + selectedMinute);
                        }
                        extraTime = selectedHour * 60 * 60 * 1000 + selectedMinute * 60 * 1000;
                    }
                }, java.util.Calendar.HOUR_OF_DAY, java.util.Calendar.MINUTE, false).show();
            }
        });

        // Our class spinner
        // Get classes from the user
        // If the user hasn't set any classes, show a notifcation at the button with a intent to add classes
        // Show default classes


        // Add our classes to the list
        ArrayList<String> mList = new ArrayList<>();


        // If we see that the user is in class right now,
        // (Get off your phone! You're paying for this class!)
        // Set the spinner for them
        // Update the list of classes
        // TODO Set a option to display both
        // put this in a toggle in the Toolbar of adding classes
        classTimeSensor.getInstance().refreshFromRoom();
        // Set our list
        mList = classTimeSensor.getInstance().getStringList();

        ArrayAdapter<String> userEnteredDataAdapter = null;
        ArrayAdapter<CharSequence> defaultListAdapter = null;

        // If the user hasn't set up a list
        if (mList.size() == 0) {
            // Make the arrayAdapter from the default class list
            defaultListAdapter = ArrayAdapter.createFromResource(getContext(), R.array.class_list_default, android.R.layout.simple_spinner_item);
            // Set the adapter
            dataBiding.spDialogNewAssignmentClassSelector.setAdapter(defaultListAdapter);
        } else {
            // Make the arrayAdapter from items that the user created
            userEnteredDataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, mList);
            // Set the adapter
            dataBiding.spDialogNewAssignmentClassSelector.setAdapter(userEnteredDataAdapter);

        }
        dataBiding.getData();

        // Set the class id when it is selected in our object
        // TODO figure out how to handle default classes
        dataBiding.spDialogNewAssignmentClassSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataBiding.getData().setClassId(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

/*

        // When the user clicks the cancel button
        dataBiding.ivDialogNewAssignmentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onUserDismiss();
            }
        });

        // When the user clicks the done button
        dataBiding.bDialogNewAssignmentDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onUserComplete(generateAssignment());
                mCallback.onUserDismiss();
            }
        });
*/
    }

    private savedAssignment generateAssignment() {
        bindableAssignment boundData = dataBiding.getData();
        // Set the time in ms
        boundData.setDueTime(dateSeleceted.getTime() + extraTime);
        // Set the completion boolean
        boundData.setCompleted(false);

        // TODO Create a reminder oject
        // TODO Create a reoccuring field
        return new savedAssignment(boundData);
    }

    // Container Activity must implement this interface
    public interface newAssignmentFragmentListener {
        void onUserDismiss();

        void onUserComplete(savedAssignment savedAssignment);

    }
}
