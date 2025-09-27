package com.example.queuemanagement.DTO;

import com.example.queuemanagement.entites.QueueEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardAnalyticsDTO {
    private long totalVisitorsToday;
    private String peakHour; // e.g., "2:00 PM"
    private long averageWaitTimeMinutes;
    private long averageServiceTimeMinutes;
    private Map<Integer, Long> dailyTraffic; // Hour -> Visitor Count
    private List<QueueEntry> recentVisitors; // Last 5 visitors
}
