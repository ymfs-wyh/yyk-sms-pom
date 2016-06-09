package com.yyk333.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.yyk333.sms.comlinerunners.ConfigParamInitRunner;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@ImportResource("classpath:dubbo-sms.xml")
@EnableTransactionManagement
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		logger.debug("spring boot start!!!!!");
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ConfigParamInitRunner configParamInitRunner() {
		return new ConfigParamInitRunner();
	}

}
