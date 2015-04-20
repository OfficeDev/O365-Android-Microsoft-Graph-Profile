package com.microsoft.office365.profile;

import android.os.Bundle;
import android.widget.TabHost;


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
        spec.setIndicator(getResources().getString(R.string.fragment_general_title));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("tag2");
        spec.setContent(R.id.directReportsFragment);
        spec.setIndicator(getResources().getString(R.string.fragment_direct_reports_title));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("tag3");
        spec.setContent(R.id.groupsFragment);
        spec.setIndicator(getResources().getString(R.string.fragment_groups_title));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("tag4");
        spec.setContent(R.id.filesFragment);
        spec.setIndicator(getResources().getString(R.string.fragment_files_title));
        tabs.addTab(spec);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public String getUserId() {
        return mUserId;
    }
}
