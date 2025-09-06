package com.example.queuemanagement.Config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**
     * Creates a custom OpenAPI bean to define the API's metadata.
     *
     * @return A configured OpenAPI object.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Queue Management System API")
                        .version("1.0.0")
                        .description("This is the API documentation for the Dynamic Queue Management System. " +
                                "It allows for the creation and management of multiple real-time queues.")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}