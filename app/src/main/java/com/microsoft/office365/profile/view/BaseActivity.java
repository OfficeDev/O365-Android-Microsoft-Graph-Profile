/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 */
package com.microsoft.office365.profile.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.microsoft.office365.profile.ProfileApplication;
import com.microsoft.office365.profile.R;
import com.microsoft.office365.profile.auth.AuthenticationManager;

/**
 * Base activity that implements methods and fields required across all activities in the app.
 */
public class BaseActivity extends ActionBarActivity {
    private static final String TAG = "BaseActivity";
    private static final String PREFERENCES_NAME = "ProfilePreferences";

    ProfileApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplication = (ProfileApplication)getApplication();

        mApplication.setSharedPreferences(getSharedPreferences(PREFERENCES_NAME, MODE_APPEND));
    }

    /**
     * This activity gets notified about the completion of the ADAL activity through this method.
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its
     *                   setResult().
     * @param data An Intent, which can return result data to the caller (various data
     *             can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult - AuthenticationActivity has come back with results");
        super.onActivityResult(requestCode, resultCode, data);
        AuthenticationManager
                .getInstance()
                .getAuthenticationContext()
                .onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Method that adds items to the action bar if it is present.
     * The added items are:
     *  - Sign out action
     *  - My Profile action
     * @param menu The menu object to inflate
     * @return True if successful.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    /**
     * Event handler for the items in the action bar.
     * Users can sign out or see their profile from the action bar
     * @param item The selected item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.sign_out) {
            // Clear tokens
            AuthenticationManager
                    .getInstance()
                    .getAuthenticationContext()
                    .getCache()
                    .removeAll();

            // Clear objects that store user data
            AuthenticationManager.resetInstance();
            mApplication.resetTenant();
            mApplication.resetUserId();
            mApplication.resetDisplayName();

            // Clear cookies
            if(Build.VERSION.SDK_INT >= 21){
                CookieManager.getInstance().removeSessionCookies(null);
                CookieManager.getInstance().flush();
            } else {
                CookieSyncManager.createInstance(this);
                CookieManager.getInstance().removeSessionCookie();
                CookieSyncManager.getInstance().sync();
            }

            // Send the user to the initial activity, which will ask for credentials again
            final Intent userListIntent = new Intent(this, TenantUsersActivity.class);
            startActivity(userListIntent);

            return true;
        } else if(id == R.id.my_profile) {
            // Start the profile activity without sending any parameters. By default, the profile
            // activity will use the current user id and displayName.
            final Intent profileActivityIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileActivityIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
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