package com.microsoft.office365.profile;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.microsoft.aad.adal.AuthenticationResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Administrator on 4/6/2015.
 */
public class RequestManager {
    protected ExecutorService mExecutor;
    private static RequestManager INSTANCE;

    public static synchronized RequestManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RequestManager();
            int numProcessors = Runtime.getRuntime().availableProcessors();
            INSTANCE.mExecutor = Executors.newFixedThreadPool(numProcessors);
        }
        return INSTANCE;
    }
    public static synchronized void resetInstance() {
        INSTANCE = null;
    }

    protected void executeRequest(URL endpoint, String acceptHeader, RequestListener requestListener){
        RequestRunnable requestRunnable = new RequestRunnable(endpoint, acceptHeader, requestListener);
        mExecutor.submit(requestRunnable);
    }

    private class RequestRunnable implements Runnable {
        protected static final String TAG = "RequestRunnable";
        protected URL mEndpoint;
        protected RequestListener mRequestListener;
        protected String mAcceptHeader;

        protected RequestRunnable(URL endpoint, String acceptHeader, RequestListener requestListener) {
            mEndpoint = endpoint;
            mRequestListener = requestListener;
            mAcceptHeader = acceptHeader;
        }

        @Override
        public void run(){
            InputStream responseStream = null;
            HttpsURLConnection httpsConnection = null;
            JsonReader jsonReader = null;


            try {
                //TODO: In Production, we don't need to disable SSL verification
                //disableSSLVerification();
                httpsConnection = (HttpsURLConnection) mEndpoint.openConnection();

                httpsConnection.setRequestMethod("GET");
                httpsConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
                if(mAcceptHeader != null){
                    httpsConnection.setRequestProperty("accept", mAcceptHeader);
                }

                httpsConnection.connect();

                // Get the contents
                responseStream = httpsConnection.getInputStream();

                jsonReader = new JsonReader(new InputStreamReader(responseStream));
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement =  jsonParser.parse(jsonReader).getAsJsonObject();
                mRequestListener.onRequestSuccess(jsonElement);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                mRequestListener.onRequestFailure(e);
                //TODO: Handle the case where the execution is cancelled
            } finally {
                //TODO: Figure out if we need tofire close these objects or not.
                if(httpsConnection != null){
                    httpsConnection.disconnect();
                }
                if(jsonReader != null) {
                    try {
                        jsonReader.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
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

        protected String getAccessToken(){
            String accessToken = null;
            try {
                AuthenticationResult authenticationResult = AuthenticationManager
                        .getInstance()
                        .initialize(null)
                        .get();
                accessToken = authenticationResult.getAccessToken();
            } catch (InterruptedException | ExecutionException e){
                Log.e(TAG, e.getMessage());
                //TODO: Handle the case where the execution is cancelled
            }

            return accessToken;
        }

        //Method used for bypassing SSL verification
        protected void disableSSLVerification() {

            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

            } };

            SSLContext sc = null;
            try {
                sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
    }
}
