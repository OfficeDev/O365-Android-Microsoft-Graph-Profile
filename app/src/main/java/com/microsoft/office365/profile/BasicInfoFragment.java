package com.microsoft.office365.profile;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.microsoft.office365.profile.model.BasicInfo;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 4/9/2015.
 */
public class BasicInfoFragment extends Fragment implements RequestListener {
    protected static final String ME_ENDPOINT = "me";
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
                    .sendRequest(new URL(Constants.GRAPH_RESOURCE_URL + ME_ENDPOINT), this);
        } catch (MalformedURLException e) {
            // TODO: handle the case where the URL is malformed
        }

        return fragmentView;
    }

    @Override
    public void onRequestSuccess(JsonElement data) {
        Gson gson = new Gson();
        BasicInfo basicInfo = gson.fromJson(data, BasicInfo.class);

        mDisplayNameTextView.setText(basicInfo.displayName);
        mJobTitleTextView.setText(basicInfo.jobTitle);
        mDepartmentTextView.setText(basicInfo.department);
        mHireDateTextView.setText(basicInfo.hireDate);
        mMailTextView.setText("" + basicInfo.mail);
        mTelephoneNumberTextView.setText(basicInfo.telephoneNumber);
        mStateTextView.setText(basicInfo.state);
        mCountryTextView.setText(basicInfo.country);
    }

    @Override
    public void onRequestFailure(Exception e) {
        //TODO: Implement error interface
    }
}
