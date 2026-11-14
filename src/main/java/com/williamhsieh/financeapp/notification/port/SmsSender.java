package com.williamhsieh.financeapp.notification.port;

public interface SmsSender {

    void sendVerificationCode(
        String phone,
        String verificationCode
    );
}
