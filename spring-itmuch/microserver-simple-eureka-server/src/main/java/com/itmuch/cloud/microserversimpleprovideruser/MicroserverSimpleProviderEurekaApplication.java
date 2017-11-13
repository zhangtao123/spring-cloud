package com.itmuch.cloud.microserversimpleprovideruser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MicroserverSimpleProviderEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserverSimpleProviderEurekaApplication.class, args);
	}
}
