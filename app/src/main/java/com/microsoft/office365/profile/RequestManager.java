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

    protected void executeRequest(URL endpoint, String acceptHeader, JsonRequestListener requestListener){
        JsonRequestRunnable jsonRequestRunnable = new JsonRequestRunnable(endpoint, acceptHeader, requestListener);
        mExecutor.submit(jsonRequestRunnable);
    }

    protected void executeRequest(URL endpoint, InputStreamRequestListener requestListener){
        InputStreamRequestRunnable requestRunnable = new InputStreamRequestRunnable(endpoint, requestListener);
        mExecutor.submit(requestRunnable);
    }

    private class JsonRequestRunnable implements Runnable {
        protected static final String TAG = "RequestRunnable";
        protected URL mEndpoint;
        protected JsonRequestListener mJsonRequestListener;
        protected String mAcceptHeader;

        protected JsonRequestRunnable(URL endpoint, String acceptHeader, JsonRequestListener jsonRequestListener) {
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
                mJsonRequestListener.onRequestSuccess(jsonElement);
            } catch (IOException | InterruptedException | ExecutionException e) {
                Log.e(TAG, e.getMessage());
                mJsonRequestListener.onRequestFailure(e);
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
    private class InputStreamRequestRunnable implements Runnable {
        protected static final String TAG = "RequestRunnable";
        protected URL mEndpoint;
        protected InputStreamRequestListener mInputStreamRequestListener;

        protected InputStreamRequestRunnable(URL endpoint, InputStreamRequestListener inputStreamRequestListener) {
            mEndpoint = endpoint;
            mInputStreamRequestListener = inputStreamRequestListener;
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

                httpsConnection.connect();

                mInputStreamRequestListener.onRequestSuccess(httpsConnection.getInputStream());
            } catch (IOException | InterruptedException | ExecutionException e) {
                Log.e(TAG, e.getMessage());
                mInputStreamRequestListener.onRequestFailure(e);
            } finally {
                //TODO: Figure out if we need to close these objects or not.
                if(httpsConnection != null){
                    httpsConnection.disconnect();
                }
            }
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
