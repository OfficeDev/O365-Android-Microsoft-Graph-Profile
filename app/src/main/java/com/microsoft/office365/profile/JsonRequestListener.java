package com.microsoft.office365.profile;

import com.google.gson.JsonElement;

import java.net.URL;

/**
 * Created by Administrator on 4/9/2015.
 */
public interface JsonRequestListener {
    void onRequestSuccess(URL requestedEndpoint, JsonElement data);
    void onRequestFailure(URL requestedEndpoint, Exception e);
}
