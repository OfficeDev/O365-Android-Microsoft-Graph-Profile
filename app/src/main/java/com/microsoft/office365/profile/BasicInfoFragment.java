package com.microsoft.office365.profile;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.microsoft.office365.profile.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 4/9/2015.
 */
public class BasicInfoFragment extends Fragment implements JsonRequestListener, InputStreamRequestListener {
    private static final String TAG = "BasicInfoFragment";
    protected static final String ACCEPT_HEADER = "application/json;odata.metadata=full;odata.streaming=true";

    protected TextView mDisplayNameTextView;
    protected TextView mJobTitleTextView;
    protected TextView mDepartmentTextView;
    protected TextView mHireDateTextView;
    protected TextView mMailTextView;
    protected TextView mTelephoneNumberTextView;
    protected TextView mStateTextView;
    protected TextView mCountryTextView;
    protected ImageView mThumbnailPhotoImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mThumbnailPhotoImageView = (ImageView)fragmentView.findViewById(R.id.thumbnailPhotoImageView);

        try {
            ProfileApplication application = (ProfileApplication)getActivity().getApplication();
            String userEndpoint = application.getTenant() + "/users/" + ((ProfileActivity)getActivity()).getUserId();
            String thumbnailPhotoEndpoint = application.getTenant() + "/users('" + ((ProfileActivity)getActivity()).getUserId() + "')/thumbnailphoto";
            RequestManager
                    .getInstance()
                    .executeRequest(new URL(Constants.GRAPH_RESOURCE_URL + userEndpoint),
                            ACCEPT_HEADER,
                            this);
            RequestManager
                    .getInstance()
                    .executeRequest(new URL(Constants.GRAPH_RESOURCE_URL + thumbnailPhotoEndpoint),
                            this);
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
    public void onRequestSuccess(InputStream data) {
        final Drawable thumbnailPhotoDrawable = Drawable.createFromStream(data, null);
        try {
            data.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThumbnailPhotoImageView.setImageDrawable(thumbnailPhotoDrawable);
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
