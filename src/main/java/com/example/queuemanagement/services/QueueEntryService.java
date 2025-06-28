package com.example.queuemanagement.services;

import com.example.queuemanagement.Repository.QueueEntryRepository;
import com.example.queuemanagement.entites.QueueEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueueEntryService {
    private final QueueEntryRepository repository;

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
    }  public QueueEntry updateStatus(Long id, String status) {
        QueueEntry entry = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        entry.setStatus(status);
        return repository.save(entry);
    }

    public Optional<QueueEntry> getNextWaitingEntry() {
        return repository.findAll().stream()
                .filter(e -> "waiting".equalsIgnoreCase(e.getStatus()))
                .sorted((a, b) -> Integer.compare(a.getQueueNumber(), b.getQueueNumber()))
                .findFirst();
    }
    public QueueEntry createNewEntry(String name) {
        QueueEntry entry = new QueueEntry();
        entry.setName(name);
        entry.setStatus("waiting");
        entry.setQueueNumber(calculateNextQueueNumber());
        return repository.save(entry);
    }
    public int calculateNextQueueNumber() {
        List<QueueEntry> entries = repository.findAll();
        if (entries.isEmpty()) {
            return 1; // Start from 1 if no entries exist
        }
        return entries.stream()
                .mapToInt(QueueEntry::getQueueNumber)
                .max()
                .orElse(0) + 1; // Increment the highest queue number
    }
}
