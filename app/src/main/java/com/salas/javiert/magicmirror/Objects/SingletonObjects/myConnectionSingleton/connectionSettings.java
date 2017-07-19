/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.TextHttpResponseHandler;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Adapters.RecyclerAdapter;
import com.salas.javiert.magicmirror.Resources.DatabaseRestClient;
import com.salas.javiert.magicmirror.Resources.Room.connection.Entities.connectionDataBaseItem;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by javi6 on 7/3/2017.
 */

public class connectionSettings {

    private static Boolean isRestored = false; // One time use
    private String title, subtext;
    private Boolean connectionSuccessful, isLocked = false;
    private ProgressBar pbLoading;
    private ImageView ivConnectionStatus, ivLock;
    private TextView tvTitle;
    private EditText etSubTextView;
    private Integer id;

    public connectionSettings(String title, String subtext, Boolean connectionSuccessful, Boolean lockStatus) {
        this.title = title;
        this.subtext = subtext;
        this.connectionSuccessful = connectionSuccessful;
        this.isLocked = lockStatus;
    }

    public connectionSettings(connectionDataBaseItem conn) {
        this.title = conn.getName();
        this.subtext = conn.getSubtitle();
        this.connectionSuccessful = conn.getConnectionSuccessful();
        this.isLocked = conn.getLockStatus();
        this.id = conn.getId();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getConnectionSuccessful() {
        return connectionSuccessful;
    }

    public void setConnectionSuccessful(Boolean connectionSuccessful) {
        this.connectionSuccessful = connectionSuccessful;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    public boolean getisLocked() {
        return isLocked;
    }

    // called from RecyclerAdapter to tries to establish a connection with the server
    public void tryConnection(Context context) {
        restoreState();
        if (!connectionSuccessful && !isServerAddressField()) {
            Context[] myTaskParams = {context, null, null};
            //This has do be done in a AsyncTask as to not block the main thread.
            pingTask myTask = new pingTask();
            myTask.execute(myTaskParams);
        }
    }

    public boolean isServerAddressField() {
        return (title.equals("Host:Port") || title.equals("Directory"));
    }

    public boolean isSameAs(connectionSettings foo) {
        Boolean conditionSameTitle = foo.getTitle().equals(this.getTitle());
        Boolean conditionSameSubText = foo.getSubtext().equals(this.getSubtext());
        Boolean conditionConnectionsStatusMatch = foo.getConnectionSuccessful() == this.getConnectionSuccessful();
        Boolean conditionLocksMatch = foo.getisLocked() == this.getisLocked();
        return conditionSameTitle &&
                conditionSameSubText &&
                conditionConnectionsStatusMatch &&
                conditionLocksMatch;
    }

    // This restores the 'lock' and the Check or X on the view
    public void restoreState() {
        if (!isRestored) {
            isLoading(false);
            Log.d("Restoring", toString());
        }
    }

    @Override
    public String toString() {
        return "connectionSettings{" +
                "title='" + title + '\'' +
                ", subtext='" + subtext + '\'' +
                ", connectionSuccessful=" + connectionSuccessful +
                ", getisLocked=" + isLocked +
                '}';
    }

    // Does the work for ProgressBar and the imageview behind it
    private void isLoading(boolean spinning) {
        // If we want it to spin
        if (spinning) {
            ivConnectionStatus.setVisibility(View.GONE);
            pbLoading.setVisibility(View.VISIBLE);
        } else {
            pbLoading.setVisibility(View.GONE);
            ivConnectionStatus.setVisibility(View.VISIBLE);
            updateConnectionStauts();

        }
    }

    private void updateConnectionStauts() {
        // Set the checkmark or the X
        if (connectionSuccessful) {
            ivConnectionStatus.setImageResource(R.drawable.icons8_checkmark_16);
            setLockedState(true); // Lock the user input
        } else {
            ivConnectionStatus.setImageResource(R.drawable.icons8_delete_16);
        }
    }

    // Create the httpEntity for the post message
    private HttpEntity createPingEntity() throws UnsupportedEncodingException {
        StringEntity myEntitiy = new StringEntity("");
        myEntitiy.setContentEncoding("UTF-8");
        myEntitiy.setContentType("application/json");
        return myEntitiy;
    }

    // Function that does the connection
    private void pingFunction(final Context context, String pingURL) throws UnsupportedEncodingException {
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
                myConnectionSingleton.getInstance().saveURLStatus(context, true);
                Log.d("ConnectionTest", connectionSuccessful.toString());
            }
        };
        DatabaseRestClient.post(context, pingURL, createPingEntity(), "application/x-www-form-urlencoded", response);
    }

    // Set the lock state
    // Set the inputType to null to prevent the user to enter text
    // Set the drawable to a locked lock / unlocked lock
    // Set the text color
    // TODO: Find a better color and a better way to lock this field
    private void setLockedState(boolean lockTheView) {
        if (lockTheView) {
            etSubTextView.setInputType(InputType.TYPE_NULL);
            etSubTextView.setTextColor(Color.GRAY);
            ivLock.setImageResource(R.drawable.icons8_lock_16);
            isLocked = true;
        } else {
            etSubTextView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            etSubTextView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            etSubTextView.setTextColor(Color.BLACK);
            ivLock.setImageResource(R.drawable.icons8_unlock_16);
            isLocked = false;
        }
    }

    // Toggle the 'lock'
    public void toggleLockEditText() {
        if (isLocked) {
            setLockedState(false);
        } else {
            setLockedState(true);
        }
    }

    // Called from RecyclerView to give us the views we need
    public void loadFromHolder(RecyclerAdapter.MyViewHolder holder) {
        tvTitle = holder.getSettings_object_title();
        pbLoading = holder.getSettings_loading();
        etSubTextView = holder.getSettings_object_subtitle();
        ivConnectionStatus = holder.getSettings_connection_status();
        ivLock = holder.getSettings_lock();
    }

    //Task that makes the connection
    private class pingTask extends AsyncTask<Context, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading(true);
        }

        @Override
        protected Void doInBackground(Context... params) {
            try {
                Thread.sleep(2000); // Sleep to create illusion of loading
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                pingFunction(params[0], subtext); // Try to establish a connection
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isLoading(false);
        }
    }

}
