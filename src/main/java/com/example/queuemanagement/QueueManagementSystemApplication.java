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

//    @Bean
//    public CommandLineRunner loadTestData(QueueEntryRepository repository) {
//        return args -> {
//            QueueEntry entry1 = QueueEntry.builder()
//                    .name("Test User 1")
//                    .phone("9999999999")
//                    .status("waiting")
//                    .queueNumber(1)
//                    .notified(false)
//                    .build();
//
//            QueueEntry entry2 = QueueEntry.builder()
//                    .name("Test User 2")
//                    .phone("8888888888")
//                    .status("waiting")
//                    .queueNumber(2)
//                    .notified(false)
//                    .build();
//
//            repository.save(entry1);
//            repository.save(entry2);
//
//        };
//    }
}
