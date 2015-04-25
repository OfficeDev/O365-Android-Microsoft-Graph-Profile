/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 */
package com.microsoft.office365.profile.viewcontroller;

import android.os.Bundle;

import com.microsoft.office365.profile.R;
import com.microsoft.office365.profile.util.EndpointFactory;
import com.microsoft.office365.profile.util.ProfileEndpoint;

import java.net.URL;

/**
 * Fragment for the direct reports in {@link ProfileActivity}.
 */
public class DirectReportsFragment extends UserListFragment {

    /**
     * The endpoint that is getting requested by the parent fragment {@link BaseListFragment#onCreate(Bundle)}
     * @return The URL object that represents the endpoint
     */
    public URL getEndpoint(){
        return EndpointFactory.getEndpoint(ProfileEndpoint.DIRECT_REPORTS);
    }

    /**
     * Returns the message to display when there are no direct reports returned by a request.
     * @return The message to display if there are no direct reports.
     */
    @Override
    String getEmptyArrayMessage() {
        return getResources().getString(R.string.empty_array_direct_reports_fragment_message);
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