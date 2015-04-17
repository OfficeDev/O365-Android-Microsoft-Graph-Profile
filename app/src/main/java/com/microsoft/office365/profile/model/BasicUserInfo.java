package com.microsoft.office365.profile.model;

/**
 * Created by ricardol on 4/10/2015.
 */
public class BasicUserInfo implements CharSequence {
    // The files fragment returns a lastModifiedBy field that contains and id property. In contrast,
    // all of the user scenarios (Manager, Direct Reports, User List) use the objectId property.
    // I defined both properties here to make it easy for Gson to infer the values.
    public String id;
    public String objectId;
    public String displayName;
    public String jobTitle;

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
