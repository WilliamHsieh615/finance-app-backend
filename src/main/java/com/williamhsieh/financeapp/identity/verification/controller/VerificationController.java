package com.williamhsieh.financeapp.identity.verification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.williamhsieh.financeapp.identity.verification.dto.ConfirmEmailVerificationRequest;
import com.williamhsieh.financeapp.identity.verification.dto.ConfirmSmsVerificationRequest;
import com.williamhsieh.financeapp.identity.verification.dto.SendEmailVerificationRequest;
import com.williamhsieh.financeapp.identity.verification.dto.SendSmsVerificationRequest;
import com.williamhsieh.financeapp.identity.verification.dto.VerificationResponse;
import com.williamhsieh.financeapp.identity.verification.service.EmailVerificationService;
import com.williamhsieh.financeapp.identity.verification.service.SmsVerificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/verifications")
public class VerificationController {

    private final EmailVerificationService emailVerificationService;
    private final SmsVerificationService smsVerificationService;

    public VerificationController(
        EmailVerificationService emailVerificationService,
        SmsVerificationService smsVerificationService
    ) {
        this.emailVerificationService = emailVerificationService;
        this.smsVerificationService = smsVerificationService;
    }

    @PostMapping("/email/send")
    public ResponseEntity<Void> sendEmail(
        @Valid @RequestBody SendEmailVerificationRequest request
    ) {
        emailVerificationService.send(request.email());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email/confirm")
    public ResponseEntity<VerificationResponse> confirmEmail(
        @Valid @RequestBody ConfirmEmailVerificationRequest request
    ) {
        return ResponseEntity.ok(
            emailVerificationService.confirm(request.token())
        );
    }

    @PostMapping("/sms/send")
    public ResponseEntity<Void> sendSms(
        @Valid @RequestBody SendSmsVerificationRequest request
    ) {
        smsVerificationService.send(request.phone());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sms/confirm")
    public ResponseEntity<VerificationResponse> confirmSms(
        @Valid @RequestBody ConfirmSmsVerificationRequest request
    ) {
        return ResponseEntity.ok(
            smsVerificationService.confirm(
                request.phone(),
                request.code()
            )
        );
    }
}
