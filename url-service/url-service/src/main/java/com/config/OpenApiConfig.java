package com.example.urlservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI urlServiceOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("URL Service API")
                .version("v1")
                .description("URL qısaltma və yönləndirmə API-si."));
    }
}
