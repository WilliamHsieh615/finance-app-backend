package com.williamhsieh.financeapp.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.williamhsieh.financeapp.service.user.UserAccountAccessPolicy;
import com.williamhsieh.financeapp.service.auth.UserSessionAccessPolicy;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ActiveAccountFilter
    extends OncePerRequestFilter {

    private final UserAccountAccessPolicy
        userAccountAccessPolicy;
    
    private final UserSessionAccessPolicy
        userSessionAccessPolicy;

    public ActiveAccountFilter(
        UserAccountAccessPolicy userAccountAccessPolicy,
        UserSessionAccessPolicy userSessionAccessPolicy
    ) {
        this.userAccountAccessPolicy =
            userAccountAccessPolicy;
        this.userSessionAccessPolicy =
            userSessionAccessPolicy;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        Authentication authentication =
            SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (
            authentication
                instanceof JwtAuthenticationToken jwtAuthentication &&
            authentication.isAuthenticated()
        ) {
            Long userId =
                parseUserId(
                    jwtAuthentication
                        .getToken()
                        .getSubject()
                );

            String sessionId =
                jwtAuthentication
                    .getToken()
                    .getClaimAsString("sid");

            boolean accountAvailable =
                userAccountAccessPolicy
                    .isAvailable(userId);

            if (!accountAvailable) {
                throw new AccessDeniedException(
                    "會員帳號目前無法使用"
                );
            }

            boolean sessionAvailable =
                userSessionAccessPolicy
                    .isAvailable(
                        userId,
                        sessionId
                    );

            if (!sessionAvailable) {
                throw new AccessDeniedException(
                    "登入 Session 已失效"
                );
            }
        }

        filterChain.doFilter(
            request,
            response
        );
    }

    private Long parseUserId(
        String subject
    ) {
        if (subject == null || subject.isBlank()) {
            throw new AccessDeniedException(
                "Access Token 的使用者資訊無效"
            );
        }

        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new AccessDeniedException(
                "Access Token 的使用者資訊無效",
                exception
            );
        }
    }
}