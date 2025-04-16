package com.mobylab.springbackend.dto;

import com.mobylab.springbackend.entity.Registration.RegistrationStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class RegistrationDto {
    private UUID id;

    @NotNull(message = "User ID is required")
    private UUID userId;
    
    private String username;

    @NotNull(message = "Event ID is required")
    private UUID eventId;
    
    private String eventTitle;
    
    private LocalDateTime eventStartTime;
    
    private LocalDateTime registrationTime;

    private RegistrationStatus status;

    private String notes;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }
    
    public String getEventTitle() {
        return eventTitle;
    }
    
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
    
    public LocalDateTime getEventStartTime() {
        return eventStartTime;
    }
    
    public void setEventStartTime(LocalDateTime eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
} 