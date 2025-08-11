package com.portfolio_caio.crud_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrudSpringbootApplication {
	public static void main(String[] args) {
		String port = System.getenv("PORT");
		if (port != null) {
		  System.setProperty("server.port", port);
		}
		SpringApplication.run(CrudSpringbootApplication.class, args);
	}
}
