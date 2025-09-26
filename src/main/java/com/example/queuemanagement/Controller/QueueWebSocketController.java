// In src/main/java/com/example/queuemanagement/Controller/QueueWebSocketController.java
package com.example.queuemanagement.Controller;

import com.example.queuemanagement.entites.QueueEntry;
import com.example.queuemanagement.services.QueueEntryService;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class QueueWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final QueueEntryService queueEntryService;

    // A simple DTO (Data Transfer Object) to send queue data and wait time together
    public static class QueueUpdate {
        public List<QueueEntry> entries;
        public long estimatedWaitTime; // in minutes

        public QueueUpdate(List<QueueEntry> entries, long estimatedWaitTime) {
            this.entries = entries;
            this.estimatedWaitTime = estimatedWaitTime;
        }
    }

    public QueueWebSocketController(SimpMessagingTemplate messagingTemplate, @Lazy QueueEntryService queueEntryService) {
        this.messagingTemplate = messagingTemplate;
        this.queueEntryService = queueEntryService;
    }

    public void broadcastQueueUpdate(String queueId) {
        List<QueueEntry> allEntries = queueEntryService.getAllEntriesByQueueId(queueId);
        allEntries.sort(Comparator.comparingInt(QueueEntry::getQueueNumber));

        long estimatedWaitTime = calculateEstimatedWaitTime(allEntries);

        QueueUpdate update = new QueueUpdate(allEntries, estimatedWaitTime);
        messagingTemplate.convertAndSend("/topic/queue/" + queueId, update);
    }

    private long calculateEstimatedWaitTime(List<QueueEntry> entries) {
        // 1. Get the list of users who have already been called and have timestamps
        List<QueueEntry> calledEntries = entries.stream()
                .filter(e -> "called".equalsIgnoreCase(e.getStatus()) && e.getCalledAt() != null)
                .sorted(Comparator.comparing(QueueEntry::getCalledAt))
                .collect(Collectors.toList());

        if (calledEntries.size() < 2) {
            return -1; // Not enough data to estimate
        }

        // 2. Calculate the time difference between each consecutive call
        List<Long> serviceTimes = new ArrayList<>();
        for (int i = 0; i < calledEntries.size() - 1; i++) {
            LocalDateTime firstCall = calledEntries.get(i).getCalledAt();
            LocalDateTime secondCall = calledEntries.get(i + 1).getCalledAt();
            serviceTimes.add(Duration.between(firstCall, secondCall).toMinutes());
        }

        // 3. Calculate the average service time
        OptionalDouble averageServiceTime = serviceTimes.stream().mapToLong(l -> l).average();
        if (averageServiceTime.isEmpty()) {
            return -1; // Cannot calculate average
        }

        // 4. Get the number of people still waiting
        long peopleWaiting = entries.stream().filter(e -> "waiting".equalsIgnoreCase(e.getStatus())).count();

        // 5. Estimate the total wait time
        return (long) (averageServiceTime.getAsDouble() * peopleWaiting);
    }


    public void notifyUserAndBroadcastUpdate(String queueId, QueueEntry calledEntry) {
        messagingTemplate.convertAndSend("/topic/called/" + queueId + "/" + calledEntry.getId(), calledEntry);
        broadcastQueueUpdate(queueId);
    }
}