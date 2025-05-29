package org.example.projectmanagerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan("org.example.projectmanagerapp.entity")
@EnableJpaRepositories("org.example.projectmanagerapp.repository")
public class ProjectManagerAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectManagerAppApplication.class, args);
    }
}
