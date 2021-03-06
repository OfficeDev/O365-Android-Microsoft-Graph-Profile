/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 */
package com.microsoft.office365.profile.viewcontroller;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.microsoft.office365.profile.R;
import com.microsoft.office365.profile.model.User;
import com.microsoft.office365.profile.util.EndpointFactory;
import com.microsoft.office365.profile.util.InputStreamRequestListener;
import com.microsoft.office365.profile.util.JsonRequestListener;
import com.microsoft.office365.profile.util.ProfileEndpoint;
import com.microsoft.office365.profile.util.RequestManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * The fragment for the user details in {@link ProfileActivity}
 * Displays thumbnailPhoto (if available), manager, and user properties.
 */
public class UserDetailsFragment extends Fragment implements
        JsonRequestListener, InputStreamRequestListener, View.OnClickListener {
    private static final String TAG = "UserDetailsFragment";
    private static final String ACCEPT_HEADER = "application/json;odata.metadata=full;odata.streaming=true";

    private TextView mJobTitleTextView;
    private TextView mDepartmentTextView;
    private TextView mHireDateTextView;
    private TextView mAliasTextView;
    private TextView mTelephoneNumberTextView;
    private TextView mStateTextView;
    private TextView mCountryTextView;
    private ImageView mThumbnailPhotoImageView;
    private TextView mManagerDisplayName;
    private TextView mManagerJobTitle;
    private TextView mNoManager;
    private LinearLayout mManagerSection;
    private URL mUserDetailsEndpoint;
    private URL mThumbnailPhotoEndpoint;
    private URL mManagerEndpoint;
    private User mManager;
    private LinearLayout mProgressContainer;
    private RelativeLayout mContainerLayout;

    /**
     * Initializes all the views and uses the {@link RequestManager} object to send the
     * requests to get thumbnailPhoto, manager and user properties.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_user_details, container, false);

        mJobTitleTextView = (TextView)fragmentView.findViewById(R.id.jobTitleTextView);
        mDepartmentTextView = (TextView)fragmentView.findViewById(R.id.departmentTextView);
        mHireDateTextView = (TextView)fragmentView.findViewById(R.id.hireDateTextView);
        mAliasTextView = (TextView)fragmentView.findViewById(R.id.aliasTextView);
        mTelephoneNumberTextView = (TextView)fragmentView.findViewById(R.id.telephoneNumberTextView);
        mStateTextView = (TextView)fragmentView.findViewById(R.id.stateTextView);
        mCountryTextView = (TextView)fragmentView.findViewById(R.id.countryTextView);
        mThumbnailPhotoImageView = (ImageView)fragmentView.findViewById(R.id.thumbnailPhotoImageView);
        mManagerDisplayName = (TextView)fragmentView.findViewById(R.id.managerDisplayName);
        mManagerJobTitle = (TextView)fragmentView.findViewById(R.id.managerJobTitle);
        mNoManager = (TextView)fragmentView.findViewById(R.id.noManager);
        mManagerSection = (LinearLayout)fragmentView.findViewById(R.id.managerSection);
        mProgressContainer = (LinearLayout)fragmentView.findViewById(R.id.progressContainer);
        mContainerLayout = (RelativeLayout)fragmentView.findViewById(R.id.dataContainer);

        mManagerSection.setOnClickListener(this);

        mUserDetailsEndpoint = EndpointFactory.getEndpoint(ProfileEndpoint.USER_DETAILS);
        mThumbnailPhotoEndpoint = EndpointFactory.getEndpoint(ProfileEndpoint.THUMBNAIL_PHOTO);
        mManagerEndpoint = EndpointFactory.getEndpoint(ProfileEndpoint.MANAGER);

        RequestManager
                .getInstance()
                .executeRequest(mUserDetailsEndpoint,
                        ACCEPT_HEADER,
                        this);
        RequestManager
                .getInstance()
                .executeRequest(mThumbnailPhotoEndpoint,
                        this);
        RequestManager
                .getInstance()
                .executeRequest(mManagerEndpoint,
                        ACCEPT_HEADER,
                        this);

        return fragmentView;
    }

    /**
     * Event handler for the onClick event on the manager views.
     * @param v
     */
    @Override
    public void onClick(View v){
        final Intent profileActivityIntent = new Intent(getActivity(), ProfileActivity.class);
        // Send the user's id to the Profile activity
        profileActivityIntent.putExtra("userId", mManager.objectId);
        profileActivityIntent.putExtra("displayName", mManager.displayName);
        startActivity(profileActivityIntent);
    }

    /**
     * Handles the onSucess events for the manager and user properties requests
     * @param requestedEndpoint The requested endpoint. Objects that send multiple requests can
     *                          use this parameter to differentiate from what endpoint the request
     *                          comes from.
     * @param data The data from the endpoint.
     */
    @Override
    public void onRequestSuccess(final URL requestedEndpoint, final JsonElement data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (requestedEndpoint.sameFile(mUserDetailsEndpoint)) {
                    User user = new Gson().fromJson(data, User.class);
                    mJobTitleTextView.setText(user.jobTitle);
                    mDepartmentTextView.setText(user.department);
                    mHireDateTextView.setText(user.hireDate);
                    mAliasTextView.setText(user.mailNickname);
                    mTelephoneNumberTextView.setText(user.telephoneNumber);
                    mStateTextView.setText(user.state);
                    mCountryTextView.setText(user.country);

                    mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                            getActivity(), android.R.anim.fade_out));
                    mContainerLayout.startAnimation(AnimationUtils.loadAnimation(
                            getActivity(), android.R.anim.fade_in));
                    mProgressContainer.setVisibility(View.GONE);
                    mContainerLayout.setVisibility(View.VISIBLE);
                } else {
                    mManager = new Gson().fromJson(data, User.class);
                    mManagerDisplayName.setText(mManager.displayName);
                    mManagerJobTitle.setText(mManager.jobTitle);
                }
            }
        });
    }

    /**
     * Handles onSucess events for the thumbnailPhoto request
     * @param requestedEndpoint The requested endpoint. Objects that send multiple requests can
     *                          use this parameter to differentiate from what endpoint the request
     *                          comes from.
     * @param data The data from the endpoint.
     */
    @Override
    public void onRequestSuccess(URL requestedEndpoint, InputStream data) {
        final Drawable thumbnailPhotoDrawable = Drawable.createFromStream(data, null);
        try {
            data.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThumbnailPhotoImageView.setImageDrawable(thumbnailPhotoDrawable);
            }
        });
    }

    /**
     * Handles onFailure events for all requests
     * @param requestedEndpoint The requested endpoint. Objects that send multiple requests can
     *                          use this parameter to differentiate from what endpoint the request
     *                          comes from.
     * @param e Exception object with details about the error.
     */
    @Override
    public void onRequestFailure(URL requestedEndpoint, Exception e) {
        Log.e(TAG, e.getMessage());

        if(requestedEndpoint.sameFile(mManagerEndpoint)){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mNoManager.setText(getResources().getText(R.string.file_not_found_exception_manager_fragment_message));
                    mNoManager.setVisibility(View.VISIBLE);
                    mManagerDisplayName.setVisibility(View.GONE);
                    mManagerJobTitle.setVisibility(View.GONE);
                    mManagerSection.setClickable(false);
                }
            });
        } else if(!requestedEndpoint.sameFile(mThumbnailPhotoEndpoint)){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(
                            getActivity(),
                            R.string.http_failure_toast_text,
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}

// *********************************************************
//
// O365-Android-Profile, https://github.com/OfficeDev/O365-Android-Profile
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