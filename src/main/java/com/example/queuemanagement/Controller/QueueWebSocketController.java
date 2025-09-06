package com.example.queuemanagement.Controller;

import com.example.queuemanagement.Repository.QueueEntryRepository;
import com.example.queuemanagement.entites.QueueEntry;
import com.example.queuemanagement.services.QueueEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class QueueWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final QueueEntryService queueEntryService;

    public QueueWebSocketController(SimpMessagingTemplate messagingTemplate, @Lazy  QueueEntryService queueEntryService) {
        this.messagingTemplate = messagingTemplate;
        this.queueEntryService = queueEntryService;
    }

    public void broadcastQueueUpdate(String queueId) {
        List<QueueEntry> allEntries = queueEntryService.getAllEntriesByQueueId(queueId);
        allEntries.sort(Comparator.comparingInt(QueueEntry::getQueueNumber));
        messagingTemplate.convertAndSend("/topic/queue/" + queueId, allEntries);
    }

    public void notifyUserAndBroadcastUpdate(String queueId, QueueEntry calledEntry) {
        messagingTemplate.convertAndSend("/topic/called/" + queueId + "/" + calledEntry.getId(), calledEntry);
        broadcastQueueUpdate(queueId);
    }
}
