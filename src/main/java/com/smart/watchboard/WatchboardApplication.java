package com.smart.watchboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WatchboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(WatchboardApplication.class, args);
	}

}
