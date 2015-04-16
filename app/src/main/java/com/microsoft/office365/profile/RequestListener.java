package com.microsoft.office365.profile;

import com.google.gson.JsonElement;

import java.io.InputStream;

/**
 * Created by Administrator on 4/9/2015.
 */
public interface RequestListener {
    void onRequestSuccess(Object data);
    void onRequestFailure(Exception e);
}
