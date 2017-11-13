package com.itmuch.cloud.microserversimpleprovideruser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MicroserverSimpleProviderUserApplication2 {

	public static void main(String[] args) {
		SpringApplication.run(MicroserverSimpleProviderUserApplication2.class, args);
	}
}
