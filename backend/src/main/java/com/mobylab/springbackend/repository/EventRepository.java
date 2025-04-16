package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Event;
import com.mobylab.springbackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    Page<Event> findByOrganizer(User organizer, Pageable pageable);
    Page<Event> findByCategoryId(UUID categoryId, Pageable pageable);
    Page<Event> findByStartTimeAfter(LocalDateTime dateTime, Pageable pageable);
    Page<Event> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    List<Event> findTop5ByStartTimeAfterOrderByStartTimeAsc(LocalDateTime now);
} 