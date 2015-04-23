/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 */
package com.microsoft.office365.profile.viewcontroller;

import android.os.Bundle;
import android.widget.TabHost;

import com.microsoft.office365.profile.R;

/**
 * Activity that displays the following information about an user:
 * {@link UserDetailsFragment}
 * {@link DirectReportsFragment}
 * {@link GroupsFragment}
 * {@link FilesFragment}
 */
public class ProfileActivity extends BaseActivity {
    private String mUserId;

    /**
     * Gets the fragments organized in a TabHost. It also sets the title of the activity to
     * the displayName of the user.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If the activity was started with userId and displayName parameters, then use them.
        // Otherwise, retrieve the signed in user's userId and displayName
        // from the application object.
        mUserId = getIntent().hasExtra("userId") ? getIntent().getStringExtra("userId") : mApplication.getUserId();
        String displayName = getIntent().hasExtra("displayName") ? getIntent().getStringExtra("displayName") : mApplication.getDisplayName();
        setTitle(displayName);

        setContentView(R.layout.activity_profile);

        TabHost tabs=(TabHost)findViewById(R.id.tabHost);
        tabs.setup();
        TabHost.TabSpec spec=tabs.newTabSpec("userDetails");
        spec.setContent(R.id.userDetailsFragment);
        spec.setIndicator(getResources().getString(R.string.fragment_general_title));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("directReports");
        spec.setContent(R.id.directReportsFragment);
        spec.setIndicator(getResources().getString(R.string.fragment_direct_reports_title));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("groups");
        spec.setContent(R.id.groupsFragment);
        spec.setIndicator(getResources().getString(R.string.fragment_groups_title));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("files");
        spec.setContent(R.id.filesFragment);
        spec.setIndicator(getResources().getString(R.string.fragment_files_title));
        tabs.addTab(spec);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Return the user id
     * @return
     */
    public String getUserId() {
        return mUserId;
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