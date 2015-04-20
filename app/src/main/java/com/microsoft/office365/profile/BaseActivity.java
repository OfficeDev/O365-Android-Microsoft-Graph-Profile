package com.microsoft.office365.profile;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Created by ricardol on 4/13/2015.
 */
public class BaseActivity extends ActionBarActivity {
    protected static final String TAG = "BaseActivity";
    protected static final String PREFERENCES_NAME = "ProfilePreferences";

    protected ProfileApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplication = (ProfileApplication)getApplication();

        mApplication.mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_APPEND);
    }

    /**
     * This activity gets notified about the completion of the ADAL activity through this method.
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its
     *                   setResult().
     * @param data An Intent, which can return result data to the caller (various data
     *             can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult - AuthenticationActivity has come back with results");
        super.onActivityResult(requestCode, resultCode, data);
        AuthenticationManager
                .getInstance()
                .getAuthenticationContext()
                .onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
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
            mApplication.resetTenant();
            mApplication.resetUserId();

            //Clear cookies.
            if(Build.VERSION.SDK_INT >= 21){
                CookieManager.getInstance().removeSessionCookies(null);
                CookieManager.getInstance().flush();
            } else {
                CookieSyncManager.createInstance(this);
                CookieManager.getInstance().removeSessionCookie();
                CookieSyncManager.getInstance().sync();
            }

            final Intent userListIntent = new Intent(this, UserListActivity.class);
            startActivity(userListIntent);

            return true;
        } else if(id == R.id.my_profile) {
            final Intent profileActivityIntent = new Intent(this, ProfileActivity.class);
            // Send the user's id to the Profile activity
            profileActivityIntent.putExtra("userId", mApplication.getUserId());
            startActivity(profileActivityIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
