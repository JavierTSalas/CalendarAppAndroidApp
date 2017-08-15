/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.salas.javiert.magicmirror.Fragments.AssignmentsFragment;
import com.salas.javiert.magicmirror.Fragments.CalendarFragment;
import com.salas.javiert.magicmirror.Fragments.ConnectionFragment;
import com.salas.javiert.magicmirror.Fragments.NewAssignmentFragment;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Room.assignments.savedAssignments.Entities.savedAssignment;
import com.salas.javiert.magicmirror.Views.BottomNavigationViewHelper;

import java.util.Date;

/**
 * Created by javi6 on 8/3/2017.
 */

public class TempMainActivity extends AppCompatActivity implements CalendarFragment.calendarFragmentListener, NewAssignmentFragment.newAssignmentFragmentListener {

    private static final String CALENDAR_FRAGMENT_TAG = "CALENDAR";
    private static final String NEW_ASSIGNMENT_FRAGMENT_TAG = "NEW";
    private static final String TAG = "MainActivity";
    private static boolean isCalendarFragmentVisible = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_view);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);

        // Disable the icons for rising when selected
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // Set our default fragment
        final android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        CalendarFragment calendarFragment = new CalendarFragment();
        fragmentTransaction.replace(R.id.innerFrame, calendarFragment, CALENDAR_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        // Bind the buttons on the bottom of the screen
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()) {
                    case R.id.ic_cal:
                        CalendarFragment calendarFragment = new CalendarFragment();
                        fragmentTransaction.replace(R.id.innerFrame, calendarFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.ic_list:
                        AssignmentsFragment assignmentsFragment = new AssignmentsFragment();
                        fragmentTransaction.replace(R.id.innerFrame, assignmentsFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.ic_settings:
                        ConnectionFragment settingsFragment = new ConnectionFragment(); // TODO settings fragment
                        fragmentTransaction.replace(R.id.innerFrame, settingsFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                }

                return false;
            }
        });


    }

    // CalendarFragment interface
    @Override
    public void onClickNew(Date dateSelected, savedAssignment savedAssignment) {
        //TODO
        // Open a list on the button // FAB?
        // Open the respective fragment
        NewAssignmentFragment bundledFragment = bundledFragment(dateSelected, savedAssignment);

        // Since our outerFragment (the view we want to inflate) is larger than the fragment that is already inflated
        // (The smaller frame in CalendarFragment.java) fragment. If we inflate our outerFragment we should hide the
        // other one since the user shouldn't be interacting with it.
        openOuterFragmentHideInnerFragment(bundledFragment);

    }

    private void openOuterFragmentHideInnerFragment(final NewAssignmentFragment bundledFragment) {

        new AsyncTask<Void, Void, Void>() {
            FragmentManager fragmentManager = getSupportFragmentManager();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Inflate outerFrame
                fragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.outerFrame, bundledFragment, NEW_ASSIGNMENT_FRAGMENT_TAG)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // Pause the fragment that we are covering
                if (isCalendarFragmentVisible) {
                    // Hide the fragment that we aren't seeing
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag(CALENDAR_FRAGMENT_TAG))
                            .commit();
                    isCalendarFragmentVisible = false;
                }
                return null;
            }
        }.execute();

    }

    private void closeOuterFragmentShowInnerFragment() {
        new AsyncTask<Void, Void, Void>() {
            FragmentManager fragmentManager = getSupportFragmentManager();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // We want to open the inner fragment
                if (!isCalendarFragmentVisible) {
                    fragmentManager.beginTransaction()
                            .show(fragmentManager.findFragmentByTag(CALENDAR_FRAGMENT_TAG))
                            .commit();
                    isCalendarFragmentVisible = true;
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                // Then remove the outerFrame. We remove it instead of hiding it since
                // we don't want to keep it in memory anymore
                fragmentManager.beginTransaction().
                        remove(fragmentManager.findFragmentByTag(NEW_ASSIGNMENT_FRAGMENT_TAG))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
                return null;
            }
        }.execute();

    }

    // Creates a NewAssignmentFragment with a bundle of a Date and savedAssignment GSON formatted strings
    // Date is used to set elements to a desired date (mainly today or a selected date on the calendar)
    // savedAssignment is used to create a new bindableAssignment(savedAssignment) to be bound to the layout
    private NewAssignmentFragment bundledFragment(Date dateSeleceted, savedAssignment item) {
        // Pass the object as a gson string to NewAssignmentFragment
        // TODO make a bundle to do this but for prototyping this should be fine


        Log.d("Inflater", "inflating");

        NewAssignmentFragment newAssignmentFragment = new NewAssignmentFragment();


        Bundle args = new Bundle();
        args.putString(NewAssignmentFragment.DATE_KEY, new Gson().toJson(dateSeleceted, Date.class));
        Log.d(NewAssignmentFragment.DATE_KEY, "Put in bundle" + new Gson().toJson(dateSeleceted, Date.class));
        Log.d(NewAssignmentFragment.DATE_KEY, "Put in bundle" + dateSeleceted.toString());

        args.putString(NewAssignmentFragment.ITEM_KEY, new Gson().toJson(item, savedAssignment.class));
        Log.d(NewAssignmentFragment.ITEM_KEY, "Put in bundle" + new Gson().toJson(item, savedAssignment.class));
        Log.d(NewAssignmentFragment.ITEM_KEY, "Put in bundle" + item.toString());

        // Set our args
        Log.d("Inflater", "seting Arguments");
        newAssignmentFragment.setArguments(args);


        return newAssignmentFragment;
    }

    // NewAssignmentFragment Interface
    @Override
    public void onUserDismiss() {
        closeOuterFragmentShowInnerFragment();
    }

    @Override
    public void onUserComplete(savedAssignment savedAssignment) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CALENDAR_FRAGMENT_TAG);
        // Safe type casting
        if (fragment instanceof CalendarFragment) {
            ((CalendarFragment) fragment).makeEventFromAssignment(savedAssignment);

            Log.d(TAG, "Event counter:" + ((CalendarFragment) fragment).getEventCountForLong(savedAssignment.getDueTime()));
        }
    }
}
