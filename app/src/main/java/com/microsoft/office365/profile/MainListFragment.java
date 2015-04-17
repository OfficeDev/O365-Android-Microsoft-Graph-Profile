package com.microsoft.office365.profile;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 */
public class MainListFragment extends UserListFragment {
    protected static final String TAG = "MainUserListFragment";
    protected static final String LAST_SECTION_ENDPOINT = "/users?$filter=userType%20eq%20'Member'";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainListFragment() {
    }

    public String getEndpoint(){
        return LAST_SECTION_ENDPOINT;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.fragment_main_user_list_title;
    }
}
