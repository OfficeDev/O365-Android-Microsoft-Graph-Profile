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
                //TODO: In Production, we don't need to disable SSL verification
                //disableSSLVerification();
                httpsConnection = (HttpsURLConnection) mEndpoint.openConnection();

                httpsConnection.setRequestMethod("GET");
                String accessToken = AuthenticationManager
                        .getInstance()
                        .initialize(null)
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
                //TODO: Figure out if we need to close these objects or not.
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
                //TODO: In Production, we don't need to disable SSL verification
                //disableSSLVerification();
                httpsConnection = (HttpsURLConnection) mEndpoint.openConnection();

                httpsConnection.setRequestMethod("GET");
                String accessToken = AuthenticationManager
                        .getInstance()
                        .initialize(null)
                        .get().getAccessToken();
                httpsConnection.setRequestProperty("Authorization", "Bearer " + accessToken);

                httpsConnection.connect();

                mInputStreamRequestListener.onRequestSuccess(mEndpoint, httpsConnection.getInputStream());
            } catch (IOException | InterruptedException | ExecutionException e) {
                Log.e(TAG, e.getMessage());
                mInputStreamRequestListener.onRequestFailure(mEndpoint, e);
            } finally {
                //TODO: Figure out if we need to close these objects or not.
                if(httpsConnection != null){
                    httpsConnection.disconnect();
                }
            }
        }
    }
}
