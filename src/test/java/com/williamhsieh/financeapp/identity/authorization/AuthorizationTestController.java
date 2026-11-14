package com.williamhsieh.financeapp.identity.authorization;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;

@RestController
@RequestMapping("/api/v1/test/authorization")
public class AuthorizationTestController {

    @GetMapping("/user-read")
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

    @GetMapping("/support")
    @PreAuthorize(
        "hasRole('SUPPORT')"
    )
    public Map<String, String> supportOnly() {
        return Map.of(
            "result",
            "support-allowed"
        );
    }
}
