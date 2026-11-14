package com.williamhsieh.financeapp.identity.verification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.verification.entity.UserEmailVerification;

public interface UserEmailVerificationRepository
    extends JpaRepository<UserEmailVerification, Long> {

    Optional<UserEmailVerification>
        findByTokenHashAndVerifiedDateIsNull(String tokenHash);

    Optional<UserEmailVerification>
        findFirstByUser_IdAndVerifiedDateIsNullOrderByCreatedDateDesc(
            Long userId
        );
}