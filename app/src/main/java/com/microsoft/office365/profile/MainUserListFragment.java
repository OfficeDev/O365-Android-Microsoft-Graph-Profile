package com.microsoft.office365.profile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.office365.profile.model.BasicUserInfo;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 */
public class MainUserListFragment extends BaseUserListFragment {
    protected static final String TAG = "MainUserListFragment";
    protected static final String LAST_SECTION_ENDPOINT = "/users";
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainUserListFragment() {
    }

    public void setEndpoint(){
        mEndpoint = LAST_SECTION_ENDPOINT;
    }
}
