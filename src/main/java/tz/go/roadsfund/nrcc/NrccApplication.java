package tz.go.roadsfund.nrcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for NRCC Database Management System
 * National Road Classification Committee - Roads Fund Board
 */
@SpringBootApplication
@EnableJpaAuditing
public class NrccApplication {

    public static void main(String[] args) {
        SpringApplication.run(NrccApplication.class, args);
    }
}
