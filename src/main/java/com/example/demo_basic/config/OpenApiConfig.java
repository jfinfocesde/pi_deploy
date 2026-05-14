package com.example.demo_basic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration
// public class OpenApiConfig {

//     @Bean
//     public OpenAPI adopcionOpenAPI() {
//         return new OpenAPI()
//                 .info(new Info()
//                         .title("API de Adopción de Mascotas")
//                         .description("""
//                                 Backend para un sistema simple de adopción de mascotas.
                                
//                                 Permite administrar **Adoptantes**, **Mascotas** y **Solicitudes** con:
//                                 - Herencia JPA (`@MappedSuperclass`)
//                                 - Enumeraciones (`@Enumerated`)
//                                 - Auditoría automática (`@CreatedDate` / `@LastModifiedDate`)
//                                 """)
//                         .version("1.0.0")
//                         .contact(new Contact()
//                                 .name("Equipo de Desarrollo")
//                                 .email("dev@adopcion.com"))
//                         .license(new License()
//                                 .name("MIT License")
//                                 .url("https://opensource.org/licenses/MIT")));
//     }
// }

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Sistema de Ventas Cesde")
                .version("1.0.0")
                .description("Documentación técnica de servicios REST."));
    }
}
