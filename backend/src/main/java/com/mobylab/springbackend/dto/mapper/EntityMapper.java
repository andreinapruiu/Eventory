package com.mobylab.springbackend.dto.mapper;

import com.mobylab.springbackend.dto.*;
import com.mobylab.springbackend.entity.*;
import com.mobylab.springbackend.repository.CategoryRepository;
import com.mobylab.springbackend.repository.RegistrationRepository;
import com.mobylab.springbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RegistrationRepository registrationRepository;

    @Autowired
    public EntityMapper(UserRepository userRepository, 
                        CategoryRepository categoryRepository, 
                        RegistrationRepository registrationRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.registrationRepository = registrationRepository;
    }

    // Event Mappings
    public EventDto toDto(Event event) {
        if (event == null) {
            return null;
        }

        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setMaxAttendees(event.getMaxAttendees());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setUpdatedAt(event.getUpdatedAt());
        
        if (event.getOrganizer() != null) {
            dto.setOrganizerId(event.getOrganizer().getId());
            dto.setOrganizerName(event.getOrganizer().getUsername());
        }
        
        if (event.getCategory() != null) {
            dto.setCategoryId(event.getCategory().getId());
            dto.setCategoryName(event.getCategory().getName());
        }
        
        if (event.getLocation() != null) {
            dto.setLocation(toDto(event.getLocation()));
        }
        
        Long registrationsCount = registrationRepository.countByEvent(event);
        dto.setRegistrationsCount(registrationsCount);
        
        return dto;
    }

    public Event toEntity(EventDto dto) {
        if (dto == null) {
            return null;
        }

        Event event = new Event();
        
        if (dto.getId() != null) {
            event.setId(dto.getId());
        }
        
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setMaxAttendees(dto.getMaxAttendees());
        
        if (dto.getOrganizerId() != null) {
            userRepository.findById(dto.getOrganizerId())
                    .ifPresent(event::setOrganizer);
        }
        
        if (dto.getCategoryId() != null) {
            categoryRepository.findById(dto.getCategoryId())
                    .ifPresent(event::setCategory);
        }
        
        if (dto.getLocation() != null) {
            event.setLocation(toEntity(dto.getLocation()));
        }
        
        return event;
    }

    // Location Mappings
    public LocationDto toDto(Location location) {
        if (location == null) {
            return null;
        }

        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setAddress(location.getAddress());
        dto.setCity(location.getCity());
        dto.setPostalCode(location.getPostalCode());
        dto.setCountry(location.getCountry());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        
        return dto;
    }

    public Location toEntity(LocationDto dto) {
        if (dto == null) {
            return null;
        }

        Location location = new Location();
        
        if (dto.getId() != null) {
            location.setId(dto.getId());
        }
        
        location.setName(dto.getName());
        location.setAddress(dto.getAddress());
        location.setCity(dto.getCity());
        location.setPostalCode(dto.getPostalCode());
        location.setCountry(dto.getCountry());
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        
        return location;
    }

    // Category Mappings
    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setEventsCount((long) category.getEvents().size());
        
        return dto;
    }

    public Category toEntity(CategoryDto dto) {
        if (dto == null) {
            return null;
        }

        Category category = new Category();
        
        if (dto.getId() != null) {
            category.setId(dto.getId());
        }
        
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        
        return category;
    }

    // Registration Mappings
    public RegistrationDto toDto(Registration registration) {
        if (registration == null) {
            return null;
        }

        RegistrationDto dto = new RegistrationDto();
        dto.setId(registration.getId());
        dto.setRegistrationTime(registration.getRegistrationTime());
        dto.setStatus(registration.getStatus());
        dto.setNotes(registration.getNotes());
        
        if (registration.getUser() != null) {
            dto.setUserId(registration.getUser().getId());
            dto.setUsername(registration.getUser().getUsername());
        }
        
        if (registration.getEvent() != null) {
            dto.setEventId(registration.getEvent().getId());
            dto.setEventTitle(registration.getEvent().getTitle());
            dto.setEventStartTime(registration.getEvent().getStartTime());
        }
        
        return dto;
    }

    public Registration toEntity(RegistrationDto dto) {
        if (dto == null) {
            return null;
        }

        Registration registration = new Registration();
        
        if (dto.getId() != null) {
            registration.setId(dto.getId());
        }
        
        registration.setNotes(dto.getNotes());
        registration.setStatus(dto.getStatus());
        
        if (dto.getUserId() != null) {
            userRepository.findById(dto.getUserId())
                    .ifPresent(registration::setUser);
        }
        
        return registration;
    }

    // List Mappings
    public <S, T> List<T> mapList(List<S> source, java.util.function.Function<S, T> mapper) {
        return source.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
} 