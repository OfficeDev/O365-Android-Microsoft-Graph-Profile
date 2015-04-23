/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 */
package com.microsoft.office365.profile.util;

import com.google.gson.JsonElement;

import java.net.URL;

/**
 * Interface used to provide event callbacks for http requests that return a JsonElement.
 */
public interface JsonRequestListener {
    /**
     * Success event handler
     * @param requestedEndpoint The requested endpoint. Objects that send multiple requests can
     *                          use this parameter to differentiate from what endpoint the request
     *                          comes from.
     * @param data The data from the endpoint.
     */
    void onRequestSuccess(URL requestedEndpoint, JsonElement data);

    /**
     * Error event handler
     * @param requestedEndpoint The requested endpoint. Objects that send multiple requests can
     *                          use this parameter to differentiate from what endpoint the request
     *                          comes from.
     * @param e Exception object with details about the error.
     */
    void onRequestFailure(URL requestedEndpoint, Exception e);
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