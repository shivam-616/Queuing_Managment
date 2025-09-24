package com.example.queuemanagement.Controller;

import com.example.queuemanagement.entites.QueueEntry;
import com.example.queuemanagement.services.QueueEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/{queueId}")
@RequiredArgsConstructor
public class AdminController {

//    public AdminController(QueueEntryService queueEntryService, QueueWebSocketController queueWebSocketController) {
//        this.queueEntryService = queueEntryService;
//        this.queueWebSocketController = queueWebSocketController;
//    }

    private final QueueEntryService queueEntryService;
    private final QueueWebSocketController queueWebSocketController;

    @PostMapping("/next")
    public ResponseEntity<?> callNextPerson(@PathVariable String queueId) {
        Optional<QueueEntry> nextEntryOpt = queueEntryService.getNextWaitingEntry(queueId);

        if (nextEntryOpt.isPresent()) {
            QueueEntry nextEntry = nextEntryOpt.get();
            QueueEntry calledEntry = queueEntryService.updateStatus(nextEntry.getId(), "called");
            queueWebSocketController.notifyUserAndBroadcastUpdate(queueId, calledEntry);
            return ResponseEntity.ok(calledEntry);
        } else {
            return ResponseEntity.status(404).body("No one is waiting in the queue.");
        }
    }
}