package com.microsoft.office365.profile.model;

/**
 * Created by ricardol on 4/10/2015.
 */
public class BasicUserInfo implements CharSequence {
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
    public CharSequence subSequence(int start, int end) {
        return displayName.subSequence(start, end);
    }

    public String jobTitle;
}
