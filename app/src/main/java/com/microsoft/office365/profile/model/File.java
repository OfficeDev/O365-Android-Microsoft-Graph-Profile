package com.microsoft.office365.profile.model;

/**
 * Created by ricardol on 4/16/2015.
 */
public class File implements CharSequence {
    public String id;
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
