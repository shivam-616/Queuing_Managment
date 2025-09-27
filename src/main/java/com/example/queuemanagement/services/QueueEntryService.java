package com.example.queuemanagement.services;

import com.example.queuemanagement.Controller.QueueWebSocketController;
import com.example.queuemanagement.DTO.AnalyticsDTO;
import com.example.queuemanagement.DTO.DashboardAnalyticsDTO;
import com.example.queuemanagement.Repository.QueueEntryRepository;
import com.example.queuemanagement.entites.QueueEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueueEntryService {

    private final QueueEntryRepository repository;
    private final @Lazy QueueWebSocketController webSocketController;

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

        // Set calledAt timestamp when status becomes "called"
        if ("called".equalsIgnoreCase(status)) {
            entry.setCalledAt(LocalDateTime.now());
        }

        QueueEntry savedEntry = repository.save(entry);

        // Keep live admin page updated
        webSocketController.broadcastQueueUpdate(savedEntry.getQueueId());
        return savedEntry;
    }

    public Optional<QueueEntry> getNextWaitingEntry(String queueId) {
        return repository.findAllByQueueId(queueId).stream()
                .filter(e -> "waiting".equalsIgnoreCase(e.getStatus()))
                .min(Comparator.comparingInt(QueueEntry::getQueueNumber));
    }

    public QueueEntry createNewEntry(String queueId, Map<String, Object> details) {
        // Prevent duplicate waiting entries with same details
        List<QueueEntry> waitingEntries = repository.findByQueueIdAndStatus(queueId, "waiting");
        boolean isDuplicate = waitingEntries.stream()
                .anyMatch(entry -> entry.getDetails().equals(details));
        if (isDuplicate) {
            throw new IllegalStateException("You are already in this queue.");
        }

        QueueEntry entry = new QueueEntry();
        entry.setQueueId(queueId);
        entry.setDetails(details);
        entry.setStatus("waiting");
        entry.setQueueNumber(calculateNextQueueNumber(queueId));
        QueueEntry savedEntry = repository.save(entry);

        // Keep live admin page updated
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

    public void clearQueue(String queueId) {
        repository.deleteAllByQueueId(queueId);
        webSocketController.broadcastQueueUpdate(queueId); // Notify clients
    }

    /**
     * Basic analytics across all records for this queue.
     */
    public AnalyticsDTO getAnalytics(String queueId) {
        List<QueueEntry> allEntries = repository.findAllByQueueId(queueId);
        List<QueueEntry> servedEntries = allEntries.stream()
                .filter(e -> "called".equalsIgnoreCase(e.getStatus()) && e.getCalledAt() != null && e.getCreatedAt() != null)
                .sorted(Comparator.comparing(QueueEntry::getCalledAt))
                .collect(Collectors.toList());

        long avgWaitTime = (long) servedEntries.stream()
                .mapToLong(e -> Duration.between(e.getCreatedAt(), e.getCalledAt()).toMinutes())
                .average().orElse(-1);

        long avgServiceTime = -1;
        if (servedEntries.size() > 1) {
            long totalServiceDuration = 0;
            for (int i = 0; i < servedEntries.size() - 1; i++) {
                totalServiceDuration += Duration.between(servedEntries.get(i).getCalledAt(), servedEntries.get(i + 1).getCalledAt()).toMinutes();
            }
            avgServiceTime = totalServiceDuration / (servedEntries.size() - 1);
        }

        long totalServedToday = allEntries.stream()
                .filter(e -> "called".equalsIgnoreCase(e.getStatus()) && e.getCreatedAt() != null && e.getCreatedAt().toLocalDate().isEqual(LocalDate.now()))
                .count();

        Map<Integer, Long> trafficByHour = allEntries.stream()
                .filter(e -> e.getCreatedAt() != null && e.getCreatedAt().toLocalDate().isEqual(LocalDate.now()))
                .collect(Collectors.groupingBy(e -> e.getCreatedAt().getHour(), Collectors.counting()));

        return new AnalyticsDTO(avgWaitTime, avgServiceTime, totalServedToday, trafficByHour);
    }

    /**
     * Dashboard analytics intended for the business/admin dashboard.
     * This computes analytics for today's entries and returns a DashboardAnalyticsDTO.
     * This method does not depend on any undefined variable and will not break the live-update flow.
     */
    public DashboardAnalyticsDTO getDashboardAnalytics(String queueId) {
        // Get today's entries for this queue
        List<QueueEntry> todayEntries = repository.findAllByQueueId(queueId).stream()
                .filter(e -> e.getCreatedAt() != null && e.getCreatedAt().toLocalDate().isEqual(LocalDate.now()))
                .collect(Collectors.toList());

        // Traffic by hour (today)
        Map<Integer, Long> trafficByHour = todayEntries.stream()
                .collect(Collectors.groupingBy(e -> e.getCreatedAt().getHour(), Collectors.counting()));

        // Determine peak hour (safely handle empty map)
        String peakHour = "N/A";
        if (trafficByHour != null && !trafficByHour.isEmpty()) {
            Optional<Map.Entry<Integer, Long>> peakEntry = trafficByHour.entrySet().stream()
                    .max(Map.Entry.comparingByValue());
            if (peakEntry.isPresent()) {
                int hour24 = peakEntry.get().getKey();
                String amPm = hour24 < 12 ? "AM" : "PM";
                int hour12 = hour24 % 12;
                if (hour12 == 0) hour12 = 12;
                peakHour = hour12 + ":00 " + amPm;
            }
        }

        // Compute avg wait time (minutes) for today's served entries
        List<QueueEntry> servedToday = todayEntries.stream()
                .filter(e -> "called".equalsIgnoreCase(e.getStatus()) && e.getCalledAt() != null && e.getCreatedAt() != null)
                .sorted(Comparator.comparing(QueueEntry::getCalledAt))
                .collect(Collectors.toList());

        long avgWaitTime = (long) servedToday.stream()
                .mapToLong(e -> Duration.between(e.getCreatedAt(), e.getCalledAt()).toMinutes())
                .average().orElse(-1);

        // Compute avg service time (minutes) based on calledAt gaps for today's served entries
        long avgServiceTime = -1;
        if (servedToday.size() > 1) {
            long totalServiceDuration = 0;
            for (int i = 0; i < servedToday.size() - 1; i++) {
                totalServiceDuration += Duration.between(servedToday.get(i).getCalledAt(), servedToday.get(i + 1).getCalledAt()).toMinutes();
            }
            avgServiceTime = totalServiceDuration / (servedToday.size() - 1);
        }

        // Last 5 recent visitors (today)
        List<QueueEntry> recentVisitors = todayEntries.stream()
                .sorted(Comparator.comparing(QueueEntry::getCreatedAt).reversed())
                .limit(5)
                .collect(Collectors.toList());

        return new DashboardAnalyticsDTO(
                todayEntries.size(),
                peakHour,
                avgWaitTime,
                avgServiceTime,
                trafficByHour,
                recentVisitors
        );
    }
}
