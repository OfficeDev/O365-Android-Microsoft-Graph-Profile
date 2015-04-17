package com.microsoft.office365.profile;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 */
public class ManagerFragment extends UserListFragment {
    protected static final String TAG = "ManagerFragment";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ManagerFragment() {
    }

    public String getEndpoint(){
        return "/users/" + ((ProfileActivity)getActivity()).getUserId() + "/manager";
    }

    @Override
    public int getTitleResourceId() {
        return R.string.fragment_manager_title;
    }

    /**
     * Returns the message to display when the user doesn't have a manager.
     * @return The message to display when the user doesn't have a manager.
     */
    @Override
    public CharSequence getFileNotFoundExceptionMessage() {
        return getResources().getText(R.string.file_not_found_exception_manager_fragment_message);
    }

    @Override
    public void onRequestFailure(Exception e) {
        Log.e(TAG, e.getMessage());

        //If we have a FileNotFoundException it's because this user doesn't have a manager
        if(e.getClass().equals(FileNotFoundException.class)) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                    View header = layoutInflater.inflate(R.layout.header_base_list, null);
                    TextView title = (TextView) header.findViewById(R.id.title);
                    title.setText(getTitleResourceId());

                    ArrayList<String> notAvailableList = new ArrayList<>();
                    notAvailableList.add("He doesn't report to anybody");

                    ListView listView = getListView();
                    listView.addHeaderView(header);
                    listView.setClickable(false);
                    listView.setEnabled(false);

                    setListAdapter(new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,
                            notAvailableList));
                    setListShown(true);
                }
            });
        } else { // If it's not a FileNotFoundException then let the parent handle the error
            super.onRequestFailure(e);
        }
    }
}
