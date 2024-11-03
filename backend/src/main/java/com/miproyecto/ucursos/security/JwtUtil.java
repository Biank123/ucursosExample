package com.miproyecto.ucursos.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyString; // Clave secreta como String
    private SecretKey secretKey; // Clave secreta como SecretKey

    @PostConstruct
    public void init() {
        // Generar la clave secreta a partir de la clave secreta en formato String
        secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    // Generar token
    public String generateToken(Long userId, String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        boolean isValid = (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
        System.out.println("Token válido: " + isValid); // Para depuración
    
        // Extraer y mostrar los claims del token
        try {
            Claims claims = Jwts.parserBuilder() // Cambiado a parserBuilder()
                .setSigningKey(secretKey) // Configurar la clave de firma
                .build() // Construir el parser
                .parseClaimsJws(token) // Analizar el token
                .getBody(); // Obtener los claims
            System.out.println("Claims: " + claims);
        } catch (Exception e) {
            System.out.println("Error al analizar el token: " + e.getMessage());
        }
    
        return isValid;
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class); 
    }

    public String extractEmail(String token) {
        String email = extractAllClaims(token).getSubject();
        System.out.println("Email extraído del token: " + email); // Para depuración
        return email;
    }

    // Comprobar si ha expirado
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Extraer todos los datos del token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }
}