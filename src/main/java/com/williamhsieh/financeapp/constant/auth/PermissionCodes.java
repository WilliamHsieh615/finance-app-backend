package com.williamhsieh.financeapp.constant.auth;

public final class PermissionCodes {

    private PermissionCodes() {
    }

    /*
     * USER
     */
    public static final String PROFILE_READ_SELF =
        "PROFILE_READ_SELF";

    public static final String PROFILE_UPDATE_SELF =
        "PROFILE_UPDATE_SELF";

    /*
     * USER_MANAGEMENT
     */
    public static final String USER_READ =
        "USER_READ";

    public static final String USER_STATUS_UPDATE =
        "USER_STATUS_UPDATE";

    public static final String USER_DELETE =
        "USER_DELETE";

    public static final String USER_READ_DELETED =
        "USER_READ_DELETED";

    public static final String USER_RESTORE =
        "USER_RESTORE";
}
