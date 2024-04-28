package com.university.mcmaster.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        OpenAPI openAPI = new OpenAPI();
        Info info = new Info();
        info.setTitle("Mc Master Student Housing Portal");
        info.setDescription("Backend APIs for mc master student housing portal");
        info.setVersion("0.0.1");
        openAPI.setInfo(info);
        openAPI = openAPI.components(
                new Components()
                        .addSecuritySchemes(
                                "bearer-jwt",
                                new SecurityScheme()
                                        .in(SecurityScheme.In.HEADER)
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat("JWT")
                                        .scheme("bearer")
                                        .name("Authorization")
                        )

        ).addSecurityItem(new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
        return openAPI;
    }
}
