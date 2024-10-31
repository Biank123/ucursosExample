package com.miproyecto.ucursos.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.Nonnull;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(@Nonnull CorsRegistry registry) {
        registry.addMapping("/**") // Permite todas las rutas
                .allowedOrigins("http://localhost:3000") 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
