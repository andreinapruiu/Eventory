package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Event;
import com.mobylab.springbackend.entity.Registration;
import com.mobylab.springbackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, UUID> {
    Page<Registration> findByUser(User user, Pageable pageable);
    Page<Registration> findByEvent(Event event, Pageable pageable);
    Optional<Registration> findByUserAndEvent(User user, Event event);
    boolean existsByUserAndEvent(User user, Event event);
    long countByEvent(Event event);
    List<Registration> findByEvent_IdAndStatus(UUID eventId, Registration.RegistrationStatus status);
} 