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

    protected void sendRequest(URL endpoint, RequestListener requestListener){
        RequestRunnable requestRunnable = new RequestRunnable(endpoint, requestListener);
        mExecutor.submit(requestRunnable);
    }

    private class RequestRunnable implements Runnable {
        protected static final String TAG = "RequestRunnable";
        protected static final String ACCEPT_HEADER = "application/json;odata.metadata=full;odata.streaming=true";
        protected URL mEndpoint;
        protected RequestListener mRequestListener;

        protected RequestRunnable(URL endpoint, RequestListener requestListener) {
            mEndpoint = endpoint;
            mRequestListener = requestListener;
        }

        @Override
        public void run(){
            InputStream responseStream = null;
            HttpsURLConnection httpsConnection = null;
            JsonReader jsonReader = null;
            JsonElement jsonElement = null;

            try {
                //disableSSLVerification();
                httpsConnection = (HttpsURLConnection) mEndpoint.openConnection();

                httpsConnection.setRequestMethod("GET");
                httpsConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
                httpsConnection.setRequestProperty("accept", ACCEPT_HEADER);

                httpsConnection.connect();

                // Get the contents
                responseStream = httpsConnection.getInputStream();

                jsonReader = new JsonReader(new InputStreamReader(responseStream));
                JsonParser jsonParser = new JsonParser();
                jsonElement =  jsonParser.parse(jsonReader).getAsJsonObject();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                //TODO: Handle the case where the execution is cancelled
            } finally {
                //TODO: Figure out if we need to close these objects or not.
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
                mRequestListener.onRequestSuccess(jsonElement);
            }
        }

        protected String getAccessToken(){
            String accessToken = null;
            try {
                AuthenticationResult authenticationResult = AuthenticationManager
                        .getInstance()
                        .initialize()
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
