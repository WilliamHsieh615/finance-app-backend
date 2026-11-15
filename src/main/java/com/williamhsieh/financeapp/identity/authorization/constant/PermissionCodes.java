package com.williamhsieh.financeapp.identity.authorization.constant;

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

    /*
     * AUTHORIZATION
     */
    public static final String ROLE_READ =
        "ROLE_READ";

    public static final String ROLE_ASSIGN =
        "ROLE_ASSIGN";

    public static final String ROLE_CREATE =
        "ROLE_CREATE";

    public static final String ROLE_UPDATE =
        "ROLE_UPDATE";

    public static final String ROLE_DELETE =
        "ROLE_DELETE";

    public static final String PERMISSION_READ =
        "PERMISSION_READ";

    public static final String ROLE_PERMISSION_MANAGE =
        "ROLE_PERMISSION_MANAGE";

    public static final String PERMISSION_MANAGE =
        "PERMISSION_MANAGE";
}
