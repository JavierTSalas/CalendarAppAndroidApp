/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Fragments;

import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.salas.javiert.magicmirror.Objects.helperObjects.assignment_class;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Room.assignments.savedAssignments.Entities.savedAssignment;
import com.salas.javiert.magicmirror.Resources.Room.assignments.savedAssignments.savedAssignmentDataBaseCreator;
import com.salas.javiert.magicmirror.Resources.Room.assignments.savedEvent.Entities.savedEvent;
import com.salas.javiert.magicmirror.Resources.Room.assignments.savedEvent.savedEventDatabaseCreator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by javi6 on 8/3/2017.
 */

public class CalendarFragment extends Fragment {
    private CompactCalendarView compactCalendarView;
    private Date dateSeleceted;
    private generatingTask generatingTask;
    private TextView calendarTitle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Required for adding buttons to the ToolBar
        setHasOptionsMenu(true);


        final View view = inflater.inflate(R.layout.layout_fragment_calendar, container, false);
        compactCalendarView = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);


        calendarTitle = ((TextView) view.findViewById(R.id.tvCalendarTitle));

        generateEventList();

        updateTitle(compactCalendarView.getFirstDayOfCurrentMonth());

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                dateSeleceted = dateClicked;
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                // Update the adapter with this
                // FIXME: 8/4/2017
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                updateTitle(firstDayOfNewMonth);
            }
        });


        view.findViewById(R.id.tvFakeButtonPlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddDialog();
            }
        });

        view.findViewById(R.id.tvFakeButtonCircle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the date to today
                compactCalendarView.setCurrentDate(new Date());
                // Update the title
                updateTitle(new Date());
            }
        });

        return view;
    }

    private void createAddDialog() {

    }

    private void updateTitle(Date firstDayOfNewMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());
        String formattedDate = sdf.format(firstDayOfNewMonth);
        calendarTitle.setText(formattedDate);
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
                Toast.makeText(this.getContext(), "Reset Calendar", Toast.LENGTH_SHORT)
                        .show();
                populateCal();
                break;
            case R.id.action_connection_save:
                Toast.makeText(this.getContext(), "Add new Event ", Toast.LENGTH_SHORT)
                        .show();
                addNewEvent();
                break;

            default:
                break;
        }

        return true;
    }


    @Override
    public void onDetach() {
        //If we a no longer viewing this fragment we should cancel the AsyncTask
        // generatingTask populates the calendar
        if (generatingTask != null && generatingTask.getStatus() == AsyncTask.Status.RUNNING) {
            generatingTask.cancel(true);
        }
        Log.d("Fragments", "CalendarFragment has been closed. Canceling AsyncTask()");
        super.onDetach();
    }

    private void addNewEvent() {
        // Create dialog
        // Add to room
        // Add to RecyclerView
        // Add to calendar
    }

    private void populateCal() {
        // Creators used in the task below
        final savedEventDatabaseCreator savedEventDBCreator = savedEventDatabaseCreator.getInstance(getContext());
        final savedAssignmentDataBaseCreator savedAssignmentDBCreator = savedAssignmentDataBaseCreator.getInstance(getContext());

        new AsyncTask<Void, Void, Void>() {

            List<Event> myEvents = new ArrayList<>();

            @Override
            protected void onPreExecute() {
                if (savedAssignmentDBCreator.isDatabaseCreated().getValue() == Boolean.FALSE)
                    savedAssignmentDBCreator.createDb(getContext());
                if (savedEventDBCreator.isDatabaseCreated().getValue() == Boolean.FALSE)
                    savedEventDBCreator.createDb(getContext());
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // Get the list of assignment that the user has created
                List<savedAssignment> assignmentList = savedAssignmentDBCreator.getDatabase().savedAssignmentDao().getAll();
                // Associate the assignments to a unique ID
                // Put in a map so we can find it when needed
                SparseArray<savedAssignment> assignmentMap = new SparseArray<savedAssignment>();
                // Make the map of IDs
                for (savedAssignment assignment : assignmentList) {
                    assignmentMap.put(assignment.getId(), assignment);
                }

                // Get a list of the events that the user has created
                List<savedEvent> eventList = savedEventDBCreator.getDatabase().savedEventDao().getAll();
                for (savedEvent savedEvent : eventList) {

                    // Find the corresponding assignment
                    savedAssignment assignment = assignmentMap.get(savedEvent.getId());
                    // Add it to the list if we haven't completed it
                    if (!assignment.isCompleted())
                        myEvents.add(new Event(savedEvent.getColor(), assignment.getDueTime(), assignment));
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // Add all of events that we fetched
                compactCalendarView.addEvents(myEvents);
                // Update the calendar
                compactCalendarView.invalidate();
            }
        }.execute();
    }

    private void generateEventList() {
        generatingTask = new generatingTask();
        generatingTask.execute();
    }

    public Event makeEventFromAssignment(assignment_class assignment) {
        // TODO get color code for a class
        int color = Color.GREEN;
        return new Event(color, assignment.due.getTime(), assignment);
    }

    private class generatingTask extends AsyncTask<Void, Void, Void> {
        List<Event> myEvents = new ArrayList<Event>();

        @Override
        protected Void doInBackground(Void... params) {
            //myEvents = null;  TODO Get event from Google Cal or Room Database
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            compactCalendarView.addEvents(myEvents);
            compactCalendarView.invalidate();
            Toast.makeText(compactCalendarView.getContext(), "Done", Toast.LENGTH_SHORT).show();

        }
    }
}
