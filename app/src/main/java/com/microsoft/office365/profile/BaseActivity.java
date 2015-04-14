package com.microsoft.office365.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.UserInfo;

import java.util.concurrent.ExecutionException;

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

        if(AuthenticationManager.getInstance().getAuthenticationContext() == null) {
            AuthenticationManager
                    .getInstance()
                    .setContextActivity(this);
        }

        if(!mApplication.isUserSignedIn()){
            new AuthenticateTask().execute();
        }
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
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

            AuthenticationManager.getInstance().setContextActivity(this);
            new AuthenticateTask().execute();

            //Clear cookies.
            if(Build.VERSION.SDK_INT >= 21){
                CookieManager.getInstance().removeSessionCookies(null);
                CookieManager.getInstance().flush();
            } else {
                CookieManager.getInstance().removeSessionCookie();
                CookieSyncManager.getInstance().sync();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected class AuthenticateTask extends AsyncTask<Void, Void, AuthenticationResult> {
        private static final String TAG = "AuthenticateTask";

        @Override
        protected AuthenticationResult doInBackground(Void... params) {
            AuthenticationResult authenticationResult = null;
            try{
                authenticationResult = AuthenticationManager
                        .getInstance()
                        .initialize()
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                Log.e(TAG, "doInBackground - " + e.getMessage());
            }

            if(authenticationResult != null) {
                UserInfo userInfo = authenticationResult.getUserInfo();
                mApplication.setDisplayableId(userInfo.getDisplayableId());
                mApplication.setDisplayName(userInfo.getGivenName() + " " + userInfo.getGivenName());
            }

            return authenticationResult;
        }
    }
}
