/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 */
package com.microsoft.office365.profile;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.AuthenticationSettings;
import com.microsoft.aad.adal.UserInfo;
import com.microsoft.office365.profile.auth.AuthenticationListener;

import java.security.SecureRandom;

/**
 * Created by ricardol on 4/13/2015.
 */
public class ProfileApplication extends Application implements AuthenticationListener {
    private static final String TAG = "ProfileApplication";

    private SharedPreferences mSharedPreferences;
    private static final String USER_ID_FIELD = "userId";
    private static final String DISPLAYNAME_FIELD = "displayName";
    private static final String TENANT_FIELD = "tenant";

    public ProfileApplication(){
        super();

        // Devices with API level lower than 18 must setup an encryption key.
        if (Build.VERSION.SDK_INT < 18 && AuthenticationSettings.INSTANCE.getSecretKeyData() == null) {
            AuthenticationSettings.INSTANCE.setSecretKey(generateSecretKey());
        }

        // We're not using Microsoft Intune's Company portal app,
        // skip the broker check so we don't get warnings about the following permissions
        // in manifest:
        // GET_ACCOUNTS
        // USE_CREDENTIALS
        // MANAGE_ACCOUNTS
        AuthenticationSettings.INSTANCE.setSkipBroker(true);
    }

    /**
     * Randomly generates an encryption key for devices with API level lower than 18.
     * @return The encryption key in a 32 byte long array.
     */
    protected byte[] generateSecretKey() {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        return key;
    }

    @Override
    public void onAuthenticationSuccess(AuthenticationResult authenticationResult) {
        UserInfo userInfo = authenticationResult.getUserInfo();
        if(!getUserId().equals(userInfo.getUserId())) {
            // AuthenticationResult returns the TenantId as null
            // we can extract the value from the displayableId
            setTenant(userInfo.getDisplayableId().split("@")[1]);
            setUserId(userInfo.getUserId());
            setDisplayName(userInfo.getGivenName() + " " + userInfo.getFamilyName());
        }
    }

    @Override
    public void onAuthenticationFailure(Exception e) {
        Log.e(TAG, e.getMessage());
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    public String getTenant() {
        return mSharedPreferences.getString(TENANT_FIELD, "");
    }

    public void setTenant(String tenant) {
        mSharedPreferences.edit().putString(TENANT_FIELD, tenant).apply();
    }

    public void resetTenant(){
        mSharedPreferences.edit().remove(TENANT_FIELD).apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(USER_ID_FIELD, "");
    }

    public void setUserId(String userId) {
        mSharedPreferences.edit().putString(USER_ID_FIELD, userId).apply();
    }

    public void resetUserId(){
        mSharedPreferences.edit().remove(USER_ID_FIELD).apply();
    }

    public String getDisplayName() {
        return mSharedPreferences.getString(DISPLAYNAME_FIELD, "");
    }

    public void setDisplayName(String displayName) {
        mSharedPreferences.edit().putString(DISPLAYNAME_FIELD, displayName).apply();
    }

    public void resetDisplayName(){
        mSharedPreferences.edit().remove(DISPLAYNAME_FIELD).apply();
    }

    public boolean isUserSignedIn(){
        return mSharedPreferences.contains(USER_ID_FIELD);
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