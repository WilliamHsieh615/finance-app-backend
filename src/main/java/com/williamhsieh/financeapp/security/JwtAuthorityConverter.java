package com.williamhsieh.financeapp.security;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthorityConverter
    implements Converter<
        Jwt,
        AbstractAuthenticationToken
    > {

    private final JwtGrantedAuthoritiesConverter
        roleConverter;

    private final JwtGrantedAuthoritiesConverter
        permissionConverter;

    public JwtAuthorityConverter() {
        roleConverter =
            new JwtGrantedAuthoritiesConverter();

        roleConverter.setAuthoritiesClaimName(
            "roles"
        );

        roleConverter.setAuthorityPrefix(
            "ROLE_"
        );

        permissionConverter =
            new JwtGrantedAuthoritiesConverter();

        permissionConverter.setAuthoritiesClaimName(
            "permissions"
        );

        permissionConverter.setAuthorityPrefix("");
    }

    @Override
    public AbstractAuthenticationToken convert(
        Jwt jwt
    ) {
        Set<GrantedAuthority> authorities =
            new LinkedHashSet<>();

        authorities.addAll(
            roleConverter.convert(jwt)
        );

        authorities.addAll(
            permissionConverter.convert(jwt)
        );

        return new JwtAuthenticationToken(
            jwt,
            authorities,
            jwt.getSubject()
        );
    }
}
