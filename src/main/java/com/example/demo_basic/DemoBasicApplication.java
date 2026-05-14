package com.example.demo_basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DemoBasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoBasicApplication.class, args);
	}

}
