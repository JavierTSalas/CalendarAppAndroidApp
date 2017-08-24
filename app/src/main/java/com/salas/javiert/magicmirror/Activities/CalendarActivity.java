/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;
import com.salas.javiert.magicmirror.Fragments.NewAssignmentFragment;
import com.salas.javiert.magicmirror.Objects.bindableObjects.bindableAssignment;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Adapters.calendarFragmentReyclerAdapter;
import com.salas.javiert.magicmirror.Resources.Room.assignments.savedAssignments.Entities.savedAssignment;
import com.salas.javiert.magicmirror.Resources.Room.assignments.savedAssignments.savedAssignmentDataBaseCreator;
import com.salas.javiert.magicmirror.Resources.Room.assignments.savedEvent.Entities.savedEvent;
import com.salas.javiert.magicmirror.Resources.Room.assignments.savedEvent.savedEventDatabaseCreator;
import com.salas.javiert.magicmirror.Views.BottomNavigationViewHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by javi6 on 8/3/2017.
 */

public class CalendarActivity extends AppCompatActivity implements NewAssignmentFragment.newAssignmentFragmentListener {

    private static final String CALENDAR_FRAGMENT_TAG = "CALENDAR";
    private static final String NEW_ASSIGNMENT_FRAGMENT_TAG = "NEW";
    private static final String TAG = "MainActivity";
    private static boolean isCalendarFragmentVisible = true; // Double checking never hurts right?
    private List<Event> eventsToBeSaved = new ArrayList<>();
    private int runningCountOfAssignments;
    private CompactCalendarView compactCalendarView;
    private RecyclerView mRecyclerView;
    private generatingTask generatingTask;
    private calendarFragmentReyclerAdapter mAdapter;
    private PopupMenu pum;
    private Date dateSeleceted;

