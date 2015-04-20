package com.microsoft.office365.profile.view;

import com.microsoft.office365.profile.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 */
public class DirectReportsFragment extends UserListFragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DirectReportsFragment() {
    }

    public String getEndpoint(){
        return "/users/" + ((ProfileActivity)getActivity()).getUserId() + "/directReports";
    }

    /**
     * Returns the message to display when there are no direct reports returned by a request.
     * @return The message to display if there are no direct reports.
     */
    @Override
    public CharSequence getEmptyArrayMessage() {
        return getResources().getText(R.string.empty_array_direct_reports_fragment_message);
    }
}
