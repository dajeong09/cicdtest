package com.innocamp.dduha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DduhaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DduhaApplication.class, args);
    }

}
