package com.miproyecto.ucursos;

import java.util.Arrays; // Para Arrays.asList

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; // Para la anotación @Bean
import org.springframework.web.cors.CorsConfiguration; // Para la anotación @Configuration
import org.springframework.web.cors.CorsConfigurationSource; // Para CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Para UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Para CorsConfigurationSource
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // Para CorsRegistry

import jakarta.annotation.Nonnull; // Para WebMvcConfigurer


@Configuration
public class WebConfig implements WebMvcConfigurer {
    

    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(@Nonnull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
                

    }
     @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    

}
