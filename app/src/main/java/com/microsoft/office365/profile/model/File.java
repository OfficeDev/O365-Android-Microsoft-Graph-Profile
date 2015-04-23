/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See full license at the bottom of this file.
 */
package com.microsoft.office365.profile.model;

import android.support.annotation.NonNull;

/**
 * Model class to store OneDrive for Business data.
 */
public class File implements CharSequence {
    public String name;
    public Node lastModifiedBy;

    @Override
    public int length() {
        return name.length();
    }

    @Override
    public char charAt(int index) {
        return name.charAt(index);
    }

    /**
     * Implemented this method to be able to use this object with the default ArrayAdapter<> class.
     * @return
     */
    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return name.subSequence(start, end);
    }

    public static class Node{
        public User user;
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