package com.lupenmar.unicorntask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Margarita Lupenko");
        myContact.setEmail("mrg.lupenko@gmail.com");

        Info information = new Info()
                .title("Car rental management system API")
                .version("1.0")
                .description("This API provides endpoints for car management, car rental accounting and customer management.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
