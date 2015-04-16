package com.microsoft.office365.profile;

import com.google.gson.JsonElement;

/**
 * Created by Administrator on 4/9/2015.
 */
public interface RequestListener {
    void onRequestSuccess(String data);
    void onRequestFailure(Exception e);
}
