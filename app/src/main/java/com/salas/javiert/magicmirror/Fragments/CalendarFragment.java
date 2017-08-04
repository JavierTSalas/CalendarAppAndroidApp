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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.salas.javiert.magicmirror.Objects.helperObjects.assignment_class;
import com.salas.javiert.magicmirror.R;

import java.util.Date;
import java.util.List;

/**
 * Created by javi6 on 8/3/2017.
 */

public class CalendarFragment extends Fragment {
    private CompactCalendarView compactCalendarView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Required for adding buttons to the ToolBar
        setHasOptionsMenu(true);


        View view = inflater.inflate(R.layout.layout_activity_default, container, false);
        compactCalendarView = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        generateEventList();


        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                // Update the adapter with this
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });

        return view;
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


    private void addNewEvent() {
        // Create dialog
        // Add to room
        // Add to RecyclerView
        // Add to calendar
    }

    private void populateCal() {
        // Fetch stuff from room
        new AsyncTask<Void, Void, Void>() {

            List<Event> myEvents;

            @Override
            protected Void doInBackground(Void... params) {
                // Fetch the assignments if there are any
                // Room.GetEvents

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                compactCalendarView.addEvents(myEvents);
                compactCalendarView.invalidate();
            }
        }.execute();
    }


    private void generateEventList() {
        new AsyncTask<Void, Void, Void>() {
            List<Event> myEvents;

            @Override
            protected Void doInBackground(Void... params) {
                myEvents = null; // TODO Get event from Google Cal or Room Database
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                compactCalendarView.addEvents(myEvents);
            }
        }.execute();
    }

    public Event makeEventFromAssignment(assignment_class assignment) {
        // TODO get color code for a class
        int color = Color.GREEN;
        return new Event(color, assignment.due.getTime(), assignment);
    }
}
