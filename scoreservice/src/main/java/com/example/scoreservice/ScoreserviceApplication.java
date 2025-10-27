package com.example.scoreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
public class ScoreserviceApplication {

	public static void main(String[] args) {
		//SpringApplication.run(ScoreserviceApplication.class, args);
		SpringApplication app = new SpringApplication(ScoreserviceApplication.class);
		ConfigurableApplicationContext context = app.run(args);
		System.out.println("Server port: " + context.getEnvironment().getProperty("server.port"));
		
	}

}
