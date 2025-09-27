package com.example.queuemanagement.Controller;

import com.example.queuemanagement.DTO.DashboardAnalyticsDTO;
import com.example.queuemanagement.services.QueueEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final QueueEntryService queueEntryService;

    @GetMapping("/{queueId}")
    public ResponseEntity<DashboardAnalyticsDTO> getDashboardAnalytics(@PathVariable String queueId) {
        return ResponseEntity.ok(queueEntryService.getDashboardAnalytics(queueId));
    }
}
