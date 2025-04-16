package com.mobylab.springbackend.service;

import com.mobylab.springbackend.dto.PageResponseDto;
import com.mobylab.springbackend.dto.RegistrationDto;
import com.mobylab.springbackend.entity.Registration.RegistrationStatus;

import java.util.List;
import java.util.UUID;

public interface RegistrationService {
    RegistrationDto registerForEvent(UUID userId, UUID eventId, String notes);
    RegistrationDto getRegistration(UUID id);
    RegistrationDto getRegistrationByUserAndEvent(UUID userId, UUID eventId);
    PageResponseDto<RegistrationDto> getUserRegistrations(UUID userId, int page, int size);
    PageResponseDto<RegistrationDto> getEventRegistrations(UUID eventId, int page, int size);
    RegistrationDto updateRegistrationStatus(UUID id, RegistrationStatus status);
    void cancelRegistration(UUID id);
    List<RegistrationDto> getRegistrationsByEventAndStatus(UUID eventId, RegistrationStatus status);
    boolean isUserRegisteredForEvent(UUID userId, UUID eventId);
    long getEventRegistrationsCount(UUID eventId);
} 