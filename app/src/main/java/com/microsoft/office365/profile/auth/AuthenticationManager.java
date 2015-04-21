/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 * Portions of this class are adapted from the AuthenticationController.java file from Microsoft Open Technologies, Inc.
 * located at https://github.com/OfficeDev/Office-365-SDK-for-Android/blob/master/samples/outlook/app/src/main/java/com/microsoft/services/controllers/AuthenticationController.java
 */
package com.microsoft.office365.profile.auth;

import android.app.Activity;
import android.util.Log;

import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.aad.adal.ADALError;
import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationException;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.AuthenticationResult.AuthenticationStatus;
import com.microsoft.aad.adal.PromptBehavior;
import com.microsoft.office365.profile.Constants;

/**
 * Class that provides a singleton object to manage interaction with Azure Active Directory to
 * get tokens that the app needs to send requests to Office 365.
 */
public class AuthenticationManager {
    private static final String TAG = "AuthenticationManager";

    private AuthenticationContext mAuthenticationContext;
    private Activity mContextActivity;
    private final String mResourceId;

    /**
     * Returns the singleton object that the app can use to get tokens.
     * @return The singleton AuthenticationManager object.
     */
    public static synchronized AuthenticationManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AuthenticationManager();
        }
        return INSTANCE;
    }

    /**
     * Sets the singleton object to null. Use it when signing users out of your app.
     */
    public static synchronized void resetInstance() {
        INSTANCE = null;
    }

    private static AuthenticationManager INSTANCE;

    private AuthenticationManager() {
        mResourceId = Constants.UNIFIED_ENDPOINT_RESOURCE_ID;
    }

    /**
     * Set the context activity to the current activity before getting tokens.
     *
     * @param contextActivity Current activity which can be utilized for interactive prompt.
     */
    public void setContextActivity(final Activity contextActivity) {
        this.mContextActivity = contextActivity;
    }

    /**
     * Calls AuthenticationContext.acquireToken once to get tokens. User must provide credentials
     * the first time. Subsequent calls retrieve tokens from the local cache or use the refresh
     * token to get a valid access token.
     * If all tokens expire, then the next call to getTokens will prompt for user credentials.
     * By default, you would use this in an asynchronous mode, but you can also call getTokens in
     * synchronously by appending get() to getTokens. For example, getTokens(null).get() which
     * will return an AuthenticationResult object.
     * @param authenticationCallback
     * @return A signal to wait on before continuing execution that contains an AuthenticationResult
     * object with information about the user, and the tokens.
     */
    public synchronized SettableFuture<AuthenticationResult> getTokens(final AuthenticationCallback authenticationCallback) {

        final SettableFuture<AuthenticationResult> result = SettableFuture.create();

        if (verifyAuthenticationContext()) {
            getAuthenticationContext().acquireToken(
                    this.mContextActivity,
                    this.mResourceId,
                    Constants.CLIENT_ID,
                    Constants.REDIRECT_URI,
                    PromptBehavior.Auto,
                    new AuthenticationCallback<AuthenticationResult>() {
                        @Override
                        public void onSuccess(final AuthenticationResult authenticationResult) {
                            if (authenticationResult != null) {
                                if(authenticationCallback != null) {
                                    if(authenticationResult.getStatus() == AuthenticationStatus.Succeeded) {
                                        authenticationCallback.onSuccess(authenticationResult);
                                    } else {
                                        // Unknown error. Errors, like when the user cancels the
                                        // operation usually go through the onError method
                                        authenticationCallback.onError(
                                                new AuthenticationException(ADALError.AUTH_FAILED,
                                                        "Authentication failed")
                                        );
                                    }
                                }
                                result.set(authenticationResult);
                            } else {
                                result.setException(new AuthenticationException(ADALError.AUTH_FAILED,
                                        "Authentication failed"));
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            if(authenticationCallback != null) {
                                authenticationCallback.onError(e);
                            }
                            result.setException(e);
                        }
                    }
            );
        } else {
            result.setException(new Throwable("Auth context verification failed. " +
                    "Use setContextActivity(Activity) before getTokens()"));
        }
        return result;
    }

    /**
     * Gets AuthenticationContext for Azure Active Directory.
     *
     * @return authenticationContext, if successful
     */
    public AuthenticationContext getAuthenticationContext() {
        if (mAuthenticationContext == null) {
            try {
                mAuthenticationContext = new AuthenticationContext(this.mContextActivity, Constants.AUTHORITY_URL, false);
            } catch (Throwable t) {
                Log.e(TAG, t.toString());
            }
        }
        return mAuthenticationContext;
    }

    private boolean verifyAuthenticationContext() {
        if (this.mContextActivity == null) {
            Log.e(TAG,"Must set context activity");
            return false;
        }
        return true;
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