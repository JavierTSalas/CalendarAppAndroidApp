/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;
import com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton.connectionSettings;
import com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton.myConnectionSingleton;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Adapters.RecyclerAdapter;
import com.salas.javiert.magicmirror.Resources.DatabaseRestClient;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by javi6 on 7/2/2017.
 */

public class ConnectionFragment extends Fragment {
    ImageView ivCloud;
    TextView tvConnectionStatus;
    Boolean connectionSuccessful = false; // TODO: Save this

    public String getPingURL() {
        final String pingURL = "outputtodo.php"; //TODO: This
        return pingURL;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_connection, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.settingsRecyclerView);
        List<connectionSettings> myConnectionSetting = initData();
        RecyclerAdapter mAdpater = new RecyclerAdapter(view.getContext(), myConnectionSetting);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerView.setAdapter(mAdpater);

        //Required for adding buttons to the ToolBar
        setHasOptionsMenu(true);

        // The 'status'
        tvConnectionStatus = (TextView) view.findViewById(R.id.tvConnectedToServer);

        // The iamge view
        ivCloud = (ImageView) view.findViewById(R.id.ivConnection);
        ivCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tryConnection(getContext());
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
                Toast.makeText(this.getContext(), "Action load selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.action_connection_save:
                Toast.makeText(this.getContext(), "Action save selected", Toast.LENGTH_SHORT)
                        .show();
                break;

            default:
                break;
        }

        return true;
    }


    // Fetches the data from the preferences if there is none, then create default
    private List<connectionSettings> initData() {
        myConnectionSingleton.getInstance().loadFromPreferences(getContext());
        return myConnectionSingleton.getInstance().getmyList();
    }

    // If true set tvConnectionStatus to green else set to red
    private void updateConnectionStatus() {
        if (connectionSuccessful) {
            tvConnectionStatus.setTextColor(Color.GREEN);
        } else {
            tvConnectionStatus.setTextColor(Color.RED);
        }
    }

    private void tryConnection(Context context) {
        Context[] myTaskParams = {context, null, null};
        //This has do be done in a AsyncTask as to not block the main thread.
        pingTask myTask = new pingTask();
        myTask.execute(myTaskParams);
        // WHEN THE TASKS FINISHES
    }

    private void pingFunction(Context context) throws UnsupportedEncodingException {
        //Define our response handler
        TextHttpResponseHandler response = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                connectionSuccessful = false;
                Log.d("ConnectionTest", connectionSuccessful.toString());
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                connectionSuccessful = true;
                Log.d("ConnectionTest", connectionSuccessful.toString());
            }
        };
        DatabaseRestClient.post(context, getPingURL(), createPingEntity(), "application/x-www-form-urlencoded", response);
    }

    private HttpEntity createPingEntity() throws UnsupportedEncodingException {
        StringEntity myEntitiy = new StringEntity("");
        myEntitiy.setContentEncoding("UTF-8");
        myEntitiy.setContentType("application/json");
        return myEntitiy;
    }

    public enum FETCH { //TODO: To get one particular string
        HOST, PORT, ASSIGN_INTER, TODO_INTER, OCCUR_INTER, CT_INTER, CLASS_INTER
    }

    private class pingTask extends AsyncTask<Context, Void, Void> {

        //TODO:Loading

        @Override
        protected Void doInBackground(Context... params) {
            try {
                pingFunction(params[0]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateConnectionStatus();
        }
    }
}
