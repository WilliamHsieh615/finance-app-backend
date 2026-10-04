package com.williamhsieh.financeapp.service.auth;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public record UserAuthorities(
    Set<String> roles,
    Set<String> permissions
) {

    public UserAuthorities {
        roles = Collections.unmodifiableSet(
            new LinkedHashSet<>(roles)
        );

        permissions = Collections.unmodifiableSet(
            new LinkedHashSet<>(permissions)
        );
    }
}
