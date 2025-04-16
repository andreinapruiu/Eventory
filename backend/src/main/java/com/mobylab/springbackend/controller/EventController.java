package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.dto.EventDto;
import com.mobylab.springbackend.dto.PageResponseDto;
import com.mobylab.springbackend.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Event Management", description = "APIs for managing events within the Eventory platform")
@SecurityRequirement(name = "bearerAuth")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    @Operation(
        summary = "Create a new event", 
        description = "Create a new event with the current user as the organizer"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Event created successfully",
                     content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "403", description = "Forbidden - User does not have organizer privileges")
    })
    public ResponseEntity<EventDto> createEvent(
            @Parameter(description = "Event data", required = true) @Valid @RequestBody EventDto eventDto,
            @Parameter(description = "User ID of the organizer", required = true) @RequestHeader("userId") UUID organizerId) {
        EventDto createdEvent = eventService.createEvent(eventDto, organizerId);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get an event by ID", 
        description = "Retrieve detailed information about a specific event"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Event found",
                     content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<EventDto> getEvent(
            @Parameter(description = "Event ID", required = true) @PathVariable UUID id) {
        EventDto event = eventService.getEvent(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping
    @Operation(
        summary = "Get all events with pagination", 
        description = "Retrieve a paginated list of all events"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    public ResponseEntity<PageResponseDto<EventDto>> getAllEvents(
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of events per page") @RequestParam(defaultValue = "10") int size) {
        PageResponseDto<EventDto> events = eventService.getAllEvents(page, size);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/organizer/{organizerId}")
    @Operation(
        summary = "Get events by organizer", 
        description = "Retrieve all events organized by a specific user"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404", description = "Organizer not found")
    })
    public ResponseEntity<PageResponseDto<EventDto>> getEventsByOrganizer(
            @Parameter(description = "Organizer's user ID", required = true) @PathVariable UUID organizerId,
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of events per page") @RequestParam(defaultValue = "10") int size) {
        PageResponseDto<EventDto> events = eventService.getEventsByOrganizer(organizerId, page, size);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(
        summary = "Get events by category", 
        description = "Retrieve all events in a specific category"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<PageResponseDto<EventDto>> getEventsByCategory(
            @Parameter(description = "Category ID", required = true) @PathVariable UUID categoryId,
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of events per page") @RequestParam(defaultValue = "10") int size) {
        PageResponseDto<EventDto> events = eventService.getEventsByCategory(categoryId, page, size);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/search")
    @Operation(
        summary = "Search events", 
        description = "Search events by title (case-insensitive partial match)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    public ResponseEntity<PageResponseDto<EventDto>> searchEvents(
            @Parameter(description = "Search text", required = true) @RequestParam String query,
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of events per page") @RequestParam(defaultValue = "10") int size) {
        PageResponseDto<EventDto> events = eventService.searchEvents(query, page, size);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/upcoming")
    @Operation(
        summary = "Get upcoming events", 
        description = "Retrieve the top 5 upcoming events"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    public ResponseEntity<List<EventDto>> getUpcomingEvents() {
        List<EventDto> events = eventService.getUpcomingEvents();
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    @Operation(
        summary = "Update an event", 
        description = "Update an existing event (organizers can only update their own events, admins can update any)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Event updated successfully",
                     content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "403", description = "Forbidden - User does not have permission to update this event"),
        @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<EventDto> updateEvent(
            @Parameter(description = "Event ID", required = true) @PathVariable UUID id,
            @Parameter(description = "Updated event data", required = true) @Valid @RequestBody EventDto eventDto) {
        EventDto updatedEvent = eventService.updateEvent(id, eventDto);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    @Operation(
        summary = "Delete an event", 
        description = "Delete an event by its ID (organizers can only delete their own events, admins can delete any)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Event deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "403", description = "Forbidden - User does not have permission to delete this event"),
        @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<Void> deleteEvent(
            @Parameter(description = "Event ID", required = true) @PathVariable UUID id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
} 