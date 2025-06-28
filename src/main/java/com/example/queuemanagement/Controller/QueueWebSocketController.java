package com.example.queuemanagement.Controller;

import com.example.queuemanagement.Repository.QueueEntryRepository;
import com.example.queuemanagement.entites.QueueEntry;
import com.example.queuemanagement.services.QueueEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class QueueWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final QueueEntryRepository qu;
    private final QueueEntryService queueEntryService;

    // This method is called when admin "calls" the next person
    public void notifyNextPerson() {
        Optional<QueueEntry> next = queueEntryService.getNextWaitingEntry();
        if (next.isPresent()) {
            QueueEntry called = queueEntryService.updateStatus(next.get().getId(), "called");

            // Notify the person whose turn it is
            messagingTemplate.convertAndSend("/topic/queue", called);

            // Broadcast updated waiting list for everyone to recalculate positions
            List<QueueEntry> allWaiting = qu.findAll().stream()
                    .filter(e -> "waiting".equalsIgnoreCase(e.getStatus()))
                    .sorted(Comparator.comparingInt(QueueEntry::getQueueNumber))
                    .collect(Collectors.toList());

            messagingTemplate.convertAndSend("/topic/queue", allWaiting);
        }
    }
}
