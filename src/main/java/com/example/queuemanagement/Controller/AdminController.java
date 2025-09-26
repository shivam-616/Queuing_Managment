package com.example.queuemanagement.Controller;

import com.example.queuemanagement.entites.QueueEntry;
import com.example.queuemanagement.services.QueueEntryService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/{queueId}")
@RequiredArgsConstructor
public class AdminController {


    private final QueueEntryService     queueEntryService;
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

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearQueue(@PathVariable String queueId) {
        queueEntryService.clearQueue(queueId);
        return ResponseEntity.ok().build();
    }
}