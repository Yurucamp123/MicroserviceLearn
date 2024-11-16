package com.lethanhbinh.employeeservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Employee Service API documentation - lethanhbinh",
                description = "API documentation for employee service",
                version = "1.0",
                contact = @Contact(
                        name = "Le Thanh Binh",
                        email = "binhltse173315@fpt.edu.vn",
                        url = "binh.lt.com.vn"
                ),
                license = @License(
                        name = "Employee license",
                        url = "binhlicense.lt.com.vn"
                ),
                termsOfService = "binhltemployee.com.vn"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8003/", description = "Local ENV"
                ),
                @Server(
                        url = "http://bookmanagement.dev.com", description = "Dev ENV"
                )
        }
)
public class OpenAPIConfig {
}
