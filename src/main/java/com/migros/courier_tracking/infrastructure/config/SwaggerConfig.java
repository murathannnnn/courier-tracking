package com.migros.courier_tracking.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI courierTrackingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Courier Tracking API")
                        .description("RESTful API for tracking courier locations and managing store entries")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Migros One")
                                .email("support@migros.com")));
    }
}
