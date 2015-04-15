package com.microsoft.office365.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
public abstract class BaseUserListFragment extends ListFragment implements RequestListener, AuthenticationListener {
    private static final String TAG = "BaseUserListFragment";
    protected static final String ACCEPT_HEADER = "application/json;odata.metadata=minimal;odata.streaming=true";

    protected String mEndpoint;
    protected ProfileApplication mApplication;
    ArrayList<BasicUserInfo> mBasicUserInfoList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BaseUserListFragment() { }

    public abstract void setEndpoint();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (ProfileApplication)getActivity().getApplication();

        setEndpoint();

        AuthenticationManager
                .getInstance()
                .setContextActivity(getActivity());

        if(!mApplication.isUserSignedIn()) {
            AuthenticationManager
                    .getInstance()
                    .initialize(this);
        } else {
            String endpoint = Constants.GRAPH_RESOURCE_URL + mApplication.getTenant() + mEndpoint;
            sendRequest(endpoint);
        }
    }

    protected void sendRequest(String endpoint){
        try {
            RequestManager
                    .getInstance()
                    .executeRequest(new URL(endpoint),
                            ACCEPT_HEADER,
                            this);
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            // TODO: handle the case where the URL is malformed
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        final Intent profileActivityIntent = new Intent(getActivity(), ProfileActivity.class);
        // Send the user's given name and displayable id to the SendMail activity
        profileActivityIntent.putExtra("userId", mBasicUserInfoList.get(position).objectId);
        startActivity(profileActivityIntent);
    }

    @Override
    public void onAuthenticationSuccess(AuthenticationResult authenticationResult) {
        mApplication.onAuthenticationSuccess(authenticationResult);
        mEndpoint = mApplication.getTenant() + "/users";
        String endpoint = Constants.GRAPH_RESOURCE_URL + mApplication.getTenant() + mEndpoint;
        sendRequest(endpoint);
    }

    @Override
    public void onAuthenticationFailure(Exception e) {
        //TODO: implement this
    }

    @Override
    public void onRequestSuccess(final JsonElement data) {
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<BasicUserInfo>>() {
        }.getType();
        final ArrayList<BasicUserInfo> basicUserInfoList;

        if(((JsonObject) data).has("value")) {
            basicUserInfoList = gson.fromJson(((JsonObject) data).getAsJsonArray("value"), listType);
        } else {
            basicUserInfoList = new ArrayList<>();
            BasicUserInfo justOneUser = gson.fromJson(data, BasicUserInfo.class);
            basicUserInfoList.add(justOneUser);
        }

        // Make the data available to the entire class
        mBasicUserInfoList = basicUserInfoList;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setListAdapter(new BasicUserInfoAdapter(
                        getActivity(),
                        R.layout.list_item_basic_user_info,
                        basicUserInfoList));
                setListShown(true);
            }
        });
    }

    @Override
    public void onRequestFailure(Exception e) {
        Log.e(TAG, e.getMessage());
        e.printStackTrace();
        //TODO: Implement error interface
    }

    private class BasicUserInfoAdapter extends ArrayAdapter<BasicUserInfo>{
        protected Context mContext;
        protected ArrayList<BasicUserInfo> mData;
        protected int mLayoutResourceId;
        protected LayoutInflater mLayoutInflater;

        public BasicUserInfoAdapter(Context context, int layoutResourceId, ArrayList<BasicUserInfo> data) {
            super(context, layoutResourceId, data);

            this.mLayoutResourceId = layoutResourceId;
            this.mContext = context;
            this.mData = data;

            mLayoutInflater = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            if(convertView == null) {
                row = mLayoutInflater.inflate(mLayoutResourceId, null);
            } else {
                row = convertView;
            }
            TextView v = (TextView) row.findViewById(R.id.displayName);
            v.setText(mData.get(position).displayName);
            v = (TextView) row.findViewById(R.id.jobTitle);
            v.setText(mData.get(position).jobTitle);
            return row;
        }
    }

}