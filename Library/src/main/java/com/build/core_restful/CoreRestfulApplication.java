package com.build.core_restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoreRestfulApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoreRestfulApplication.class, args);
	}
}