package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.dto.PageResponseDto;
import com.mobylab.springbackend.dto.RegistrationDto;
import com.mobylab.springbackend.entity.Registration.RegistrationStatus;
import com.mobylab.springbackend.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/registrations")
@Tag(name = "Registration Management", description = "APIs for event registration management")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/user/{userId}/event/{eventId}")
    @Operation(summary = "Register for an event", description = "Register a user for an event")
    public ResponseEntity<RegistrationDto> registerForEvent(
            @PathVariable UUID userId,
            @PathVariable UUID eventId,
            @RequestParam(required = false) String notes) {
        RegistrationDto registration = registrationService.registerForEvent(userId, eventId, notes);
        return new ResponseEntity<>(registration, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a registration by ID", description = "Retrieve a registration by its ID")
    public ResponseEntity<RegistrationDto> getRegistration(@PathVariable UUID id) {
        RegistrationDto registration = registrationService.getRegistration(id);
        return ResponseEntity.ok(registration);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get registrations by user", description = "Retrieve all registrations for a specific user")
    public ResponseEntity<PageResponseDto<RegistrationDto>> getUserRegistrations(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponseDto<RegistrationDto> registrations = registrationService.getUserRegistrations(userId, page, size);
        return ResponseEntity.ok(registrations);
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    @Operation(summary = "Get registrations by event", description = "Retrieve all registrations for a specific event")
    public ResponseEntity<PageResponseDto<RegistrationDto>> getEventRegistrations(
            @PathVariable UUID eventId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponseDto<RegistrationDto> registrations = registrationService.getEventRegistrations(eventId, page, size);
        return ResponseEntity.ok(registrations);
    }

    @GetMapping("/event/{eventId}/status/{status}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    @Operation(summary = "Get registrations by event and status", description = "Retrieve all registrations for a specific event with a specific status")
    public ResponseEntity<List<RegistrationDto>> getRegistrationsByEventAndStatus(
            @PathVariable UUID eventId,
            @PathVariable RegistrationStatus status) {
        List<RegistrationDto> registrations = registrationService.getRegistrationsByEventAndStatus(eventId, status);
        return ResponseEntity.ok(registrations);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    @Operation(summary = "Update registration status", description = "Update the status of a registration")
    public ResponseEntity<RegistrationDto> updateRegistrationStatus(
            @PathVariable UUID id,
            @RequestParam RegistrationStatus status) {
        RegistrationDto updatedRegistration = registrationService.updateRegistrationStatus(id, status);
        return ResponseEntity.ok(updatedRegistration);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel a registration", description = "Cancel a registration by its ID")
    public ResponseEntity<Void> cancelRegistration(@PathVariable UUID id) {
        registrationService.cancelRegistration(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    @Operation(summary = "Check if a user is registered for an event", description = "Check if a user is already registered for a specific event")
    public ResponseEntity<Boolean> isUserRegisteredForEvent(
            @RequestParam UUID userId,
            @RequestParam UUID eventId) {
        boolean isRegistered = registrationService.isUserRegisteredForEvent(userId, eventId);
        return ResponseEntity.ok(isRegistered);
    }

    @GetMapping("/event/{eventId}/count")
    @Operation(summary = "Get registration count for an event", description = "Get the total number of registrations for a specific event")
    public ResponseEntity<Long> getEventRegistrationsCount(@PathVariable UUID eventId) {
        long count = registrationService.getEventRegistrationsCount(eventId);
        return ResponseEntity.ok(count);
    }
} 