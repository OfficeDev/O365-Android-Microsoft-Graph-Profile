package com.microsoft.office365.profile;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public abstract class NavigationBarActivity extends BaseActivity {
    private static final String TAG = "NavigationBarActivity";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mUsernameTextView;
    private TextView mEmailTextView;
    private Button mSignOutButton;

    protected abstract int getActivityLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        if (mApplication.isUserSignedIn()) {
            setUpNavigationDrawer();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mApplication.isUserSignedIn()) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle.
        if (mApplication.isUserSignedIn()) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    private void setUpNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.activity_navigation_drawer_activity_container);
        LayoutInflater.from(this).inflate(getActivityLayoutId(), frameLayout);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_navigation_drawer_navigation_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_loading) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mUsernameTextView = (TextView) findViewById(R.id.activity_navigation_drawer_username_text_view);
        mEmailTextView = (TextView) findViewById(R.id.activity_navigation_drawer_email_text_view);
        mSignOutButton = (Button) findViewById(R.id.activity_navigation_drawer_sign_out_button);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear tokens.
                AuthenticationManager
                        .getInstance()
                        .getAuthenticationContext()
                        .getCache()
                        .removeAll();

                AuthenticationManager.resetInstance();
                RequestManager.resetInstance();
                mApplication.resetDisplayName();
                mApplication.resetDisplayableId();

                AuthenticationManager.getInstance().setContextActivity(NavigationBarActivity.this);
                new AuthenticateTask().execute();

                //Clear cookies.
                if(Build.VERSION.SDK_INT >= 21){
                    CookieManager.getInstance().removeSessionCookies(null);
                    CookieManager.getInstance().flush();
                } else {
                    CookieManager.getInstance().removeSessionCookie();
                    CookieSyncManager.getInstance().sync();
                }
            }
        });

        mEmailTextView.setText(mApplication.getDisplayableId());
        mUsernameTextView.setText(mApplication.getDisplayName());
    }
}