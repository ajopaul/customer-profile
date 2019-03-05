package com.ajopaul.qantas.customerprofile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CustomerProfileApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CustomerProfileApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CustomerProfileApplication.class);
	}

}