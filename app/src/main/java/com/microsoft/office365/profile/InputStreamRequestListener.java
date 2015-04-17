package com.microsoft.office365.profile;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by ricardol on 4/16/2015.
 */
public interface InputStreamRequestListener {
    void onRequestSuccess(URL requestedEndpoint, InputStream data);
    void onRequestFailure(URL requestedEndpoint, Exception e);
}