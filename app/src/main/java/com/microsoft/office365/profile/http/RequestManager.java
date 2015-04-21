/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 */
package com.microsoft.office365.profile.http;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.microsoft.office365.profile.auth.AuthenticationManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Administrator on 4/6/2015.
 */
public class RequestManager {
    private ExecutorService mExecutor;
    private static RequestManager INSTANCE;
    private static final int MAX_NUMBER_OF_THREADS = 6;

    public static synchronized RequestManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RequestManager();
            INSTANCE.mExecutor = Executors.newFixedThreadPool(MAX_NUMBER_OF_THREADS);
        }
        return INSTANCE;
    }
    public static synchronized void resetInstance() {
        INSTANCE = null;
    }

    public void executeRequest(URL endpoint, String acceptHeader, JsonRequestListener requestListener){
        JsonRequestRunnable jsonRequestRunnable = new JsonRequestRunnable(endpoint, acceptHeader, requestListener);
        mExecutor.submit(jsonRequestRunnable);
    }

    public void executeRequest(URL endpoint, InputStreamRequestListener requestListener){
        InputStreamRequestRunnable requestRunnable = new InputStreamRequestRunnable(endpoint, requestListener);
        mExecutor.submit(requestRunnable);
    }

    private class JsonRequestRunnable implements Runnable {
        static final String TAG = "RequestRunnable";
        final URL mEndpoint;
        final JsonRequestListener mJsonRequestListener;
        final String mAcceptHeader;

        JsonRequestRunnable(URL endpoint, String acceptHeader, JsonRequestListener jsonRequestListener) {
            mEndpoint = endpoint;
            mJsonRequestListener = jsonRequestListener;
            mAcceptHeader = acceptHeader;
        }

        @Override
        public void run(){
            InputStream responseStream = null;
            HttpsURLConnection httpsConnection = null;

            try {
                httpsConnection = (HttpsURLConnection) mEndpoint.openConnection();

                httpsConnection.setRequestMethod("GET");
                String accessToken = AuthenticationManager
                        .getInstance()
                        .getTokens(null)
                        .get().getAccessToken();
                httpsConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
                httpsConnection.setRequestProperty("accept", mAcceptHeader);

                httpsConnection.connect();

                // Get the contents
                responseStream = httpsConnection.getInputStream();
                JsonReader jsonReader = new JsonReader(new InputStreamReader(responseStream));
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement =  jsonParser.parse(jsonReader).getAsJsonObject();
                mJsonRequestListener.onRequestSuccess(mEndpoint, jsonElement);
            } catch (IOException | InterruptedException | ExecutionException e) {
                Log.e(TAG, e.getMessage());
                mJsonRequestListener.onRequestFailure(mEndpoint, e);
            } finally {
                if(httpsConnection != null){
                    httpsConnection.disconnect();
                }
                if (responseStream != null) {
                    try {
                        responseStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        }
    }
    private class InputStreamRequestRunnable implements Runnable {
        static final String TAG = "RequestRunnable";
        final URL mEndpoint;
        final InputStreamRequestListener mInputStreamRequestListener;

        InputStreamRequestRunnable(URL endpoint, InputStreamRequestListener inputStreamRequestListener) {
            mEndpoint = endpoint;
            mInputStreamRequestListener = inputStreamRequestListener;
        }

        @Override
        public void run(){
            HttpsURLConnection httpsConnection = null;

            try {
                httpsConnection = (HttpsURLConnection) mEndpoint.openConnection();

                httpsConnection.setRequestMethod("GET");
                String accessToken = AuthenticationManager
                        .getInstance()
                        .getTokens(null)
                        .get().getAccessToken();
                httpsConnection.setRequestProperty("Authorization", "Bearer " + accessToken);

                httpsConnection.connect();

                mInputStreamRequestListener.onRequestSuccess(mEndpoint, httpsConnection.getInputStream());
            } catch (IOException | InterruptedException | ExecutionException e) {
                Log.e(TAG, e.getMessage());
                mInputStreamRequestListener.onRequestFailure(mEndpoint, e);
            } finally {
                if(httpsConnection != null){
                    httpsConnection.disconnect();
                }
            }
        }
    }
}

// *********************************************************
//
// O365-Android-Connect, https://github.com/OfficeDev/O365-Android-Profile
//
// Copyright (c) Microsoft Corporation
// All rights reserved.
//
// MIT License:
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// *********************************************************