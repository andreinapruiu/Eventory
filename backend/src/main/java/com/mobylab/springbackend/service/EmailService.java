package com.mobylab.springbackend.service;

import java.time.LocalDateTime;

public interface EmailService {
    void sendRegistrationConfirmation(String to, String username, String eventName, LocalDateTime eventTime);
} 