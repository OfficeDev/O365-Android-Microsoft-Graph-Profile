package com.microsoft.office365.profile;

import android.content.Context;
import android.content.Intent;
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
import com.microsoft.office365.profile.model.BasicUserInfo;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by ricardol on 4/16/2015.
 */
public abstract class UserListFragment extends BaseListFragment {
    private static final String TAG = "UserListFragment";
    ArrayList<BasicUserInfo> mBasicUserInfoList;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        final Intent profileActivityIntent = new Intent(getActivity(), ProfileActivity.class);
        // Send the user's given name and displayable id to the SendMail activity
        profileActivityIntent.putExtra("userId", mBasicUserInfoList.get((int)id).objectId);
        startActivity(profileActivityIntent);
    }

    @Override
    public void onRequestSuccess(final JsonElement data) {
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<BasicUserInfo>>() { }.getType();

        if(((JsonObject) data).has("value")) {
            mBasicUserInfoList = gson.fromJson(((JsonObject) data).getAsJsonArray("value"), listType);
        } else {
            mBasicUserInfoList = new ArrayList<>();
            BasicUserInfo justOneUser = gson.fromJson(data, BasicUserInfo.class);
            mBasicUserInfoList.add(justOneUser);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                View header = layoutInflater.inflate(R.layout.header_base_list, null);
                TextView title = (TextView)header.findViewById(R.id.title);
                title.setText(getTitleResourceId());

                ListView listView = getListView();
                listView.addHeaderView(header);

                // If there are no elements, display a custom message
                if (mBasicUserInfoList.size() == 0) {
                    // I don't want to accept any clicks
                    listView.setEnabled(false);

                    BasicUserInfo noData = new BasicUserInfo();
                    noData.displayName = (String)getEmptyArrayMessage();
                    mBasicUserInfoList.add(noData);

                    setListAdapter(new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,
                            mBasicUserInfoList));
                } else {
                    setListAdapter(new BasicUserInfoAdapter(
                            getActivity(),
                            R.layout.list_item_basic_user_info,
                            mBasicUserInfoList));
                }
                setListShown(true);
            }
        });
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
