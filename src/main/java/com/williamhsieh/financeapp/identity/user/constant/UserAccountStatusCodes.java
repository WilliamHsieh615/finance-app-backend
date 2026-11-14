package com.williamhsieh.financeapp.identity.user.constant;

public final class UserAccountStatusCodes {

    public static final String
        PENDING_EMAIL_VERIFICATION =
            "PENDING_EMAIL_VERIFICATION";

    public static final String ACTIVE =
        "ACTIVE";

    public static final String SUSPENDED =
        "SUSPENDED";

    public static final String LOCKED =
        "LOCKED";

    public static final String DELETED =
        "DELETED";

    private UserAccountStatusCodes() {
    }
}
