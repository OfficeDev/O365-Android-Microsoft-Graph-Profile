package com.microsoft.office365.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.office365.profile.model.BasicUserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
  */
public abstract class BaseListFragment extends ListFragment implements JsonRequestListener, AuthenticationListener {
    private static final String TAG = "BaseListFragment";
    protected static final String ACCEPT_HEADER = "application/json;odata.metadata=minimal;odata.streaming=true";

    protected ProfileApplication mApplication;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BaseListFragment() { }

    public abstract String getEndpoint();
    public abstract int getTitleResourceId();

    /**
     * Returns the message to display when a FileNotFoundException is thrown by a request.
     * In some cases, this might not be an error per-se. For example, if the request looks for the manager
     * property and it doesn't find it, it may be possible that the user just doesn't have a manager.
     * @return The message to display when a FileNotFoundException is thrown.
     */
    public CharSequence getFileNotFoundExceptionMessage(){
        return getResources().getText(R.string.file_not_found_exception_default_message);
    }
    /**
     * Returns the message to display when an empty array returned by a request.
     * For example, if the request looks for the direct reports and there's none.
     * @return The message to display when a an empty array is returned.
     */
    public CharSequence getEmptyArrayMessage(){
        return getResources().getText(R.string.empty_array_default_message);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplication = (ProfileApplication)getActivity().getApplication();

        AuthenticationManager
                .getInstance()
                .setContextActivity(getActivity());

        if(!mApplication.isUserSignedIn()) {
            AuthenticationManager
                    .getInstance()
                    .initialize(this);
        } else {
            String endpoint = Constants.GRAPH_RESOURCE_URL + mApplication.getTenant() + getEndpoint();
            sendRequest(endpoint);
        }
    }

    protected void sendRequest(String endpoint){
        try {
            RequestManager
                    .getInstance()
                    .executeRequest(new URL(endpoint),
                            ACCEPT_HEADER,
                            this);
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            // TODO: handle the case where the URL is malformed
        }
    }

    @Override
    public void onAuthenticationSuccess(AuthenticationResult authenticationResult) {
        mApplication.onAuthenticationSuccess(authenticationResult);
        String endpoint = Constants.GRAPH_RESOURCE_URL + mApplication.getTenant() + getEndpoint();
        sendRequest(endpoint);
    }

    @Override
    public void onAuthenticationFailure(Exception e) {
        //TODO: implement this
    }

    @Override
    public void onRequestFailure(URL requestedEndpoint, Exception e) {
        // TODO: implement failure...
        Log.e(TAG, e.getMessage());
    }
}