    private void getCountOfAssignments() {
        new AsyncTask<Void, Void, Void>() {
            final savedAssignmentDataBaseCreator creator = savedAssignmentDataBaseCreator.getInstance(getApplicationContext());

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (creator.isDatabaseCreated().getValue().equals(Boolean.FALSE)) {
                    creator.createDb(getApplicationContext());
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                runningCountOfAssignments = (creator.getDatabase().savedAssignmentDao().getAll().size());
                return null;
            }
        }.execute();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fragment_calendar);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tbCalendarFragment);
        setSupportActionBar(mToolbar);

        // Find runningCountOfAssignments
        getCountOfAssignments();
        setupBottomNavigationBar();
        setUpReyclerView();
        populateCalendarWithEventsFromRoom();
        // Setup? Really should have added more comments
        generateEventList();


        // Set the default dateSelected so we don't get errors if the users doesn't click on another date and we need to use it
        dateSeleceted = new Date();
        populateRecyclerWithEventsOnDate(dateSeleceted);

        // Set the listener
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                dateSeleceted = dateClicked;
                populateRecyclerWithEventsOnDate(dateSeleceted);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                updateTitle(firstDayOfNewMonth);
                dateSeleceted = firstDayOfNewMonth;
            }
        });


    }

    private void populateRecyclerWithEventsOnDate(Date dateSeleceted) {
        List<Event> events = compactCalendarView.getEvents(dateSeleceted);
        Log.d(TAG, "Sending events " + events.size());
        mAdapter.setItems(getBindableAssignmentsFromEventList(events));

    }

    @Override
    protected void onStop() {
        super.onStop();
        saveEventsFromRoom();
    }

    private void saveEventsFromRoom() {
        new AsyncTask<Void, Void, Void>() {
            final savedAssignmentDataBaseCreator assignmentCreator = savedAssignmentDataBaseCreator.getInstance(getApplicationContext());


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (assignmentCreator.isDatabaseCreated().getValue().equals(Boolean.FALSE)) {
                    assignmentCreator.createDb(getApplicationContext());
                }

            }


            @Override
            protected Void doInBackground(Void... params) {
                List<savedEvent> savedEvents = new ArrayList<>();
                List<savedAssignment> savedAssignments = new ArrayList<>();
                for (Event e : eventsToBeSaved) {
                    // Safe typecasting
                    if (e.getData() instanceof savedAssignment) {
                        savedAssignment innerObject = (savedAssignment) e.getData();
                        // Add our savedAssignment object to a list
                        savedAssignments.add(innerObject);
                        eventsToBeSaved.remove(e);
                    }
                }
                assignmentCreator.getDatabase().savedAssignmentDao().insertAll(savedAssignments);
                return null;
            }


        }.execute();
    }

    private void generateEventList() {
        generatingTask = new generatingTask();
        generatingTask.execute();
    }

    private List<bindableAssignment> getBindableAssignmentsFromEventList(List<Event> EventList) {
        List<bindableAssignment> mList = new ArrayList<>();
        if (EventList != null || EventList.size() != 0) {
            for (Event e : EventList) {
                // Safe casting
                if (e.getData() instanceof savedAssignment) {
                    mList.add(new bindableAssignment((savedAssignment) e.getData()));
                    Log.d(TAG, "Converting event to bindableassignment");
                }

            }
        }
        return mList;
    }

    private void setUpReyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rvCalendarFragment);
        mAdapter = new calendarFragmentReyclerAdapter(new ArrayList<bindableAssignment>(), new calendarFragmentReyclerAdapter.OnClickRecyclerChild() {
            @Override
            public void editAssignment(savedAssignment savedAssignment) {
                inflateNewAssignmentFragment(new Date(savedAssignment.getAssignedTime()), savedAssignment.getClassId(), 1);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        // Define our calendar
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        // Update the title
        updateTitle(compactCalendarView.getFirstDayOfCurrentMonth());

    }

    private void updateTitle(Date firstDayOfNewMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());
        String formattedDate = sdf.format(firstDayOfNewMonth);
        getSupportActionBar().setTitle(formattedDate);
    }

    private void setupBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);

        // Disable the icons for rising when selected
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // Bind the buttons on the bottom of the screen
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()) {
                    case R.id.ic_cal:
                        Intent calendarIntent = new Intent(getApplicationContext(), CalendarActivity.class);
                        // So we don't open this more than once
                        calendarIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        Log.d(TAG, "Prevent user fomr reopening activity");
                        break;
                    case R.id.ic_list:
                        // TODO Open new activites
                        break;
                    case R.id.ic_settings:
                        // TODO Open new activites

                        break;
                }

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendar_fragment_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Called when a menu option is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Define our popup
        pum = new PopupMenu(this, findViewById(R.id.calendar_fragment_add));
        pum.inflate(R.menu.calendar_add_chooser_popup);
        pum.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                           @Override
                                           public boolean onMenuItemClick(MenuItem item) {
                                               switch (item.getItemId()) {
                                                   case R.id.action_add_new_assignment:
                                                       // Hack to immediately hide the popup
                                                       // I tried to use a loop for this but it didnt work for whatever reason
                                                       pum.getMenu().close();
                                                       // we use count + 1 as a possible new index in our table
                                                       inflateNewAssignmentFragment(dateSeleceted, runningCountOfAssignments + 1, 0);
                                                       return true;
                                                   case R.id.action_add_new_break:
                                                       break;
                                                   case R.id.action_add_new_class:
                                                       break;
                                                   case R.id.action_add_new_reminder:
                                                       break;
                                               }
                                               return false;
                                           }
                                       }
        );

        switch (item.getItemId()) {
            case R.id.calendar_fragment_today:
                Toast.makeText(this, "Reset Calendar", Toast.LENGTH_SHORT)
                        .show();

                // If the user wants to go back to today
                // TODO animate this
                compactCalendarView.setCurrentDate(new Date());
                // Update the title
                updateTitle(new Date());

                break;
            case R.id.calendar_fragment_add:
                // Show our popup
                pum.show();
                break;

            default:
                break;
        }

        // Return unhandled event
        return super.onOptionsItemSelected(item);
    }

    private void populateCalendarWithEventsFromRoom() {
        // Creators used in the task below
        final savedEventDatabaseCreator savedEventDBCreator = savedEventDatabaseCreator.getInstance(this);
        final savedAssignmentDataBaseCreator savedAssignmentDBCreator = savedAssignmentDataBaseCreator.getInstance(this);

        new AsyncTask<Void, Void, Void>() {

            List<Event> myEvents = new ArrayList<>();

            @Override
            protected void onPreExecute() {
                if (savedAssignmentDBCreator.isDatabaseCreated().getValue() == Boolean.FALSE)
                    savedAssignmentDBCreator.createDb(getApplicationContext());
                if (savedEventDBCreator.isDatabaseCreated().getValue() == Boolean.FALSE)
                    savedEventDBCreator.createDb(getApplicationContext());
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

    public void makeEventFromAssignment(final savedAssignment savedAssignment) {
        // TODO get color code for a class
        int color = Color.GREEN;
        compactCalendarView.addEvent(new Event(color, savedAssignment.getDueTime(), savedAssignment), true);

        new AsyncTask<Void, Void, Void>() {
            final savedAssignmentDataBaseCreator creator = savedAssignmentDataBaseCreator.getInstance(getApplicationContext());

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (creator.isDatabaseCreated().getValue().equals(Boolean.FALSE)) {
                    creator.createDb(getApplicationContext());
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                creator.getDatabase().savedAssignmentDao().insert(savedAssignment);
                return null;
            }
        }.execute();
    }

    public void inflateNewAssignmentFragment(Date calendarStartDate, Integer index, int mode) {

        NewAssignmentFragment bundledFragment = createBundledFragmentFromArguments(calendarStartDate, index, mode);

        FragmentManager fragmentManager = getSupportFragmentManager();
        // Inflate fragment
        fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.outerFrame, bundledFragment, NEW_ASSIGNMENT_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();


    }

    private void fragmentPop() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Then remove the outerFrame. We remove it instead of hiding it since
        // we don't want to keep it in memory anymore
        fragmentManager.beginTransaction()
                .remove(fragmentManager.findFragmentByTag(NEW_ASSIGNMENT_FRAGMENT_TAG))
                .setTransition(FragmentTransaction.TRANSIT_EXIT_MASK)
                .commit();

    }

    // Creates a NewAssignmentFragment with a bundle of a Date and savedAssignment GSON formatted strings
    // Date is used to set elements to a desired date (mainly today or a selected date on the calendar)
    // savedAssignment is used to create a new bindableAssignment(savedAssignment) to be bound to the layout
    // Integer mode = savedInstanceState.getInt(MODE_KEY);
    //    case 0:
    //        currentMode = MODES.EDIT;
    //        break;
    //    case 1:
    //        currentMode = MODES.NEW;
    //        break;
    private NewAssignmentFragment createBundledFragmentFromArguments(Date dateSeleceted, Integer index, Integer mode) {
        // Pass the object as a gson string to NewAssignmentFragment
        // TODO make a bundle to do this but for prototyping this should be fine


        Log.d("Inflater", "inflating");

        NewAssignmentFragment newAssignmentFragment = new NewAssignmentFragment();


        Bundle args = new Bundle();
        args.putString(NewAssignmentFragment.DATE_KEY, new Gson().toJson(dateSeleceted, Date.class));
        args.putInt(NewAssignmentFragment.COLUMN_INDEX_KEY, index);
        args.putInt(NewAssignmentFragment.MODE_KEY, mode);

        // Set our args
        Log.d("Inflater", "seting Arguments");
        newAssignmentFragment.setArguments(args);


        return newAssignmentFragment;
    }

    // NewAssignmentFragment Interface
    @Override
    public void onUserDismiss(boolean shouldIncrement) {
        if (shouldIncrement) {
            runningCountOfAssignments++;
        }
        fragmentPop();
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

    // Snackbar to tell the user that we've processed the data
    public void onUserComplete(savedAssignment savedAssignment, int mode) {
        if (mode == 0) {
            makeEventFromAssignment(savedAssignment);
            Snackbar.make(findViewById(android.R.id.content), "Assignment added", Snackbar.LENGTH_SHORT).show();
        } else if (mode == 1) {
            updateAssignmentInRoom(savedAssignment);
            Snackbar.make(findViewById(android.R.id.content), "Your edits have been saved", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void updateAssignmentInRoom(final savedAssignment savedAssignment) {
        List<Event> eventsForDay = compactCalendarView.getEvents(savedAssignment.getDueTime());

        // Get livedata savedAssignment
        // have livedata boundableassigment observer the livedata
        // update recyclerview
        new AsyncTask<Void, Void, Void>() {
            final savedAssignmentDataBaseCreator creator = savedAssignmentDataBaseCreator.getInstance(getApplicationContext());

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (creator.isDatabaseCreated().getValue().equals(Boolean.FALSE)) {
                    creator.createDb(getApplicationContext());
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                creator.getDatabase().savedAssignmentDao().updateAssignment(savedAssignment);
                return null;
            }
        }.execute();
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