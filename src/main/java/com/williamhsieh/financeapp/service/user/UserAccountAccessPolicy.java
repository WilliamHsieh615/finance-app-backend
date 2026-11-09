package com.williamhsieh.financeapp.service.user;

import org.springframework.stereotype.Component;

import com.williamhsieh.financeapp.constant.user.UserAccountStatusCodes;
import com.williamhsieh.financeapp.entity.user.User;
import com.williamhsieh.financeapp.repository.user.UserRepository;

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
