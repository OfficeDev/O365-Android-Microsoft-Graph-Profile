/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 */
package com.microsoft.office365.profile.viewcontroller;

import android.content.Context;
import android.content.Intent;
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
import com.microsoft.office365.profile.util.JsonRequestListener;
import com.microsoft.office365.profile.model.User;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

/**
 * Abstract fragment for any user-based list.
 */
public abstract class UserListFragment extends BaseListFragment {
    private static final String TAG = "UserListFragment";
    private ArrayList<User> mUserList;

    /**
     * Event handler for the listItemClick event. This handler opens a new {@link ProfileActivity}
     * with the details of the selected user.
     * @param l The list view
     * @param v The view
     * @param position The position of the selected item in the list
     * @param id The id of the selected item in the list
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        final Intent profileActivityIntent = new Intent(getActivity(), ProfileActivity.class);
        // Send the user's given name and displayable id to the SendMail activity
        profileActivityIntent.putExtra("userId", mUserList.get((int) id).objectId);
        profileActivityIntent.putExtra("displayName", mUserList.get((int) id).displayName);
        startActivity(profileActivityIntent);
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

        Type listType = new TypeToken<ArrayList<User>>() { }.getType();

        mUserList = gson.fromJson(((JsonObject) data).getAsJsonArray("value"), listType);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If there are no elements, display a custom message
                if (mUserList.size() == 0) {
                    ListView listView = getListView();
                    // I don't want to accept any clicks
                    listView.setEnabled(false);

                    User noData = new User();
                    noData.displayName = getEmptyArrayMessage();
                    mUserList.add(noData);

                    setListAdapter(new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,
                            mUserList));
                } else {
                    setListAdapter(new UserAdapter(
                            getActivity(),
                            mUserList));
                }
                setListShown(true);
            }
        });
    }

    private class UserAdapter extends ArrayAdapter<User>{
        final Context mContext;
        final ArrayList<User> mData;
        final LayoutInflater mLayoutInflater;

        public UserAdapter(Context context, ArrayList<User> data) {
            super(context, android.R.layout.two_line_list_item, data);

            this.mContext = context;
            this.mData = data;

            mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            if(convertView == null) {
                row = mLayoutInflater.inflate(android.R.layout.two_line_list_item, null);
            } else {
                row = convertView;
            }
            TextView v = (TextView) row.findViewById(android.R.id.text1);
            v.setText(mData.get(position).displayName);
            v = (TextView) row.findViewById(android.R.id.text2);
            v.setText(mData.get(position).jobTitle);
            return row;
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