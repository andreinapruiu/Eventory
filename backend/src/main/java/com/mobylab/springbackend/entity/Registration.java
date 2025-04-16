package com.mobylab.springbackend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "registrations", schema = "project")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "registration_time", nullable = false)
    private LocalDateTime registrationTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    @Column(name = "notes")
    private String notes;

    @PrePersist
    protected void onCreate() {
        registrationTime = LocalDateTime.now();
        if (status == null) {
            status = RegistrationStatus.PENDING;
        }
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public Registration setId(UUID id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Registration setUser(User user) {
        this.user = user;
        return this;
    }

    public Event getEvent() {
        return event;
    }

    public Registration setEvent(Event event) {
        this.event = event;
        return this;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public Registration setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
        return this;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public Registration setStatus(RegistrationStatus status) {
        this.status = status;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public Registration setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public enum RegistrationStatus {
        PENDING, CONFIRMED, CANCELLED, ATTENDED
    }
} 