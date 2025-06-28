package com.example.queuemanagement.Repository;

import com.example.queuemanagement.entites.QueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {
    // Custom query methods can be defined here if needed
    // For example, to find all entries by a specific attribute
    Optional<QueueEntry> findByQueueNumber(int queueNumber);
}
