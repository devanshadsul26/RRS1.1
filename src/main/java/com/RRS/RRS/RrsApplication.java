package com.RRS.RRS;

import com.RRS.RRS.table.TableRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RrsApplication.class, args);
    }

    @Bean
    CommandLineRunner testDb(TableRepository tableRepository) {
        return args -> {
            long count = tableRepository.count();
            System.out.println("Tables in DB = " + count);
        };
    }
}
