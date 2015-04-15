package com.microsoft.office365.profile;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.AuthenticationSettings;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;


public class ProfileActivity extends BaseActivity {
    private static final String TAG = "ProfileActivity";
    protected String mUserId;

//    @Override
//    protected int getActivityLayoutId() {
//        return R.layout.activity_profile;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUserId = getIntent().hasExtra("userId") ? getIntent().getStringExtra("userId") : mApplication.getUserId();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // If the activity was launched with a user id attached, load that user information
        // else, load the current user information

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public String getUserId() {
        return mUserId;
    }
}
