package com.microsoft.office365.profile.util;

import android.util.Log;

import com.microsoft.office365.profile.ProfileApplication;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ricardol on 4/24/2015.
 */
public class EndpointFactory {
    private static final String TAG = "EndpointFactory";

    private static final String UNIFIED_ENDPOINT_RESOURCE_URL = "https://graph.microsoft.com/beta/";
    private static final String USERS_PATH_SECTION = "/users";
    private static final String PATH_SECTION_SEPARATOR = "/";
    private static final String USER_DIRECTORY_FILTER_QUERY_STRING = "?$filter=userType%20eq%20'Member'";
    private static final String THUMBNAIL_PHOTO_LAST_PATH_SECTION = "/thumbnailPhoto";
    private static final String MANAGER_LAST_PATH_SECTION = "/manager";
    private static final String DIRECT_REPORTS_LAST_PATH_SECTION = "/directReports";
    private static final String GROUPS_LAST_PATH_SECTION = "/memberOf";
    private static final String FILES_LAST_PATH_SECTION = "/files";

    public static URL getEndpoint(ProfileEndpoint profileEndpoint){
        URL endpoint = null;
        String endpointString = "";
        switch(profileEndpoint){
            case DIRECT_REPORTS:
                endpointString =
                        UNIFIED_ENDPOINT_RESOURCE_URL
                                + ProfileApplication.getTenant()
                                + USERS_PATH_SECTION
                                + PATH_SECTION_SEPARATOR
                                + ProfileApplication.getUserId()
                                + DIRECT_REPORTS_LAST_PATH_SECTION;
                break;
            case FILES:
                endpointString =
                        UNIFIED_ENDPOINT_RESOURCE_URL
                                + ProfileApplication.getTenant()
                                + USERS_PATH_SECTION
                                + PATH_SECTION_SEPARATOR
                                + ProfileApplication.getUserId()
                                + FILES_LAST_PATH_SECTION;
                break;
            case GROUPS:
                endpointString =
                        UNIFIED_ENDPOINT_RESOURCE_URL
                                + ProfileApplication.getTenant()
                                + USERS_PATH_SECTION
                                + PATH_SECTION_SEPARATOR
                                + ProfileApplication.getUserId()
                                + GROUPS_LAST_PATH_SECTION;
                break;
            case MANAGER:
                endpointString =
                        UNIFIED_ENDPOINT_RESOURCE_URL
                                + ProfileApplication.getTenant()
                                + USERS_PATH_SECTION
                                + PATH_SECTION_SEPARATOR
                                + ProfileApplication.getUserId()
                                + MANAGER_LAST_PATH_SECTION;
                break;
            case THUMBNAIL_PHOTO:
                endpointString =
                        UNIFIED_ENDPOINT_RESOURCE_URL
                                + ProfileApplication.getTenant()
                                + USERS_PATH_SECTION
                                + PATH_SECTION_SEPARATOR
                                + ProfileApplication.getUserId()
                                + THUMBNAIL_PHOTO_LAST_PATH_SECTION;
                break;
            case USER_DETAILS:
                endpointString =
                        UNIFIED_ENDPOINT_RESOURCE_URL
                                + ProfileApplication.getTenant()
                                + USERS_PATH_SECTION
                                + PATH_SECTION_SEPARATOR
                                + ProfileApplication.getUserId();
                break;
            case USER_DIRECTORY:
                endpointString =
                        UNIFIED_ENDPOINT_RESOURCE_URL
                                + ProfileApplication.getTenant()
                                + USERS_PATH_SECTION
                                + USER_DIRECTORY_FILTER_QUERY_STRING;
                break;
        }

        try {
            endpoint = new URL(endpointString);
        } catch (MalformedURLException e){
            Log.e(TAG, e.getMessage());
        }

        return endpoint;
    }
}
