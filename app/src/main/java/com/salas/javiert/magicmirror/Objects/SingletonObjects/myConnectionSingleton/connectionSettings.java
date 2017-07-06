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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.TextHttpResponseHandler;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.DatabaseRestClient;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by javi6 on 7/3/2017.
 */

public class connectionSettings {

    private String title, subtext;
    private Boolean connectionSuccessful;
    private ProgressBar pbLoading;
    private ImageView ivConnectionStatus, ivLock;
    private TextView titleTextView;
    private EditText subTextView;

    public connectionSettings(String title, String subtext) {
        this.title = title;
        this.subtext = subtext;
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

    // called from RecyclerAdapter to allow us to work on a ProgressBar
    public void setProgessBarView(ProgressBar settings_loading) {
        pbLoading = settings_loading;
        pbLoading.setVisibility(View.VISIBLE);
        // Set up the view
        determineSpinning(true);
        Log.d("progressBar", "true");
    }

    public void setConnectionSuccessful(Boolean connectionSuccessful) {
        this.connectionSuccessful = connectionSuccessful;
    }

    public void setIvConnectionStatus(ImageView ivConnectionStatus) {
        this.ivConnectionStatus = ivConnectionStatus;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
        title = titleTextView.getText().toString();
    }

    public void setSubTextView(EditText subTextView) {
        this.subTextView = subTextView;
        subtext = subTextView.getText().toString();
    }

    // called from RecyclerAdapter to tries to establish a connection with the server
    public void tryConnection(Context context) {
        if (!connectionSuccessful && !(title.contains("Host") || title.contains("Port"))) {
            Context[] myTaskParams = {context, null, null};
            //This has do be done in a AsyncTask as to not block the main thread.
            pingTask myTask = new pingTask();
            myTask.execute(myTaskParams);
        }
    }

    // Does the work for ProgressBar and the imageview behind it
    private void determineSpinning(boolean spinning) {
        // If we want it to spin
        if (spinning) {
            ivConnectionStatus.setVisibility(View.GONE);
            pbLoading.setVisibility(View.VISIBLE);
        } else {
            pbLoading.setVisibility(View.GONE);
            ivConnectionStatus.setVisibility(View.VISIBLE);
            if (connectionSuccessful) {
                ivConnectionStatus.setImageResource(R.drawable.icons8_checkmark_16);
                lockIfConnectionSuccessful();
            } else {
                ivConnectionStatus.setImageResource(R.drawable.icons8_delete_16);
            }
        }
        Log.d("determineSpinning", String.valueOf(spinning));
    }

    // Create the httpEntity for the post message
    private HttpEntity createPingEntity() throws UnsupportedEncodingException {
        StringEntity myEntitiy = new StringEntity("");
        myEntitiy.setContentEncoding("UTF-8");
        myEntitiy.setContentType("application/json");
        return myEntitiy;
    }

    // Function that does the connection
    private void pingFunction(Context context, String pingURL) throws UnsupportedEncodingException {
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
        DatabaseRestClient.post(context, pingURL, createPingEntity(), "application/x-www-form-urlencoded", response);
    }

    private void lockIfConnectionSuccessful() {
        if (connectionSuccessful) {
            subTextView.setInputType(InputType.TYPE_NULL);
            subTextView.setTextColor(Color.GRAY);
            ivLock.setImageResource(R.drawable.icons8_lock_16);
        }
    }

    public void toggleLockEditText() {
        if (subTextView.getInputType() == InputType.TYPE_NULL) {
            subTextView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            ivLock.setImageResource(R.drawable.icons8_unlock_16);
            subTextView.setTextColor(Color.BLACK);
        } else {
            subTextView.setInputType(InputType.TYPE_NULL);
            subTextView.setTextColor(Color.GRAY);
            ivLock.setImageResource(R.drawable.icons8_lock_16);
        }
    }

    public void setIvLock(ImageView ivLock) {
        this.ivLock = ivLock;
    }

    //Task that makes the connection
    private class pingTask extends AsyncTask<Context, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            determineSpinning(true);
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
            determineSpinning(false);
        }
    }

}
