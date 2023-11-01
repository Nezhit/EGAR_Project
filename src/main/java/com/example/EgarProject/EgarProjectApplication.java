package com.example.EgarProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EgarProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(EgarProjectApplication.class, args);
	}

}
