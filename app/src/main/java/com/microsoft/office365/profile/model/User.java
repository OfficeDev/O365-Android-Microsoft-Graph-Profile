/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 */
package com.microsoft.office365.profile.model;

import android.support.annotation.NonNull;

/**
 * Created by Administrator on 4/6/2015.
 */
public class User implements CharSequence {
    // The files fragment returns a lastModifiedBy field that contains and id property. In contrast,
    // all of the user scenarios (Manager, Direct Reports, User List) use the objectId property.
    // I defined both properties here to make it easy for Gson to infer the values.
    public String id;
    public String objectId;
    public String country;
    public String department;
    public String displayName;
    public String hireDate;
    public String jobTitle;
    public String mailNickname;
    public String state;
    public String telephoneNumber;

    @Override
    public int length() {
        return displayName.length();
    }

    @Override
    public char charAt(int index) {
        return displayName.charAt(index);
    }

    @NonNull
    @Override
    public String toString() {
        return displayName;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return displayName.subSequence(start, end);
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