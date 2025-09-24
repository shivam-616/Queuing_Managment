package com.example.queuemanagement;

import com.example.queuemanagement.Repository.QueueEntryRepository;
import com.example.queuemanagement.entites.QueueEntry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class QueueManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueueManagementSystemApplication.class, args);
    }


}
