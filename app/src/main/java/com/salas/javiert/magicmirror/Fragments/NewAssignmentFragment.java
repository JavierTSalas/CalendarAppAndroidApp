/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Fragments;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
    public final static String MODE_KEY = "mode";
    public final static String TAG = "NewAssignmentFragment";

    DialogNewAssignmentEventBinding dataBiding;
    bindableAssignment item = new bindableAssignment();
    Date dateSeleceted = new Date();
    newAssignmentFragmentListener mCallback;
    java.util.Calendar calendarStartDate = java.util.Calendar.getInstance();
    java.util.Calendar calendarEndDate = java.util.Calendar.getInstance();
    MODES currentMode;
    private boolean changesMade = false;

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
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (currentMode == MODES.NEW) {
            if (id == R.id.action_save) {
                // handle confirmation button click here
                if (userHasCorrectFields()) {
                    mCallback.onUserComplete(generateAssignment());
                    mCallback.onUserDismiss();
                } else {
                    final AlertDialog.Builder missingFields = new AlertDialog.Builder(getContext());
                    missingFields.setTitle("Please completely fill out the required elements to save");
                    missingFields.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    missingFields.show();
                }
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
        } else if (currentMode == MODES.EDIT) {
            if (id == R.id.action_save) {
                // handle confirmation button click here
                if (userHasCorrectFields()) {
                    mCallback.onUserComplete(generateAssignment());
                    mCallback.onUserDismiss();
                } else {
                    if (changesMade) {
                        final AlertDialog.Builder changesHaveBeenMade = new AlertDialog.Builder(getContext());
                        changesHaveBeenMade.setTitle("You have made changes to the assignment. Would you like to save your edits?");
                        changesHaveBeenMade.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCallback.onUserComplete(generateAssignment());
                                mCallback.onUserDismiss();
                            }
                        });
                        changesHaveBeenMade.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCallback.onUserDismiss();
                            }
                        });
                        changesHaveBeenMade.show();
                    }
                }
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
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean userHasCorrectFields() {
        bindableAssignment data = dataBiding.getData();
        boolean userHasTitle = (data.getName() != null) && data.getName().length() > 0;
        boolean userHasStartTime = (data.getAssignedTime() > 0);
        // TODO Make sure the user cannot set an end date before the start date
        boolean userHasEndTime = (data.getDueTime() > data.getAssignedTime());
        boolean alloweUserToContinue = userHasTitle && userHasStartTime && userHasEndTime;
        Log.d(TAG, " " + userHasTitle + " " + userHasStartTime + " " + userHasEndTime + " " + alloweUserToContinue);
        return alloweUserToContinue;
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


        // Set our title
        if (currentMode == MODES.EDIT) {
            dataBiding.tbDialogNewAssignmentToolbar.setTitle("Edit assignment");
        } else if (currentMode == MODES.NEW) {
            dataBiding.tbDialogNewAssignmentToolbar.setTitle("New Assignment");
        }

        ((AppCompatActivity) getActivity()).setSupportActionBar(dataBiding.tbDialogNewAssignmentToolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        return dataBiding.getRoot();
    }

    public void getDataFromBundle(Bundle savedInstanceState) {


        Log.d("Bundle", "Fetching items");

        // Try to get the dateSelected that was sent as a string
        String dateJson = savedInstanceState.getString(DATE_KEY, "");
        //  If the string is not "" , try to get our date for the view
        if (!dateJson.equals("")) {
            dateSeleceted = new Gson().fromJson(dateJson, Date.class);
            Log.d(DATE_KEY, "Loaded from bundle" + dateJson);
            Log.d(DATE_KEY, "Loaded from bundle" + dateSeleceted.toString());
        }

        // Try to get the bindableAssignment that was sent as a string
        String savedAssignmentJSON = savedInstanceState.getString(ITEM_KEY, "");
        //  If the string is not "" , try to get our bindableAssignment for the view
        if (!savedAssignmentJSON.equals("")) {
            Gson gson = new Gson();
            // Converting form savedAssignment since the bound data is too fancy to save as a gson formatted text
            item = new bindableAssignment(gson.fromJson(savedAssignmentJSON, savedAssignment.class));
            Log.d(ITEM_KEY, "Loaded from bundle" + savedAssignmentJSON);
            Log.d(ITEM_KEY, "Loaded from bundle" + item.toString());
        }
        dataBiding.setData(item);


        Integer mode = savedInstanceState.getInt(MODE_KEY);
        switch (mode) {
            case 0:
                currentMode = MODES.EDIT;
                break;
            case 1:
                currentMode = MODES.NEW;
                break;
        }
    }

    private void bindViews() {
        // Create a Calendar object with the date from the dateSelected on the calendar
        calendarStartDate.setTimeInMillis(dateSeleceted.getTime());

        dataBiding.getData().setAssignedTime(dateSeleceted.getTime());

        // Get the users default Date format
        // Set default format to the start date fields
        dataBiding.etAssignmentDueStartDate.setText(FileDataUtil.getModifiedDate(Locale.getDefault(), dateSeleceted.getTime()));
        dataBiding.etAssignmentDueStartTime.setText(FileDataUtil.getModifiedTime(Locale.getDefault(), System.currentTimeMillis()));

        if (currentMode == MODES.EDIT) {
            dataBiding.etAssignmentDueEndDate.setText(FileDataUtil.getModifiedDate(Locale.getDefault(), dataBiding.getData().getDueTime()));
            dataBiding.etAssignmentDueEndTime.setText(FileDataUtil.getModifiedTime(Locale.getDefault(), dataBiding.getData().getDueTime()));

        }

        // date listener for the user to pick the date
        final DatePickerDialog.OnDateSetListener dateSetListenerStart = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                changesMade = true;
                // Assign the date that the user just picked
                calendarStartDate.set(year, monthOfYear, dayOfMonth);
                // Set the DueTime in our object
                dataBiding.getData().setAssignedTime(calendarStartDate.getTimeInMillis());
                // Get the users default Date format
                String modifiedDate = FileDataUtil.getModifiedDate(Locale.getDefault(), calendarStartDate.getTimeInMillis());
                // Update the text
                dataBiding.etAssignmentDueStartDate.setText(modifiedDate);
            }

        };

        // date listener for the user to pick the date
        final DatePickerDialog.OnDateSetListener dateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                changesMade = true;
                // Assign the date that the user just picked
                calendarEndDate.set(year, monthOfYear, dayOfMonth);
                // Set the DueTime in our object
                dataBiding.getData().setDueTime(calendarEndDate.getTimeInMillis());
                // Get the users default Date format
                String modifiedDate = FileDataUtil.getModifiedDate(Locale.getDefault(), calendarEndDate.getTimeInMillis());
                // Update the text
                dataBiding.etAssignmentDueEndDate.setText(modifiedDate);
                // Update our object
                dataBiding.getData().setDueTime(calendarEndDate.getTimeInMillis());

            }

        };

        // When the user clicks the date field
        dataBiding.etAssignmentDueStartDate.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                changesMade = true;
                // Pass the arguments of calendarFromDate() to set the date on the picker to the selcted date on the calendar
                //noinspection WrongConstant
                new DatePickerDialog(getContext(), dateSetListenerStart, calendarStartDate.get(Calendar.YEAR), calendarStartDate.get(Calendar.MONTH), calendarStartDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // When the user clicks the date field
        dataBiding.etAssignmentDueEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changesMade = true;
                // Pass the arguments of calendarFromDate() to set the date on the picker to the selected date on the calendar
                //noinspection WrongConstant
                new DatePickerDialog(getContext(), dateSetListenerEnd, calendarEndDate.get(Calendar.YEAR), calendarEndDate.get(Calendar.MONTH), calendarEndDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // When the user clicks the start time field
        dataBiding.etAssignmentDueStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        changesMade = true;
                        // Reset our date
                        calendarStartDate.set(Calendar.HOUR_OF_DAY, 0);
                        calendarStartDate.set(Calendar.MINUTE, 0);
                        calendarStartDate.set(Calendar.SECOND, 0);
                        calendarStartDate.set(Calendar.MILLISECOND, 0);
                        // Set it
                        calendarStartDate.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendarStartDate.set(Calendar.MINUTE, selectedMinute);
                        // Set the time format to hh:mm
                        dataBiding.etAssignmentDueStartTime.setText(FileDataUtil.getModifiedTime(Locale.getDefault(), calendarStartDate.getTimeInMillis()));
                    }
                }, java.util.Calendar.HOUR_OF_DAY, java.util.Calendar.MINUTE, false).show();
            }
        });

        // When the user clicks the end time field
        dataBiding.etAssignmentDueEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        changesMade = true;
                        // Reset our date
                        calendarEndDate.set(Calendar.HOUR_OF_DAY, 0);
                        calendarEndDate.set(Calendar.MINUTE, 0);
                        calendarEndDate.set(Calendar.SECOND, 0);
                        calendarEndDate.set(Calendar.MILLISECOND, 0);
                        // Set it
                        calendarEndDate.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendarEndDate.set(Calendar.MINUTE, selectedMinute);
                        // Set the time format to hh:mm
                        dataBiding.etAssignmentDueEndTime.setText(FileDataUtil.getModifiedTime(Locale.getDefault(), calendarEndDate.getTimeInMillis()));

                    }
                }, java.util.Calendar.HOUR_OF_DAY, java.util.Calendar.MINUTE, false).show();
            }
        });

        // Our class spinner
        // Get classes from the user
        // If the user hasn't set any classes, show a notifcation at the button with a intent to add classes
        // Show default classes


        // Add our classes to the list
        ArrayList<String> mList;


        // If we see that the user is in class right now,
        // (Get off your phone! You're paying for this class!)
        // Set the spinner for them
        // Update the list of classes
        // TODO Set a option to display both
        // put this in a toggle in the Toolbar of adding classes
        classTimeSensor.getInstance().refreshFromRoom();
        // Set our list
        mList = classTimeSensor.getInstance().getStringList();

        ArrayAdapter<String> userEnteredDataAdapter;
        ArrayAdapter<CharSequence> defaultListAdapter;

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

    }

    private savedAssignment generateAssignment() {
        bindableAssignment boundData = dataBiding.getData();
        // Set the time in ms
        boundData.setAssignedTime(calendarStartDate.getTimeInMillis());
        boundData.setDueTime(calendarEndDate.getTimeInMillis());
        // Set the completion boolean
        boundData.setCompleted(false);

        // TODO Create a reminder oject
        // TODO Create a reoccuring field
        return new savedAssignment(boundData);
    }

    private enum MODES {
        NEW, EDIT
    }

    // Container Activity must implement this interface
    public interface newAssignmentFragmentListener {
        void onUserDismiss();

        void onUserComplete(savedAssignment savedAssignment);

    }
}
