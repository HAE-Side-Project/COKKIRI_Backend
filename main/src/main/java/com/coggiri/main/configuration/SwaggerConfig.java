package com.coggiri.main.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Value("${springdoc.ip}")
    String serverIp;

    @Bean
    public OpenAPI springOpenAPI() {
        Server prodServer = new Server();
        prodServer.setUrl(serverIp);
        prodServer.setDescription("Production Server");

        return new OpenAPI()
                .info(new Info()
                        .title("API 문서")
                        .description("API 명세서")
                        .version("v1.0.0")
                )
                .servers(Arrays.asList(prodServer))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                        )
                );
    }
}