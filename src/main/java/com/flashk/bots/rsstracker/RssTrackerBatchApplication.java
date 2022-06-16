package com.flashk.bots.rsstracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class RssTrackerBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(RssTrackerBatchApplication.class, args);
	}

}
