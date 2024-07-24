package com.healthcare.ehrservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class EhrServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EhrServiceApplication.class, args);
	}

}
