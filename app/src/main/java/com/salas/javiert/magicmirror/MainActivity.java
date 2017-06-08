package com.salas.javiert.magicmirror;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.salas.javiert.magicmirror.Fragments.AddFragment;
import com.salas.javiert.magicmirror.Fragments.AssignmentsFragment;
import com.salas.javiert.magicmirror.Fragments.UpdateFragment;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private Button mButton;
    private InsertTask myTask;
    private Toolbar mToolbar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.bTest);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTask = new InsertTask();
                myTask.execute();
            }
        });


        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                //Checking if the item is in checked state or not, if not make it in checked state
                item.setChecked(!item.isChecked());

                //Closing drawer on item click
                mDrawerLayout.closeDrawers();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();


                // Handle action buttons
                switch (item.getItemId()) {
                    case R.id.nav_Assignments:
                        Toast.makeText(getApplicationContext(), "Assignments Selected", Toast.LENGTH_SHORT).show();
                        AssignmentsFragment view_fragment = new AssignmentsFragment();
                        fragmentTransaction.replace(R.id.frame, view_fragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.nav_Add:
                        Toast.makeText(getApplicationContext(), "Add Selected", Toast.LENGTH_SHORT).show();
                        AddFragment add_fragment = new AddFragment();
                        fragmentTransaction.replace(R.id.frame, add_fragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.nav_Update:
                        Toast.makeText(getApplicationContext(), "Update Selected", Toast.LENGTH_SHORT).show();
                        UpdateFragment update_fragment = new UpdateFragment();
                        fragmentTransaction.replace(R.id.frame, update_fragment);
                        fragmentTransaction.commit();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;
                }

                //This will never run
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

    public void InsertAssignment() {
        RequestParams params = new RequestParams();
        TextHttpResponseHandler response = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.d("suc", responseString);
            }
        };
        params.add("name1", "Test");

        DatabaseRestClient.post("input.php", params, response);
        Log.d("responses", "");
    }

    private class InsertTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
        }


        @Override
        protected Void doInBackground(Void... params) {
            InsertAssignment();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }
}

