/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 */
package com.microsoft.office365.profile.viewcontroller;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.microsoft.office365.profile.R;
import com.microsoft.office365.profile.util.EndpointFactory;
import com.microsoft.office365.profile.util.JsonRequestListener;
import com.microsoft.office365.profile.model.Group;
import com.microsoft.office365.profile.util.ProfileEndpoint;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

/**
 * The fragment for the groups in {@link ProfileActivity}.
 */
public class GroupsFragment extends BaseListFragment {
    private ArrayList<Group> mGroupList;

    /**
     * The endpoint that is getting requested by the parent fragment {@link BaseListFragment#onCreate(Bundle)}
     * @return The URL object that represents the endpoint
     */
    public URL getEndpoint(){
        return EndpointFactory.getEndpoint(ProfileEndpoint.GROUPS);
    }

    /**
     * Returns the message to display when there are no groups returned by a request.
     * @return The message to display if there are no direct reports.
     */
    @Override
    String getEmptyArrayMessage() {
        return getResources().getString(R.string.empty_array_groups_fragment_message);
    }

    /**
     * Event handler for the {@link com.microsoft.office365.profile.util.RequestManager#executeRequest(URL, String, JsonRequestListener)}
     * method
     * @param requestedEndpoint The requested endpoint. Objects that send multiple requests can
     *                          use this parameter to differentiate from what endpoint the request
     *                          comes from.
     * @param data The data from the endpoint.
     */
    @Override
    public void onRequestSuccess(URL requestedEndpoint, final JsonElement data) {
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<Group>>() { }.getType();
        mGroupList = gson.fromJson(((JsonObject) data).getAsJsonArray("value"), listType);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView listView = getListView();
                // I don't want to accept any clicks
                listView.setEnabled(false);

                // If there are no elements, display a custom message
                if (mGroupList.size() == 0) {
                    Group noData = new Group();
                    noData.displayName = getEmptyArrayMessage();
                    mGroupList.add(noData);
                }
                setListAdapter(new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        mGroupList));
                setListShown(true);
            }
        });
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