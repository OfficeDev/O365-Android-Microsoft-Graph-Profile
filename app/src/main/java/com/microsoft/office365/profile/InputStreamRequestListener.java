package com.microsoft.office365.profile;

import java.io.InputStream;

/**
 * Created by ricardol on 4/16/2015.
 */
public interface InputStreamRequestListener {
    void onRequestSuccess(InputStream data);
    void onRequestFailure(Exception e);
}