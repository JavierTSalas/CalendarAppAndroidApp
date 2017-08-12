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
    private static boolean isCalendarFragmentAttached = true;

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


        // Lay the fragment on top of our other fragment
        // testing

        // Get the fragment that is inflated
        openOuterFragmentHideInnerFragment(bundledFragment);

    }

    private void openOuterFragmentHideInnerFragment(final NewAssignmentFragment bundledFragment) {

        new AsyncTask<Void, Void, Void>() {
            FragmentManager fragmentManager = getSupportFragmentManager();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Open OuterFragment
                fragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.outerFrame, bundledFragment, NEW_ASSIGNMENT_FRAGMENT_TAG)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // Pause InnerFragment
                if (isCalendarFragmentAttached) {
                    // Reset the transition
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag(CALENDAR_FRAGMENT_TAG))
                            .commit();
                    isCalendarFragmentAttached = false;
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
                // Resume InnerFragment
                if (!isCalendarFragmentAttached) {
                    fragmentManager.beginTransaction()
                            .show(fragmentManager.findFragmentByTag(CALENDAR_FRAGMENT_TAG))
                            .commit();
                    isCalendarFragmentAttached = true;
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                // Remove OuterFragment
                fragmentManager.beginTransaction().
                        remove(fragmentManager.findFragmentByTag(NEW_ASSIGNMENT_FRAGMENT_TAG))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
                return null;
            }
        }.execute();

    }

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

        }
        Log.d("interface", "User complete");
    }
}
