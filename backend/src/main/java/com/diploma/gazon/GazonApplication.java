package com.diploma.gazon;

import com.tinify.Tinify;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GazonApplication {
	@Value("${tinypng.apikey}")
	private String tinyAPIKey;

	@PostConstruct
	public void appInit() {
		Tinify.setKey(tinyAPIKey);
	}

	public static void main(String[] args) {
		SpringApplication.run(GazonApplication.class, args);
	}
}
