package com.cineplanetfactory.retocp.adapters.config.openapi;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RetoCP: API de Productos y Pedidos")
                        .version("1.0.0")
                        .description("Documentación del API RetoCP para gestion de productos y pedidos"));
    }
}