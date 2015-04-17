package com.microsoft.office365.profile.model;

import android.support.annotation.NonNull;

/**
 * Created by Administrator on 4/6/2015.
 */
public class Group implements CharSequence {
    public String objectId;
    public String displayName;

    @Override
    public int length() {
        return displayName.length();
    }

    @Override
    public char charAt(int index) {
        return displayName.charAt(index);
    }

    @Override
    public String toString() {
        return displayName;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return displayName.subSequence(start, end);
    }
}
