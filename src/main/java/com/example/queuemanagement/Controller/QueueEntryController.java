package com.example.queuemanagement.Controller;


import com.example.queuemanagement.DTO.QueueEntryRequest;
import com.example.queuemanagement.entites.QueueEntry;
import com.example.queuemanagement.services.QueueEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
public class QueueEntryController {
    private final QueueEntryService queueEntryService;

    @PostMapping("/create")
    public ResponseEntity<QueueEntry> createEntry(@RequestBody QueueEntryRequest name) {
        QueueEntry entry = queueEntryService.createNewEntry(name.getName());
        System.out.println("New entry created: " + entry);
        return ResponseEntity.ok(entry);
    }

    // Get all entriesz
    @GetMapping("/all")
    public ResponseEntity<List<QueueEntry>> getAllEntries() {
        return ResponseEntity.ok(queueEntryService.getAllEntries());
    }

    // Get the next waiting entry
    @GetMapping("/next")
    public ResponseEntity<QueueEntry> getNextEntry() {
        return queueEntryService.getNextWaitingEntry()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    // Update status of an entry
    @PutMapping("/update-status/{id}")
    public ResponseEntity<QueueEntry> updateStatus(@PathVariable Long id, @RequestParam String status) {
        QueueEntry updated = queueEntryService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}


