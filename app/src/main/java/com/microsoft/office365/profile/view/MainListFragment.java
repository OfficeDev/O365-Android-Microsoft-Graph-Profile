package com.microsoft.office365.profile.view;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 */
public class MainListFragment extends UserListFragment {
    private static final String LAST_SECTION_ENDPOINT = "/users?$filter=userType%20eq%20'Member'";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainListFragment() {
    }

    public String getEndpoint(){
        return LAST_SECTION_ENDPOINT;
    }
}
