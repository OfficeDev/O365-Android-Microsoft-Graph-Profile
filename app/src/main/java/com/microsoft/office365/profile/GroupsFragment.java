package com.microsoft.office365.profile;

import android.content.Context;
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
import com.microsoft.office365.profile.model.Group;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 */
public class GroupsFragment extends BaseListFragment {
    protected static final String TAG = "GroupsFragment";
    ArrayList<Group> mGroupList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupsFragment() {
    }

    public String getEndpoint(){
        return "/users/" + ((ProfileActivity)getActivity()).getUserId() + "/memberof";
    }

    @Override
    public int getTitleResourceId() {
        return R.string.fragment_groups_title;
    }

    /**
     * Returns the message to display when there are no direct reports returned by a request.
     * @return The message to display if there are no direct reports.
     */
    @Override
    public CharSequence getEmptyArrayMessage() {
        return getResources().getText(R.string.empty_array_groups_fragment_message);
    }

    @Override
    public void onRequestSuccess(URL requestedEndpoint, final JsonElement data) {
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<Group>>() { }.getType();

        if(((JsonObject) data).has("value")) {
            mGroupList = gson.fromJson(((JsonObject) data).getAsJsonArray("value"), listType);
        } else {
            mGroupList = new ArrayList<>();
            Group justOneGroup = gson.fromJson(data, Group.class);
            mGroupList.add(justOneGroup);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView listView = getListView();
                // I don't want to accept any clicks
                listView.setEnabled(false);

                // If there are no elements, display a custom message
                if (mGroupList.size() == 0) {
                    Group noData = new Group();
                    noData.displayName = (String)getEmptyArrayMessage();
                    mGroupList.add(noData);
                }
                setListAdapter(new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        mGroupList));
                setListShown(true);
            }
        });
    }
}
