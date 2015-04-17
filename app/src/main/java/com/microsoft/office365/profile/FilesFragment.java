package com.microsoft.office365.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.microsoft.office365.profile.model.File;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 */
public class FilesFragment extends BaseListFragment {
    protected static final String TAG = "FilesFragment";
    ArrayList<File> mFileList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FilesFragment() {
    }

    public String getEndpoint(){
        return "/users/" + ((ProfileActivity)getActivity()).getUserId() + "/files";
    }

    @Override
    public int getTitleResourceId() {
        return R.string.fragment_files_title;
    }

    /**
     * Returns the message to display when there are no direct reports returned by a request.
     * @return The message to display if there are no direct reports.
     */
    @Override
    public CharSequence getEmptyArrayMessage() {
        return getResources().getText(R.string.empty_array_files_fragment_message);
    }

    @Override
    public void onRequestSuccess(final JsonElement data) {
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<File>>() { }.getType();

        if(((JsonObject) data).has("value")) {
            mFileList = gson.fromJson(((JsonObject) data).getAsJsonArray("value"), listType);
        } else {
            mFileList = new ArrayList<>();
            File justOneFile = gson.fromJson(data, File.class);
            mFileList.add(justOneFile);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                View header = layoutInflater.inflate(R.layout.header_base_list, null);
                TextView title = (TextView) header.findViewById(R.id.title);
                title.setText(getTitleResourceId());

                ListView listView = getListView();
                listView.addHeaderView(header);

                // I don't want to accept any clicks
                listView.setEnabled(false);

                // If there are no elements, display a custom message
                if (mFileList.size() == 0) {
                    File noData = new File();
                    noData.name = (String)getEmptyArrayMessage();
                    mFileList.add(noData);
                }
                setListAdapter(new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        mFileList));
                setListShown(true);
            }
        });
    }
}
