package com.mobylab.springbackend.service.impl;

import com.mobylab.springbackend.dto.EventDto;
import com.mobylab.springbackend.dto.PageResponseDto;
import com.mobylab.springbackend.dto.mapper.EntityMapper;
import com.mobylab.springbackend.entity.Event;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.ResourceNotFoundException;
import com.mobylab.springbackend.repository.EventRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EntityMapper mapper;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, 
                           UserRepository userRepository,
                           EntityMapper mapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public EventDto createEvent(EventDto eventDto, UUID organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + organizerId));

        Event event = mapper.toEntity(eventDto);
        event.setOrganizer(organizer);
        
        Event savedEvent = eventRepository.save(event);
        return mapper.toDto(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getEvent(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return mapper.toDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<EventDto> getAllEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        Page<Event> eventPage = eventRepository.findAll(pageable);
        
        List<EventDto> eventDtos = eventPage.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        
        return new PageResponseDto<>(eventDtos, eventPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<EventDto> getEventsByOrganizer(UUID organizerId, int page, int size) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + organizerId));
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        Page<Event> eventPage = eventRepository.findByOrganizer(organizer, pageable);
        
        List<EventDto> eventDtos = eventPage.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        
        return new PageResponseDto<>(eventDtos, eventPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<EventDto> getEventsByCategory(UUID categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        Page<Event> eventPage = eventRepository.findByCategoryId(categoryId, pageable);
        
        List<EventDto> eventDtos = eventPage.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        
        return new PageResponseDto<>(eventDtos, eventPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<EventDto> searchEvents(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        Page<Event> eventPage = eventRepository.findByTitleContainingIgnoreCase(query, pageable);
        
        List<EventDto> eventDtos = eventPage.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        
        return new PageResponseDto<>(eventDtos, eventPage);
    }

    @Override
    @Transactional
    public EventDto updateEvent(UUID id, EventDto eventDto) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        
        // Update fields
        existingEvent.setTitle(eventDto.getTitle());
        existingEvent.setDescription(eventDto.getDescription());
        existingEvent.setStartTime(eventDto.getStartTime());
        existingEvent.setEndTime(eventDto.getEndTime());
        existingEvent.setMaxAttendees(eventDto.getMaxAttendees());
        
        // Update location if provided
        if (eventDto.getLocation() != null) {
            if (existingEvent.getLocation() == null) {
                existingEvent.setLocation(mapper.toEntity(eventDto.getLocation()));
            } else {
                existingEvent.getLocation().setName(eventDto.getLocation().getName());
                existingEvent.getLocation().setAddress(eventDto.getLocation().getAddress());
                existingEvent.getLocation().setCity(eventDto.getLocation().getCity());
                existingEvent.getLocation().setPostalCode(eventDto.getLocation().getPostalCode());
                existingEvent.getLocation().setCountry(eventDto.getLocation().getCountry());
                existingEvent.getLocation().setLatitude(eventDto.getLocation().getLatitude());
                existingEvent.getLocation().setLongitude(eventDto.getLocation().getLongitude());
            }
        }
        
        Event updatedEvent = eventRepository.save(existingEvent);
        return mapper.toDto(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        
        eventRepository.delete(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> getUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> upcomingEvents = eventRepository.findTop5ByStartTimeAfterOrderByStartTimeAsc(now);
        
        return upcomingEvents.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
} 