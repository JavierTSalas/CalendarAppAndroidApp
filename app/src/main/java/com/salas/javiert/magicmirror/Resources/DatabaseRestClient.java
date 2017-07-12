/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;
import com.salas.javiert.magicmirror.Objects.SingletonObjects_REMOVE_ME.myConnectionSingleton.myConnectionSingleton;

import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by javi6 on 5/26/2017.
 * Reference: http://www.withhari.com/http-request-android/ 5/27/2017
 */

public class DatabaseRestClient {

    private static final String TAG = "DATEBASE CONNECTION ";
    //TODO: Get this form myConnectionSingleton
    //private static final String BASE_URL = "http://192.168.56.1/learning/";
    private static String BASE_URL = "http://10.0.0.21/learning/";
    private static AsyncHttpClient client = new SyncHttpClient();

    public static void get(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        Log.d("URL=", getAbsoluteUrl(url));
        try {
            client.get(getAbsoluteUrl(url), params, responseHandler);
        } catch (Exception e) {
            Log.d("DatabaseClient", "Connection failed. See DatabaseRestClient e:" + e.toString());
        }
    }

    public static void post(Context context, String url, HttpEntity params, String contentType, ResponseHandlerInterface responseHandler) {
        client.post(context, getAbsoluteUrl(url, context), params, contentType, responseHandler);
        Log.d("POST", "SENT TO:" + getAbsoluteUrl(url, context));
    }

    private static String getAbsoluteUrl(String relativeUrl, Context context) {
        myConnectionSingleton.getInstance().loadURLToPreference(context);
        BASE_URL = myConnectionSingleton.getInstance().getURL();
        return BASE_URL + relativeUrl;

    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}


