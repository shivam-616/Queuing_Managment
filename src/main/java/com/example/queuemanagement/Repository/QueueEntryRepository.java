package com.example.queuemanagement.Repository;

import com.example.queuemanagement.entites.QueueEntry;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {
    // Custom query methods can be defined here if needed
    // For example, to find all entries by a specific attribute
    Optional<QueueEntry> findByQueueNumber(int queueNumber);
    List<QueueEntry> findAllByQueueId(String queueId);
    List<QueueEntry> findByQueueIdAndStatus(String queueId, String status);
    // Add this new method
    @Transactional
    void deleteAllByQueueId(String queueId);
}
