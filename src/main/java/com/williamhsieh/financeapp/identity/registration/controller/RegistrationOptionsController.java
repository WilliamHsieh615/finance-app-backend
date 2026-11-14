package com.williamhsieh.financeapp.identity.registration.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.williamhsieh.financeapp.identity.registration.dto.RegistrationOptionsResponse;
import com.williamhsieh.financeapp.identity.registration.service.RegistrationOptionsService;

@RestController
@RequestMapping("/api/v1/registration")
public class RegistrationOptionsController {

    private final RegistrationOptionsService registrationOptionsService;

    public RegistrationOptionsController(
        RegistrationOptionsService registrationOptionsService
    ) {
        this.registrationOptionsService = registrationOptionsService;
    }

    @GetMapping("/options")
    public RegistrationOptionsResponse getOptions() {
        return registrationOptionsService.getOptions();
    }
}
