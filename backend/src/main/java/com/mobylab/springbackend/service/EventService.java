package com.mobylab.springbackend.service;

import com.mobylab.springbackend.dto.EventDto;
import com.mobylab.springbackend.dto.PageResponseDto;

import java.util.List;
import java.util.UUID;

public interface EventService {
    EventDto createEvent(EventDto eventDto, UUID organizerId);
    EventDto getEvent(UUID id);
    PageResponseDto<EventDto> getAllEvents(int page, int size);
    PageResponseDto<EventDto> getEventsByOrganizer(UUID organizerId, int page, int size);
    PageResponseDto<EventDto> getEventsByCategory(UUID categoryId, int page, int size);
    PageResponseDto<EventDto> searchEvents(String query, int page, int size);
    EventDto updateEvent(UUID id, EventDto eventDto);
    void deleteEvent(UUID id);
    List<EventDto> getUpcomingEvents();
} 