package com.williamhsieh.financeapp.notification.port;

public interface EmailSender {

    void sendVerificationEmail(
        String email,
        String verificationToken
    );
}
