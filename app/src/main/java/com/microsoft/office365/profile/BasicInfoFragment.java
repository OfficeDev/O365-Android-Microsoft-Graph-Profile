package com.microsoft.office365.profile;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.microsoft.office365.profile.model.UserInfo;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 4/9/2015.
 */
public class BasicInfoFragment extends Fragment implements RequestListener {
    private static final String TAG = "BasicInfoFragment";
    protected static final String USER_ENDPOINT = "me";
    //protected static final String USER_ENDPOINT = "me";
    //protected static final String MY_THUMBNAILPHOTO_ENDPOINT = "me/thumbnailPhoto";
    protected static final String MY_THUMBNAILPHOTO_ENDPOINT = "patsoldemo4.onmicrosoft.com/users('236a9865-c42f-4535-9e26-30747979f5a3')/thumbnailPhoto";
    //protected static final String MY_THUMBNAILPHOTO_ENDPOINT = "dkershawtest10.ccsctp.net/users('a5b04667-f6f1-46ce-81df-13416d3aeacd')/thumbnailPhoto";
    // protected static final String MY_THUMBNAILPHOTO_ENDPOINT = "dkershawtest10.ccsctp.net/users/('a5b04667-f6f1-46ce-81df-13416d3aeacd')/thumbnailPhoto";
    protected TextView mDisplayNameTextView;
    protected TextView mJobTitleTextView;
    protected TextView mDepartmentTextView;
    protected TextView mHireDateTextView;
    protected TextView mMailTextView;
    protected TextView mTelephoneNumberTextView;
    protected TextView mStateTextView;
    protected TextView mCountryTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: remove this override if we're not using it
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_basic_info, container, false);

        mDisplayNameTextView = (TextView)fragmentView.findViewById(R.id.displayNameTextView);
        mJobTitleTextView = (TextView)fragmentView.findViewById(R.id.jobTitleTextView);
        mDepartmentTextView = (TextView)fragmentView.findViewById(R.id.departmentTextView);
        mHireDateTextView = (TextView)fragmentView.findViewById(R.id.hireDateTextView);
        mMailTextView = (TextView)fragmentView.findViewById(R.id.mailTextView);
        mTelephoneNumberTextView = (TextView)fragmentView.findViewById(R.id.telephoneNumberTextView);
        mStateTextView = (TextView)fragmentView.findViewById(R.id.stateTextView);
        mCountryTextView = (TextView)fragmentView.findViewById(R.id.countryTextView);

        try {
            RequestManager
                    .getInstance()
                    .sendRequest(new URL(Constants.GRAPH_RESOURCE_URL + USER_ENDPOINT), this);
//            RequestManager
//                    .getInstance()
//                    .sendRequest(new URL(Constants.GRAPH_RESOURCE_URL + MY_THUMBNAILPHOTO_ENDPOINT), this);
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            // TODO: handle the case where the URL is malformed
        }

        return fragmentView;
    }

    @Override
    public void onRequestSuccess(final JsonElement data) {
        Gson gson = new Gson();
        final UserInfo userInfo = gson.fromJson(data, UserInfo.class);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDisplayNameTextView.setText(userInfo.displayName);
                mJobTitleTextView.setText(userInfo.jobTitle);
                mDepartmentTextView.setText(userInfo.department);
                mHireDateTextView.setText(userInfo.hireDate);
                mMailTextView.setText(userInfo.mail);
                mTelephoneNumberTextView.setText(userInfo.telephoneNumber);
                mStateTextView.setText(userInfo.state);
                mCountryTextView.setText(userInfo.country);
            }
        });
    }

    @Override
    public void onRequestFailure(Exception e) {
        Log.e(TAG, e.getMessage());
        e.printStackTrace();
        //TODO: Implement error interface
    }
}
