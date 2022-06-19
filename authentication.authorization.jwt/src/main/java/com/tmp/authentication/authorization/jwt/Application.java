package com.tmp.authentication.authorization.jwt;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Auth API",
        version = "1.0",
        description = "Authentication and Authorization using JWT documentation"))
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
