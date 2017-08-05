/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.salas.javiert.magicmirror.Fragments.AssignmentsFragment;
import com.salas.javiert.magicmirror.Fragments.CalendarFragment;
import com.salas.javiert.magicmirror.Fragments.ConnectionFragment;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Views.BottomNavigationViewHelper;

/**
 * Created by javi6 on 8/3/2017.
 */

public class TempMainActivity extends AppCompatActivity {
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
        fragmentTransaction.replace(R.id.innerFrame, calendarFragment);
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


}
