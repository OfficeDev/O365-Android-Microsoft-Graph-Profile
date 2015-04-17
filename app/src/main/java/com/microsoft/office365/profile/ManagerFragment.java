package com.microsoft.office365.profile;

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
}
