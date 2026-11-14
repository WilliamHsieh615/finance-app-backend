package com.williamhsieh.financeapp.identity.verification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.verification.entity.UserSmsVerification;

public interface UserSmsVerificationRepository
    extends JpaRepository<UserSmsVerification, Long> {

    Optional<UserSmsVerification>
        findFirstByUser_IdAndPhoneAndVerifiedDateIsNullOrderByCreatedDateDesc(
            Long userId,
            String phone
        );

    Optional<UserSmsVerification>
        findFirstByPhoneAndVerifiedDateIsNullOrderByCreatedDateDesc(
            String phone
        );
}
