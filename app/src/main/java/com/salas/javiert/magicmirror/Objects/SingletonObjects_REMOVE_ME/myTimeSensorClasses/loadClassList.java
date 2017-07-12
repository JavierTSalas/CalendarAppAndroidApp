/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Objects.SingletonObjects_REMOVE_ME.myTimeSensorClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.salas.javiert.magicmirror.Resources.DatabaseRestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by javi6 on 7/2/2017.
 */

public class loadClassList {


    // Literally does nothing
    private loadClassList() {
    }

    // Download the data and populate myList
    public List<myClassTimeObject> fetchFromInternet() {
        final List<myClassTimeObject> returnList = new ArrayList<>();
        DatabaseRestClient.get("outputclass.php", null, new JsonHttpResponseHandler() {

            JSONArray myJSONArrayResponse;

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    myJSONArrayResponse = new JSONArray(response.getString("Class_times"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadSchedule();
                // Do something with the response
                Log.d("Query", "Successfully loaded " + (myJSONArrayResponse.length()) + " indexes from outputassignment.php");

            }


            private void loadSchedule() {
                try {
                    for (int i = 0; i < myJSONArrayResponse.length(); i++) {
                        JSONObject jObject = myJSONArrayResponse.getJSONObject(i);


                        myClassTimeObject myClassTimeObject = null;
                        try {
                            myClassTimeObject = new myClassTimeObject(jObject.getInt("id"), jObject.getInt("class_id"), jObject.getString("day_of_week"), jObject.getString("time_start"), jObject.getString("time_start"), jObject.getString("building"), jObject.getInt("room"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        returnList.add(myClassTimeObject);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });
        return returnList;
    }

    //Returns 0 if not in class, else returns the class we are currently in
    public int intOfClassInCurrently() {
        for (myClassTimeObject classTime : getMyList()) {
            if (classTime.shouldSuggest())
                return classTime.getClass_id();
        }
        return 0;
    }

    public List<myClassTimeObject> getMyList() {
        // If we want to get the data from the internet
        return fetchFromInternet();
    }


    public List<myClassTimeObject> getMyList(Context context) {
        List<myClassTimeObject> returnList =
        // We need context to read SharedPreferences
        return readFromSharedPerferences(context);
    }

    // Load our data
    public synchronized ArrayList<myClassTimeObject> readFromSharedPerferences(Context context) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        Gson gson = new Gson();
        //TODO: Make this read "Please add an item to the queue" the queue is empty

        // Declare our Type
        Type type = new TypeToken<List<myClassTimeObject>>() {
        }.getType();
        // Read the data
        ArrayList<myClassTimeObject> QUEUE_DATA_FROM_PREFERENCES = gson.fromJson(appSharedPrefs.getString("classList", ""), type);
        Log.d("SharedPref", "Reading Class List from SharedPreferences");
        return QUEUE_DATA_FROM_PREFERENCES;

    }

    final protected class readClassListTask extends AsyncTask<Context, Void, List<myClassTimeObject>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<myClassTimeObject> doInBackground(Context... params) {
            Context context = params[0];
            return getMyList(context);
        }

        protected void onPostExecute(List<myClassTimeObject> result) {

        }
    }
}
