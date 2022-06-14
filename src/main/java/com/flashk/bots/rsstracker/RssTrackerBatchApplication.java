package com.flashk.bots.rsstracker;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class RssTrackerBatchApplication {

	@PostConstruct
	public void init(){
		// Setting Spring Boot SetTimeZone
		//TimeZone.setDefault(TimeZone.getTimeZone("GMT+2:00"));
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
  
	public static void main(String[] args) {
		SpringApplication.run(RssTrackerBatchApplication.class, args);
	}

}
