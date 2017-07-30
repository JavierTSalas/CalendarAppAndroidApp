/*
 * Copyright (c) 2017. Javier Salas
 * Generic dataBinding for ViewHolder
 * source : https://stackoverflow.com/documentation/android/169/recyclerview/18296/recyclerview-with-databinding#t=20170723011950244771
 */

package com.salas.javiert.magicmirror.Views.DataBindingTest.DatabaseFragment.Fragment.RecyclerViewClasses;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.loopj.android.http.TextHttpResponseHandler;
import com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton.myConnectionSingleton;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.DatabaseRestClient;
import com.salas.javiert.magicmirror.databinding.RecyclerviewRowDatabindBinding;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by javi6 on 7/22/2017.
 */

public class bindingViewHolder extends RecyclerView.ViewHolder {

    private final String TAG = "childObserverView";
    private RecyclerviewRowDatabindBinding mBinding;


    public bindingViewHolder(View itemView) {
        super(itemView);
        this.mBinding = DataBindingUtil.bind(itemView);
    }

    public RecyclerviewRowDatabindBinding getmBinding() {
        return mBinding;
    }

    public void bind(childObserver item) {
        mBinding.setData(item);
        mBinding.executePendingBindings();
    }

    public void setHandler(childHandler handler) {
        mBinding.setHandler(handler);
        mBinding.executePendingBindings();
    }

    public boolean isServerAddressField() {
        return (mBinding.getData().getName().equals("Host:Port") || mBinding.getData().getName().equals("Directory"));
    }

    public void toggleLockEditText() {
        if (mBinding.getData().getLockStatus()) {
            setLockedState(false);
        } else {
            setLockedState(true);
        }
    }


    // Set the lock state
    // Set the inputType to null to prevent the user to enter text
    // Set the drawable to a locked lock / unlocked lock
    // Set the text color
    // TODO: Find a better color and a better way to lock this field
    private void setLockedState(boolean lockTheView) {
        if (lockTheView) {
            mBinding.etSettingsSubtext.setInputType(InputType.TYPE_NULL);
            mBinding.etSettingsSubtext.setTextColor(Color.GRAY);
            mBinding.ivlock.setImageResource(R.drawable.icons8_lock_16);
            mBinding.getData().setLockStatus(true);
        } else {
            mBinding.etSettingsSubtext.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            mBinding.etSettingsSubtext.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            mBinding.etSettingsSubtext.setTextColor(Color.BLACK);
            mBinding.ivlock.setImageResource(R.drawable.icons8_unlock_16);
            mBinding.getData().setLockStatus(false);
        }
    }


    // Tries to establish a connection with the server
    public void tryConnection(Context context) {
        if (!mBinding.getData().getConnectionSuccessful() && !isServerAddressField()) {
            Context[] myTaskParams = {context, null, null};
            //This has do be done in a AsyncTask as to not block the main thread.
            pingTask myTask = new pingTask();
            myTask.execute(myTaskParams);
        }
    }

    // Does the work for ProgressBar and the imageview behind it
    private void isLoading(boolean spinning) {
        // If we want to show the user that an action is being done
        if (spinning) {
            mBinding.ivconnectionstatus.setVisibility(View.GONE);
            mBinding.tvprogressbar.setVisibility(View.VISIBLE);
        } else {
            mBinding.ivconnectionstatus.setVisibility(View.VISIBLE);
            mBinding.tvprogressbar.setVisibility(View.GONE);

            // If we saw that the connection was successful then we should lock the EditText
            if (mBinding.getData().getConnectionSuccessful())
                setLockedState(true);
        }
    }

    // Function that does the connection
    private void pingFunction(final Context context, String pingURL) throws UnsupportedEncodingException {
        //Define our response handler
        TextHttpResponseHandler response = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                // Since we couldn't connect, set the connection Status
                mBinding.getData().setConnectionSuccessful(false);
                Log.d("ConnectionTest", mBinding.getData().getConnectionSuccessful().toString());
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                // Since we connected, set the connection Status
                mBinding.getData().setConnectionSuccessful(true);
                myConnectionSingleton.getInstance().saveURLStatus(context, true);
                Log.d("ConnectionTest", mBinding.getData().getConnectionSuccessful().toString());
            }
        };
        DatabaseRestClient.post(context, pingURL, createPingEntity(), "application/x-www-form-urlencoded", response);
    }

    // Create the httpEntity for the post message
    private HttpEntity createPingEntity() throws UnsupportedEncodingException {
        StringEntity myEntitiy = new StringEntity("");
        myEntitiy.setContentEncoding("UTF-8");
        myEntitiy.setContentType("application/json");
        return myEntitiy;
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
                pingFunction(params[0], mBinding.getData().getSubtitle()); // Try to establish a connection
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