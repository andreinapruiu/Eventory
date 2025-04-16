package com.mobylab.springbackend.service.impl;

import com.mobylab.springbackend.dto.PageResponseDto;
import com.mobylab.springbackend.dto.RegistrationDto;
import com.mobylab.springbackend.dto.mapper.EntityMapper;
import com.mobylab.springbackend.entity.Event;
import com.mobylab.springbackend.entity.Registration;
import com.mobylab.springbackend.entity.Registration.RegistrationStatus;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.exception.ResourceAlreadyExistsException;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.EventRepository;
import com.mobylab.springbackend.repository.RegistrationRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.EmailService;
import com.mobylab.springbackend.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EntityMapper mapper;
    private final EmailService emailService;

    @Autowired
    public RegistrationServiceImpl(RegistrationRepository registrationRepository,
                                  UserRepository userRepository,
                                  EventRepository eventRepository,
                                  EntityMapper mapper,
                                  EmailService emailService) {
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.mapper = mapper;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public RegistrationDto registerForEvent(UUID userId, UUID eventId, String notes) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        
        // Check if user is already registered
        if (registrationRepository.existsByUserAndEvent(user, event)) {
            throw new ResourceAlreadyExistsException("User is already registered for this event");
        }
        
        // Check if event has reached maximum capacity
        if (event.getMaxAttendees() != null) {
            long currentRegistrations = registrationRepository.countByEvent(event);
            if (currentRegistrations >= event.getMaxAttendees()) {
                throw new BadRequestException("Event has reached maximum capacity");
            }
        }
        
        // Create new registration
        Registration registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setStatus(RegistrationStatus.CONFIRMED);
        registration.setNotes(notes);
        
        Registration savedRegistration = registrationRepository.save(registration);
        
        // Send confirmation email
        emailService.sendRegistrationConfirmation(user.getEmail(), user.getUsername(), event.getTitle(), event.getStartTime());
        
        return mapper.toDto(savedRegistration);
    }

    @Override
    @Transactional(readOnly = true)
    public RegistrationDto getRegistration(UUID id) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with id: " + id));
        
        return mapper.toDto(registration);
    }

    @Override
    @Transactional(readOnly = true)
    public RegistrationDto getRegistrationByUserAndEvent(UUID userId, UUID eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        
        Registration registration = registrationRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found for user and event"));
        
        return mapper.toDto(registration);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<RegistrationDto> getUserRegistrations(UUID userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("registrationTime").descending());
        Page<Registration> registrationPage = registrationRepository.findByUser(user, pageable);
        
        List<RegistrationDto> registrationDtos = registrationPage.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        
        return new PageResponseDto<>(registrationDtos, registrationPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<RegistrationDto> getEventRegistrations(UUID eventId, int page, int size) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("registrationTime").descending());
        Page<Registration> registrationPage = registrationRepository.findByEvent(event, pageable);
        
        List<RegistrationDto> registrationDtos = registrationPage.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        
        return new PageResponseDto<>(registrationDtos, registrationPage);
    }

    @Override
    @Transactional
    public RegistrationDto updateRegistrationStatus(UUID id, RegistrationStatus status) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with id: " + id));
        
        registration.setStatus(status);
        Registration updatedRegistration = registrationRepository.save(registration);
        
        return mapper.toDto(updatedRegistration);
    }

    @Override
    @Transactional
    public void cancelRegistration(UUID id) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with id: " + id));
        
        registration.setStatus(RegistrationStatus.CANCELLED);
        registrationRepository.save(registration);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationDto> getRegistrationsByEventAndStatus(UUID eventId, RegistrationStatus status) {
        List<Registration> registrations = registrationRepository.findByEvent_IdAndStatus(eventId, status);
        
        return registrations.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserRegisteredForEvent(UUID userId, UUID eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        
        return registrationRepository.existsByUserAndEvent(user, event);
    }

    @Override
    @Transactional(readOnly = true)
    public long getEventRegistrationsCount(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        
        return registrationRepository.countByEvent(event);
    }
} 