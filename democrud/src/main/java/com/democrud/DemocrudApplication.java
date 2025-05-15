package com.democrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//@EnableScheduling
public class DemocrudApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(DemocrudApplication.class, args);
		System.out.println("Running Applicatation at port 8089 ");
	}
	
	@Bean
	public RestTemplate restTemplate() {
		
		return new RestTemplate();
	}

}
