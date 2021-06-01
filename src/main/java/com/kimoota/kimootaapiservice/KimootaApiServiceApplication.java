package com.kimoota.kimootaapiservice;

import com.kimoota.kimootaapiservice.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class KimootaApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KimootaApiServiceApplication.class, args);
	}

}
