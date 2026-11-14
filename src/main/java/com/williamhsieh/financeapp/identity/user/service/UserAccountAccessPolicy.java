package com.williamhsieh.financeapp.identity.user.service;

import org.springframework.stereotype.Component;

import com.williamhsieh.financeapp.identity.user.constant.UserAccountStatusCodes;
import com.williamhsieh.financeapp.identity.user.entity.User;
import com.williamhsieh.financeapp.identity.user.repository.UserRepository;

@Component
public class UserAccountAccessPolicy {

    private final UserRepository userRepository;

    public UserAccountAccessPolicy(
        UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public boolean isAvailable(
        User user
    ) {
        if (
            user == null ||
            user.getAccountStatus() == null
        ) {
            return false;
        }

        return user.isActive() &&
            user.getDeletedDate() == null &&
            UserAccountStatusCodes.ACTIVE.equals(
                user.getAccountStatus().getCode()
            );
    }

    public boolean isAvailable(
        Long userId
    ) {
        if (userId == null) {
            return false;
        }

        return userRepository
            .existsByIdAndActiveTrueAndDeletedDateIsNullAndAccountStatus_Code(
                userId,
                UserAccountStatusCodes.ACTIVE
            );
    }
}
