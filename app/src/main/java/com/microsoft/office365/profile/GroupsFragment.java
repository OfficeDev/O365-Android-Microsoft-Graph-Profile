package com.microsoft.office365.profile;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 */
public class GroupsFragment extends UserListFragment {
    protected static final String TAG = "GroupsFragment";

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
}
