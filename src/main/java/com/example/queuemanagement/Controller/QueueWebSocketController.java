package com.example.queuemanagement.Controller;

import com.example.queuemanagement.services.QueueEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class QueueWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final QueueEntryService queueEntryService;

    // This method is called when admin "calls" the next person
    public void notifyNextPerson() {
        queueEntryService.getNextWaitingEntry().ifPresent(entry -> {
            // Update their status to "called"
            queueEntryService.updateStatus(entry.getId(), "called");

            // Notify the client via WebSocket
            messagingTemplate.convertAndSend("/topic/queue", entry);
        });
    }
}
