package com.microsoft.office365.profile.util;

import android.util.Log;

import com.microsoft.office365.profile.ProfileApplication;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utility class that returns a URL object that represents the requested endpoint.
 * Endpoints generally follow this pattern:
 * https://graph.microsoft.com/v1.0/{tenant_id}/users/{user_id}/{resource}[?rest operator]
 */
public class EndpointFactory {
    private static final String TAG = "EndpointFactory";

    private static final String UNIFIED_ENDPOINT_RESOURCE_URL = "https://graph.microsoft.com/v1.0/";
    private static final String USERS_PATH_SECTION = "/users/";
    private static final String USER_DIRECTORY_FILTER_QUERY_STRING = "?$filter=userType%20eq%20'Member'";
    private static final String THUMBNAIL_PHOTO_RESOURCE = "/thumbnailPhoto";
    private static final String MANAGER_RESOURCE = "/manager";
    private static final String DIRECT_REPORTS_RESOURCE = "/directReports";
    private static final String GROUPS_RESOURCE = "/memberOf";
    private static final String FILES_RESOURCE = "/drive/root/children";

    /**
     * Method that builds a URL object that represents the requested endpoint.
     * @param profileEndpoint The profile endpoint requested by the caller.
     * @return The URL object that represents the requested endpoint.
     */
    public static URL getEndpoint(ProfileEndpoint profileEndpoint){
        URL endpoint = null;
        String endpointString = "";
        switch(profileEndpoint){
            case DIRECT_REPORTS:
                endpointString =
                        UNIFIED_ENDPOINT_RESOURCE_URL
                                + ProfileApplication.getTenant()
                                + USERS_PATH_SECTION
                                + ProfileApplication.getUserId()
                                + DIRECT_REPORTS_RESOURCE;
                break;
            case FILES:
                endpointString =
                        UNIFIED_ENDPOINT_RESOURCE_URL
                                + ProfileApplication.getTenant()
                                + USERS_PATH_SECTION
                                + ProfileApplication.getUserId()
                                + FILES_RESOURCE;
                break;
            case GROUPS:
                endpointString =
                        UNIFIED_ENDPOINT_RESOURCE_URL
                                + ProfileApplication.getTenant()
                                + USERS_PATH_SECTION
                                + ProfileApplication.getUserId()
                                + GROUPS_RESOURCE;
                break;
            case MANAGER:
                endpointString =
                        UNIFIED_ENDPOINT_RESOURCE_URL
                                + ProfileApplication.getTenant()
                                + USERS_PATH_SECTION
                                + ProfileApplication.getUserId()
                                + MANAGER_RESOURCE;
                break;
            case THUMBNAIL_PHOTO:
                endpointString =
                        UNIFIED_ENDPOINT_RESOURCE_URL
                                + ProfileApplication.getTenant()
                                + USERS_PATH_SECTION
                                + ProfileApplication.getUserId()
                                + THUMBNAIL_PHOTO_RESOURCE;
                break;
            case USER_DETAILS:
                endpointString =
                        UNIFIED_ENDPOINT_RESOURCE_URL
                                + ProfileApplication.getTenant()
                                + USERS_PATH_SECTION
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
