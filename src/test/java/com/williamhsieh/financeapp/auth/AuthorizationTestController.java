package com.williamhsieh.financeapp.auth;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.williamhsieh.financeapp.constant.auth.PermissionCodes;

@RestController
public class AuthorizationTestController {

    @GetMapping(
        "/api/v1/test/authorization/user-read"
    )
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.USER_READ +
        "')"
    )
    public Map<String, String> userRead() {
        return Map.of(
            "result",
            "allowed"
        );
    }

    @PreAuthorize(
        "hasRole('SUPPORT')"
    )
    @GetMapping(
        "/api/v1/test/authorization/support"
    )
    public Map<String, String> supportOnly() {
        return Map.of(
            "result",
            "support-allowed"
        );
    }
}
