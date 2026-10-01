package com.ecocollect.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ecoCollectOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("EcoCollect AI API")
                        .version("1.0.0")
                        .description(
                                "API REST pour la gestion intelligente des collectes municipales, " +
                                "des conteneurs, des secteurs et des anomalies."
                        )
                        .contact(new Contact()
                                .name("EcoCollect AI")));
    }
}
