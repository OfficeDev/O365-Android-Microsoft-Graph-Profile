package com.microsoft.office365.profile.view;

import android.content.Context;
import android.content.Intent;
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
import com.microsoft.office365.profile.model.User;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ricardol on 4/16/2015.
 */
public abstract class UserListFragment extends BaseListFragment {
    private static final String TAG = "UserListFragment";
    private ArrayList<User> mUserList;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        final Intent profileActivityIntent = new Intent(getActivity(), ProfileActivity.class);
        // Send the user's given name and displayable id to the SendMail activity
        profileActivityIntent.putExtra("userId", mUserList.get((int) id).objectId);
        startActivity(profileActivityIntent);
    }

    @Override
    public void onRequestSuccess(URL requestedEndpoint, final JsonElement data) {
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<User>>() { }.getType();

        mUserList = gson.fromJson(((JsonObject) data).getAsJsonArray("value"), listType);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If there are no elements, display a custom message
                if (mUserList.size() == 0) {
                    ListView listView = getListView();
                    // I don't want to accept any clicks
                    listView.setEnabled(false);

                    User noData = new User();
                    noData.displayName = (String) getEmptyArrayMessage();
                    mUserList.add(noData);

                    setListAdapter(new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,
                            mUserList));
                } else {
                    setListAdapter(new UserAdapter(
                            getActivity(),
                            mUserList));
                }
                setListShown(true);
            }
        });
    }

    private class UserAdapter extends ArrayAdapter<User>{
        final Context mContext;
        final ArrayList<User> mData;
        final LayoutInflater mLayoutInflater;

        public UserAdapter(Context context, ArrayList<User> data) {
            super(context, android.R.layout.two_line_list_item, data);

            this.mContext = context;
            this.mData = data;

            mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            if(convertView == null) {
                row = mLayoutInflater.inflate(android.R.layout.two_line_list_item, null);
            } else {
                row = convertView;
            }
            TextView v = (TextView) row.findViewById(android.R.id.text1);
            v.setText(mData.get(position).displayName);
            v = (TextView) row.findViewById(android.R.id.text2);
            v.setText(mData.get(position).jobTitle);
            return row;
        }
    }
}
