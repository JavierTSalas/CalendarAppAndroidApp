/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.salas.javiert.magicmirror.Fragments.CalendarFragment;
import com.salas.javiert.magicmirror.Fragments.ConnectionFragment;
import com.salas.javiert.magicmirror.Fragments.DatabaseFragment;
import com.salas.javiert.magicmirror.Fragments.DoneCheckFragment;
import com.salas.javiert.magicmirror.Fragments.MultiCheckActivity;
import com.salas.javiert.magicmirror.Fragments.NewAssignmentFragment;
import com.salas.javiert.magicmirror.Fragments.QueueFragment;
import com.salas.javiert.magicmirror.Fragments.UpdateFragment;
import com.salas.javiert.magicmirror.Objects.SingletonObjects.myQueueClasses.myQueue;
import com.salas.javiert.magicmirror.R;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);
        updateQueueCount();
        myQueue.getInstance().loadMyQueue(getApplicationContext());


        navigationView = (NavigationView) findViewById(R.id.navigation_view);


        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                // Checking if the item is in checked state or not, if not make it in checked state
                item.setChecked(!item.isChecked());

                // Closing drawer on item click
                mDrawerLayout.closeDrawers();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();


                // Handle action buttons
                switch (item.getItemId()) {
                    case R.id.nav_Assignments:
                        Toast.makeText(getApplicationContext(), "Calendar Selected", Toast.LENGTH_SHORT).show();
                        CalendarFragment view_fragment = new CalendarFragment();
                        fragmentTransaction.replace(R.id.frame, view_fragment, CalendarFragment.FRAGMENT_TAG);
                        fragmentTransaction.commit();
                        break;
                    case R.id.nav_TODOCheck:
                        Toast.makeText(getApplicationContext(), "TODOCheck Selected", Toast.LENGTH_SHORT).show();
                        DoneCheckFragment todoCheck = new DoneCheckFragment();
                        fragmentTransaction.replace(R.id.frame, todoCheck);
                        fragmentTransaction.commit();
                        break;
                    case R.id.nav_Update:
                        Toast.makeText(getApplicationContext(), "Update Selected", Toast.LENGTH_SHORT).show();
                        UpdateFragment update_fragment = new UpdateFragment();
                        fragmentTransaction.replace(R.id.frame, update_fragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.nav_Queue:
                        Toast.makeText(getApplicationContext(), "Queue Selected", Toast.LENGTH_SHORT).show();
                        QueueFragment queue_fragment = new QueueFragment();
                        fragmentTransaction.replace(R.id.frame, queue_fragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.nav_Check:
                        Toast.makeText(getApplicationContext(), "Check Selected", Toast.LENGTH_SHORT).show();
                        MultiCheckActivity check_fragment = new MultiCheckActivity();
                        fragmentTransaction.replace(R.id.frame, check_fragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.nav_Conn:
                        Toast.makeText(getApplicationContext(), "Check Selected", Toast.LENGTH_SHORT).show();
                        ConnectionFragment ConnectionFragment = new ConnectionFragment();
                        fragmentTransaction.replace(R.id.frame, ConnectionFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.nav_Database:
                        Toast.makeText(getApplicationContext(), "Check Selected", Toast.LENGTH_SHORT).show();
                        DatabaseFragment DatabaseFragment = new DatabaseFragment();
                        fragmentTransaction.replace(R.id.frame, DatabaseFragment);
                        fragmentTransaction.commit();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;
                }


                return false;
            }
        });


        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        // Disable drawer turning when pulling the Drawer open form the side of the screen
        mToggle.setDrawerSlideAnimationEnabled(false);


        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // method invoked only when the actionBar is not null.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    // Toggle the navigation drawer
    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            mToggle.setDrawerIndicatorEnabled(true);
            mToggle.syncState();

        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mToggle.setDrawerIndicatorEnabled(false);
            mToggle.syncState();
        }
    }

    // Update our item title
    private void updateQueueCount() {

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        // get menu from navigationView
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem nav_camara = menu.findItem(R.id.nav_Queue);

        // set new title to the MenuItem
        nav_camara.setTitle("Queue(" + myQueue.getInstance().getTaskCount() + ")");

    }


    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        // If there is stuff in our fragmentManager then we are probably looking at a fragment
        if (count > 0) {
            Fragment fragment = getSupportFragmentManager().getFragments().get(count);
            if (fragment instanceof NewAssignmentFragment) {
                ((NewAssignmentFragment) fragment).showDiscardDialog();
            }
        } else {
            //  super.onBackPressed();
        }


    }

}

