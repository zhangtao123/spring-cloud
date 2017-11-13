package com.itmuch.cloud.microserversimpleprovideruser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MicroserverSimpleProviderAuthenticatingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserverSimpleProviderAuthenticatingApplication.class, args);
	}
}
