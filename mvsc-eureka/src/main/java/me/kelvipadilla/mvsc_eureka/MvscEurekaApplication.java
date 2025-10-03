package me.kelvipadilla.mvsc_eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MvscEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvscEurekaApplication.class, args);
	}

}
