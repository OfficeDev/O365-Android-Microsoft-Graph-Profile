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
import android.widget.TabHost;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUserId = getIntent().hasExtra("userId") ? getIntent().getStringExtra("userId") : mApplication.getUserId();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TabHost tabs=(TabHost)findViewById(R.id.tabHost);
        tabs.setup();
        TabHost.TabSpec spec=tabs.newTabSpec("tag1");
        spec.setContent(R.id.basicInfoFragment);
        spec.setIndicator("General");
        tabs.addTab(spec);
        spec=tabs.newTabSpec("tag2");
        spec.setContent(R.id.directReportsFragment);
        spec.setIndicator("Direct Reports");
        tabs.addTab(spec);
        spec=tabs.newTabSpec("tag3");
        spec.setContent(R.id.groupsFragment);
        spec.setIndicator("Groups");
        tabs.addTab(spec);
        spec=tabs.newTabSpec("tag4");
        spec.setContent(R.id.filesFragment);
        spec.setIndicator("Files");
        tabs.addTab(spec);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public String getUserId() {
        return mUserId;
    }
}
