package com.example.queuemanagement.services;

import com.example.queuemanagement.Controller.QueueWebSocketController;
import com.example.queuemanagement.Repository.QueueEntryRepository;
import com.example.queuemanagement.entites.QueueEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueueEntryService {
    private final QueueEntryRepository repository;
    private final QueueWebSocketController webSocketController;

    public QueueEntry createEntry(QueueEntry entry) {
        return repository.save(entry);
    }

    public List<QueueEntry> getAllEntries() {
        return repository.findAll();
    }

    public Optional<QueueEntry> getById(Long id) {
        return repository.findById(id);
    }

    public Optional<QueueEntry> getByQueueNumber(int queueNumber) {
        return repository.findByQueueNumber(queueNumber);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public QueueEntry updateStatus(Long id, String status) {
        QueueEntry entry = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        entry.setStatus(status);
        QueueEntry savedEntry = repository.save(entry);
        webSocketController.broadcastQueueUpdate(savedEntry.getQueueId());
        return savedEntry;
    }

    public Optional<QueueEntry> getNextWaitingEntry(String queueId) {
        return repository.findAllByQueueId(queueId).stream()
                .filter(e -> "waiting".equalsIgnoreCase(e.getStatus()))
                .min(Comparator.comparingInt(QueueEntry::getQueueNumber));
    }

    public QueueEntry createNewEntry(String queueId, Map<String, Object> details) {
        QueueEntry entry = new QueueEntry();
        entry.setQueueId(queueId);
        entry.setDetails(details);
        entry.setStatus("waiting");
        entry.setQueueNumber(calculateNextQueueNumber(queueId));
        QueueEntry savedEntry = repository.save(entry);

        webSocketController.broadcastQueueUpdate(queueId);

        return savedEntry;
    }


    public int calculateNextQueueNumber(String queueId) {
        return repository.findAllByQueueId(queueId).stream()
                .mapToInt(QueueEntry::getQueueNumber)
                .max()
                .orElse(0) + 1;
    }
    public List<QueueEntry> getAllEntriesByQueueId(String queueId) {
        return repository.findAllByQueueId(queueId);
    }
}
