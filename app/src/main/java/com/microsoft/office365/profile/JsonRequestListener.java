package com.microsoft.office365.profile;

import com.google.gson.JsonElement;

import java.io.InputStream;

/**
 * Created by Administrator on 4/9/2015.
 */
public interface JsonRequestListener {
    void onRequestSuccess(JsonElement data);
    void onRequestFailure(Exception e);
}
