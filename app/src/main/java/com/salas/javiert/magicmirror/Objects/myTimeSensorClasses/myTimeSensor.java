/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Objects.myTimeSensorClasses;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.salas.javiert.magicmirror.DatabaseRestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by javi6 on 7/2/2017.
 */

public class myTimeSensor {


    private static List<myClassTimeObject> myList = new ArrayList();

    private static myTimeSensor instance = null;

    // Literally does nothing
    private myTimeSensor() {
    }

    //Everytime you need an instance, call this
    public static myTimeSensor getInstance() {
        if (instance == null)
            instance = new myTimeSensor();

        return instance;
    }

    // Clear the instance
    public void clearInstance() {
        instance = new myTimeSensor();
    }

    // Download the data and populate myList
    public void fetchFromInternet() {
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

                        myList.add(myClassTimeObject);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });

    }

    //Returns 0 if not in class, else returns the class we are currently in
    public int intOfClassInCurrently() {
        for (myClassTimeObject classTime : myList) {
            if (classTime.shouldSuggest())
                return classTime.getClass_id();
        }
        return 0;
    }

    public void resetInstance() {
        clearInstance();
        fetchFromInternet();
    }

}
