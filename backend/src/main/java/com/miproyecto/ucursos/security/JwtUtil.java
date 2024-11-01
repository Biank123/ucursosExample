package com.miproyecto.ucursos.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
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

    // Validar token
    public boolean validateToken(String token, String email) {
        try {
            return extractEmail(token).equals(email) && !isTokenExpired(token);
        } catch (Exception e) {
            return false; // Retorna false si hay un problema con la validación
        }
    }

    // Obtener usuario del token
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
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