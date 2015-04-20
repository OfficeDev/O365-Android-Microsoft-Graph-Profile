package com.microsoft.office365.profile.auth;

import com.microsoft.aad.adal.AuthenticationResult;

/**
 * Created by ricardol on 4/13/2015.
 */
public interface AuthenticationListener {
    void onAuthenticationSuccess(AuthenticationResult authenticationResult);
    void onAuthenticationFailure(Exception e);
}
