package com.microsoft.office365.profile.util;

/**
 * Enumeration that lists the endpoints that the EndpointFactory supports
 * in the {@link EndpointFactory#getEndpoint(ProfileEndpoint)} method.
 */
public enum ProfileEndpoint {
    DIRECT_REPORTS,
    FILES,
    GROUPS,
    MANAGER,
    THUMBNAIL_PHOTO,
    USER_DETAILS,
    USER_DIRECTORY
}
