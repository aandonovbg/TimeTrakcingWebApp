package com.example.TimeTrakcingApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class TimeTrackingAppApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
		return application.sources(TimeTrackingAppApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(TimeTrackingAppApplication.class, args);
	}

}
