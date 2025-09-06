package com.example.queuemanagement.entites;

import com.example.queuemanagement.converter.JpaConverterJson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entry_db")
@Builder
public class QueueEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String queueId;

    @Convert(converter = JpaConverterJson.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> details;

    private String status;
    private int queueNumber;
    private boolean notified;
}
