package com.grocery.store;

import com.grocery.store.service.BasicDataSetup;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BasicDataSetup basicDataSetup) {
        return args -> {
            basicDataSetup.init();
        };
    }

}
