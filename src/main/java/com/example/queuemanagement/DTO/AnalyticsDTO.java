package com.example.queuemanagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDTO {
    private long averageWaitTimeMinutes;
    private long averageServiceTimeMinutes;
    private long totalServedToday;
    private Map<Integer, Long> trafficByHour; // Hour of day -> Count
}