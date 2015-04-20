package com.microsoft.office365.profile.view;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;

import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.office365.profile.Constants;
import com.microsoft.office365.profile.ProfileApplication;
import com.microsoft.office365.profile.R;
import com.microsoft.office365.profile.auth.AuthenticationListener;
import com.microsoft.office365.profile.auth.AuthenticationManager;
import com.microsoft.office365.profile.http.JsonRequestListener;
import com.microsoft.office365.profile.http.RequestManager;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
  */
public abstract class BaseListFragment extends ListFragment implements JsonRequestListener, AuthenticationListener {
    private static final String TAG = "BaseListFragment";
    private static final String ACCEPT_HEADER = "application/json;odata.metadata=minimal;odata.streaming=true";

    private ProfileApplication mApplication;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BaseListFragment() { }

    protected abstract String getEndpoint();

    /**
     * Returns the message to display when an empty array returned by a request.
     * For example, if the request looks for the direct reports and there's none.
     * @return The message to display when a an empty array is returned.
     */
    CharSequence getEmptyArrayMessage(){
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

    private void sendRequest(String endpoint){
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