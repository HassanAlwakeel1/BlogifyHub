package com.BlogifyHub.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Hassan Alwakeel",
                        url = "https://www.linkedin.com/in/hassan-alwakeel-617537287/"
                ),
                description = "Welcome to the OpenAPI documentation for BlogifyHub, a robust and scalable backend API for a Medium-like blogging platform. BlogifyHub is designed to offer a seamless experience for users to create, manage, and explore blogs. This API provides all the essential endpoints needed to build a full-featured blog application, including user authentication, blog management, media handling, and more.",
                title = "BlogifyHub",
                version = "1.0"
        )
)
public class OpenApiConfig {
}
