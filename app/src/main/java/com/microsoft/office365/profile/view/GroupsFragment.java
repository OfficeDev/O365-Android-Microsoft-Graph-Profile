/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 */
package com.microsoft.office365.profile.view;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.microsoft.office365.profile.R;
import com.microsoft.office365.profile.model.Group;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 */
public class GroupsFragment extends BaseListFragment {
    protected static final String TAG = "GroupsFragment";
    private ArrayList<Group> mGroupList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupsFragment() {
    }

    public String getEndpoint(){
        return "/users/" + ((ProfileActivity)getActivity()).getUserId() + "/memberof";
    }

    /**
     * Returns the message to display when there are no direct reports returned by a request.
     * @return The message to display if there are no direct reports.
     */
    @Override
    CharSequence getEmptyArrayMessage() {
        return getResources().getText(R.string.empty_array_groups_fragment_message);
    }

    @Override
    public void onRequestSuccess(URL requestedEndpoint, final JsonElement data) {
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<Group>>() { }.getType();

        if(((JsonObject) data).has("value")) {
            mGroupList = gson.fromJson(((JsonObject) data).getAsJsonArray("value"), listType);
        } else {
            mGroupList = new ArrayList<>();
            Group justOneGroup = gson.fromJson(data, Group.class);
            mGroupList.add(justOneGroup);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView listView = getListView();
                // I don't want to accept any clicks
                listView.setEnabled(false);

                // If there are no elements, display a custom message
                if (mGroupList.size() == 0) {
                    Group noData = new Group();
                    noData.displayName = (String)getEmptyArrayMessage();
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