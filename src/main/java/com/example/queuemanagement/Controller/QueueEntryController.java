package com.example.queuemanagement.Controller;


import com.example.queuemanagement.DTO.QueueEntryRequest;
import com.example.queuemanagement.entites.QueueEntry;
import com.example.queuemanagement.services.QueueEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queue/{queueId}")
@RequiredArgsConstructor
public class QueueEntryController {
//    public QueueEntryController(QueueEntryService queueEntryService) {
//        this.queueEntryService = queueEntryService;
//    }

    private final QueueEntryService queueEntryService;

    @PostMapping("/create")
    public ResponseEntity<QueueEntry> createEntry(@PathVariable String queueId, @RequestBody QueueEntryRequest request) {
        QueueEntry entry = queueEntryService.createNewEntry(queueId, request.getDetails());
        return ResponseEntity.ok(entry);
    }

    @GetMapping("/all")
    public ResponseEntity<List<QueueEntry>> getAllEntries(@PathVariable String queueId) {
        return ResponseEntity.ok(queueEntryService.getAllEntriesByQueueId(queueId));
    }

    // Add this new method
    @GetMapping("/exists")
    public ResponseEntity<Boolean> queueExists(@PathVariable String queueId) {
        boolean hasEntries = !queueEntryService.getAllEntriesByQueueId(queueId).isEmpty();
        return ResponseEntity.ok(hasEntries);
    }
}



