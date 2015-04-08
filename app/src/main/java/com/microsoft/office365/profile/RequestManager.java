package com.microsoft.office365.profile;

import android.util.Log;

import com.microsoft.aad.adal.AuthenticationResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
    protected static final int MAX_NUM_THREADS = 4;

    private static RequestManager INSTANCE;

    public static synchronized RequestManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RequestManager();
            int numProcessors = Runtime.getRuntime().availableProcessors();
            INSTANCE.mExecutor = MAX_NUM_THREADS < numProcessors ?
                    Executors.newFixedThreadPool(MAX_NUM_THREADS) :
                    Executors.newFixedThreadPool(numProcessors);
        }
        return INSTANCE;
    }

    protected void sendRequests(){
        RequestRunnable basicInfo = null;
        try {
            basicInfo = new RequestRunnable(new URL(Constants.GRAPH_RESOURCE_URL + "me"));
            } catch (MalformedURLException e){
            // TODO: handle the case where the URL is malformed
        }

        mExecutor.submit(basicInfo);
    }

    private class RequestRunnable implements Runnable {
        protected static final String TAG = "RequestRunnable";
        protected URL mEndpoint;
        protected String mAccessToken = null;
        protected InputStream mResponseStream;
        protected HttpsURLConnection mHttpsConnection;
        protected BufferedReader mBufferedReader;

        public RequestRunnable(URL endpoint) {
            mEndpoint = endpoint;
        }

        @Override
        public void run(){
            try {
                AuthenticationResult authenticationResult = AuthenticationManager
                        .getInstance()
                        .initialize()
                        .get();
                mAccessToken = authenticationResult.getAccessToken();
            } catch (InterruptedException | ExecutionException e){
                Log.e(TAG, e.getMessage());
                //TODO: Handle the case where the execution is cancelled
            }

            try {
                disableSSLVerification();
                mHttpsConnection = (HttpsURLConnection) mEndpoint.openConnection();

                mHttpsConnection.setRequestMethod("GET");
                mHttpsConnection.setRequestProperty("Authorization", "Bearer " + mAccessToken);
                mHttpsConnection.setRequestProperty("accept", "application/json;odata.metadata=minimal;odata.streaming=true");

                mHttpsConnection.connect();

                // Get the contents
                mResponseStream = mHttpsConnection.getInputStream();
                mBufferedReader = new BufferedReader(new InputStreamReader(mResponseStream));


                // Get the contents in a string variable
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = mBufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                //TODO: Handle the case where the execution is cancelled
            } finally {
                mHttpsConnection.disconnect();
                try {

                    mResponseStream.close();
                    mBufferedReader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                    //TODO: Handle the case where the execution is cancelled
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
