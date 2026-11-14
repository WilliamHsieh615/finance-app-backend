package com.williamhsieh.financeapp.identity.authentication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.authentication.entity.UserRefreshToken;

public interface UserRefreshTokenRepository
    extends JpaRepository<UserRefreshToken, Long> {

    Optional<UserRefreshToken> findByTokenHash(
        String tokenHash
    );

    List<UserRefreshToken>
        findAllByUser_IdAndRevokedDateIsNull(
            Long userId
        );
}
