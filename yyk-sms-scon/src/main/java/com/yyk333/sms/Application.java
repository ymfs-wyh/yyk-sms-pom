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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@ImportResource("classpath:dubbo-sms.xml")
@EnableTransactionManagement
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	public static void main( String[] args ) {
		logger.debug("spring boot start!!!");
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/knowledge/**").allowedOrigins("*");
                registry.addMapping("/banner/**").allowedOrigins("*");
                registry.addMapping("/pns/**").allowedOrigins("*");
            }
        };
    }
	
}
