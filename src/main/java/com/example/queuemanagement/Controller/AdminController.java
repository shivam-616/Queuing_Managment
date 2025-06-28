package com.example.queuemanagement.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final QueueWebSocketController queueWebSocketController;

    // Admin hits this to call the next person
    @PostMapping("/next")
    public ResponseEntity<String> callNextPerson() {
        queueWebSocketController.notifyNextPerson();
        return ResponseEntity.ok("✅ Next person has been notified.");
    }
}
