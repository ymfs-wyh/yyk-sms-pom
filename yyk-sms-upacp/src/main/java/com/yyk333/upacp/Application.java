package com.yyk333.upacp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	public static void main( String[] args ) {
		logger.debug("spring boot start!!!!!");
		SpringApplication.run(Application.class, args);
	}
	
}
