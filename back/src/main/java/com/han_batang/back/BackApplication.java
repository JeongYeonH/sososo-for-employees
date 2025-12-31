package com.han_batang.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class BackApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);		
	}
}
