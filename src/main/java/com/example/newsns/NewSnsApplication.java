package com.example.newsns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class NewSnsApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewSnsApplication.class, args);
	}

}
