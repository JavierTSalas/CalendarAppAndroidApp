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
import com.salas.javiert.magicmirror.Fragments.SettingsFragment;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Views.BottomNavigationViewHelper;

/**
 * Created by javi6 on 8/3/2017.
 */

public class TempMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_default);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        // Disable the icons for rising when selected
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        final android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // Set our default
        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentTransaction.replace(R.id.frame, settingsFragment);
        fragmentTransaction.commit();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {
                    case R.id.ic_cal:
                        CalendarFragment calendarFragment = new CalendarFragment();
                        fragmentTransaction.replace(R.id.frame, calendarFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.ic_list:
                        AssignmentsFragment assignmentsFragment = new AssignmentsFragment();
                        fragmentTransaction.replace(R.id.frame, assignmentsFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.ic_settings:
                        SettingsFragment settingsFragment = new SettingsFragment();
                        fragmentTransaction.replace(R.id.frame, settingsFragment);
                        fragmentTransaction.commit();
                        break;
                }
                return false;
            }
        });

    }
}
