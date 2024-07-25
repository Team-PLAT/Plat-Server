package com.cabin.plat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class PlatApplication {
	public static void main(String[] args) {
		SpringApplication.run(PlatApplication.class, args);
	}

}
