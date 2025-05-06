package com.democrud.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "JWT Auth API", version = "1.0", description = "Demo CRUD API with JWT + Swagger")
)
public class SwaggerConfig {

}
