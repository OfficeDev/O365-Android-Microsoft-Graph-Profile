/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 */
package com.microsoft.office365.profile.view;

import android.os.Bundle;
import android.widget.TabHost;

import com.microsoft.office365.profile.R;


public class ProfileActivity extends BaseActivity {
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUserId = getIntent().hasExtra("userId") ? getIntent().getStringExtra("userId") : mApplication.getUserId();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TabHost tabs=(TabHost)findViewById(R.id.tabHost);
        tabs.setup();
        TabHost.TabSpec spec=tabs.newTabSpec("tag1");
        spec.setContent(R.id.basicInfoFragment);
        spec.setIndicator(getResources().getString(R.string.fragment_general_title));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("tag2");
        spec.setContent(R.id.directReportsFragment);
        spec.setIndicator(getResources().getString(R.string.fragment_direct_reports_title));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("tag3");
        spec.setContent(R.id.groupsFragment);
        spec.setIndicator(getResources().getString(R.string.fragment_groups_title));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("tag4");
        spec.setContent(R.id.filesFragment);
        spec.setIndicator(getResources().getString(R.string.fragment_files_title));
        tabs.addTab(spec);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public String getUserId() {
        return mUserId;
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