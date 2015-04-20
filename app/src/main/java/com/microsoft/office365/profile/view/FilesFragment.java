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
import com.microsoft.office365.profile.R;
import com.microsoft.office365.profile.model.File;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 */
public class FilesFragment extends BaseListFragment {
    protected static final String TAG = "FilesFragment";
    private ArrayList<File> mFileList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FilesFragment() {
    }

    public String getEndpoint(){
        return "/users/" + ((ProfileActivity)getActivity()).getUserId() + "/files";
    }

    /**
     * Returns the message to display when there are no direct reports returned by a request.
     * @return The message to display if there are no direct reports.
     */
    @Override
    CharSequence getEmptyArrayMessage() {
        return getResources().getText(R.string.empty_array_files_fragment_message);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        final Intent profileActivityIntent = new Intent(getActivity(), ProfileActivity.class);
        // Send the user's given name and displayable id to the SendMail activity
        profileActivityIntent.putExtra("userId", mFileList.get((int)id).lastModifiedBy.user.id);
        startActivity(profileActivityIntent);
    }

    @Override
    public void onRequestSuccess(URL requestedEndpoint, final JsonElement data) {
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
                // If there are no elements, display a custom message
                if (mFileList.size() == 0) {
                    File noData = new File();
                    noData.name = (String)getEmptyArrayMessage();
                    mFileList.add(noData);
                }
                setListAdapter(new FileAdapter(
                        getActivity(),
                        mFileList));
                setListShown(true);
            }
        });
    }

    private class FileAdapter extends ArrayAdapter<File>{
        final Context mContext;
        final ArrayList<File> mData;
        final LayoutInflater mLayoutInflater;

        public FileAdapter(Context context, ArrayList<File> data) {
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
            v.setText(mData.get(position).name);
            v = (TextView) row.findViewById(android.R.id.text2);
            v.setText("Last modified by: " + mData.get(position).lastModifiedBy.user.displayName);
            return row;
        }
    }
}
