package com.mobylab.springbackend.service.impl;

import com.mobylab.springbackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendRegistrationConfirmation(String to, String username, String eventName, LocalDateTime eventTime) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Event Registration Confirmation: " + eventName);
        
        String body = String.format(
                "Hello %s,\n\n" +
                "Thank you for registering for the event: %s\n" +
                "Date and Time: %s\n\n" +
                "We look forward to seeing you there!\n\n" +
                "Best regards,\n" +
                "The Eventory Team",
                username, eventName, eventTime.format(DATE_FORMATTER));
        
        message.setText(body);
        mailSender.send(message);
    }
} 