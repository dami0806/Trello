package com.sparta.trello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class TrelloApplication {

    public static void main(String[] args) {

        SpringApplication.run(TrelloApplication.class, args);
    }

}
