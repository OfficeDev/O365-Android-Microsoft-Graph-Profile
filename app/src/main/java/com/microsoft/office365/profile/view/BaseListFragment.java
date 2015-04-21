/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 */
package com.microsoft.office365.profile.view;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.office365.profile.Constants;
import com.microsoft.office365.profile.ProfileApplication;
import com.microsoft.office365.profile.R;
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
public abstract class BaseListFragment extends ListFragment implements JsonRequestListener, AuthenticationCallback {
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
                    .getTokens(this);
        } else {
            String endpoint = Constants.UNIFIED_ENDPOINT_RESOURCE_URL + mApplication.getTenant() + getEndpoint();
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
            Toast.makeText(
                    getActivity(),
                    R.string.malformed_url_toast_text,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccess(Object authenticationResult) {
        mApplication.onSuccess(authenticationResult);
        String endpoint = Constants.UNIFIED_ENDPOINT_RESOURCE_URL + mApplication.getTenant() + getEndpoint();
        sendRequest(endpoint);
    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, e.getMessage());
        Toast.makeText(
                getActivity(),
                R.string.auth_failure_toast_text,
                Toast.LENGTH_LONG).show();
        AuthenticationManager
                .getInstance()
                .getTokens(this);
    }

    @Override
    public void onRequestFailure(URL requestedEndpoint, Exception e) {
        Log.e(TAG, e.getMessage());
        Toast.makeText(
                getActivity(),
                R.string.http_failure_toast_text,
                Toast.LENGTH_LONG).show();
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