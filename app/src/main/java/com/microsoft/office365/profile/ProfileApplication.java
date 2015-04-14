package com.microsoft.office365.profile;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.AuthenticationSettings;

import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;

/**
 * Created by ricardol on 4/13/2015.
 */
public class ProfileApplication extends Application implements AuthenticationListener {
    protected static final String TAG = "ProfileApplication";
    protected SharedPreferences mSharedPreferences;
    protected static final String DISPLAYNAME_FIELD = "displayName";
    protected static final String DISPLAYABLEID_FIELD = "displayableId";

    public ProfileApplication(){
        super();

        // Devices with API level lower than 18 must setup an encryption key.
        if (Build.VERSION.SDK_INT < 18 && AuthenticationSettings.INSTANCE.getSecretKeyData() == null) {
            AuthenticationSettings.INSTANCE.setSecretKey(generateSecretKey());
        }

        // We're not using Microsoft Intune's Company portal app,
        // skip the broker check so we don't get warnings about the following permissions
        // in manifest:
        // GET_ACCOUNTS
        // USE_CREDENTIALS
        // MANAGE_ACCOUNTS
        AuthenticationSettings.INSTANCE.setSkipBroker(true);
    }

    /**
     * Randomly generates an encryption key for devices with API level lower than 18.
     * @return The encryption key in a 32 byte long array.
     */
    protected byte[] generateSecretKey() {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        return key;
    }

    @Override
    public void onAuthenticationSuccess(AuthenticationResult authenticationResult) {

    }

    @Override
    public void onAuthenticationFailure(Exception e) {
        Log.e(TAG, e.getMessage());
        //TODO: Implement error handler
    }

    public String getDisplayName() {
        return mSharedPreferences.getString(DISPLAYNAME_FIELD, "");
    }

    public void setDisplayName(String displayName) {
        mSharedPreferences.edit().putString(DISPLAYNAME_FIELD, displayName).apply();
    }

    public void resetDisplayName() {
        mSharedPreferences.edit().remove(DISPLAYNAME_FIELD).apply();
    }

    public String getDisplayableId() {
        return mSharedPreferences.getString(DISPLAYABLEID_FIELD, "");
    }

    public void setDisplayableId(String displayableId) {
        mSharedPreferences.edit().putString(DISPLAYABLEID_FIELD, displayableId).apply();
    }

    public void resetDisplayableId(){
        mSharedPreferences.edit().remove(DISPLAYABLEID_FIELD).apply();
    }

    public boolean isUserSignedIn(){
        return mSharedPreferences.contains(DISPLAYNAME_FIELD) && mSharedPreferences.contains(DISPLAYABLEID_FIELD);
    }
}
