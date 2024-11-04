package com.miproyecto.ucursos.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.miproyecto.ucursos.model.User;
import com.miproyecto.ucursos.repository.UserRepository;
import com.miproyecto.ucursos.security.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class UserService {
private SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Autowired
private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll(); // Obtiene todos los usuarios
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id); // Busca un usuario por su ID
    }

    public User save(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }
        if (!user.getRole().equals("student") && !user.getRole().equals("professor")) {
            throw new IllegalArgumentException("Rol inválido");
        }
        // Encriptar la contraseña antes de guardar el usuario
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        System.out.println("Contraseña encriptada: " + encodedPassword);

        return userRepository.save(user); // Guarda el nuevo usuario
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id); // Elimina un usuario por su ID
    }

    public String generateToken(Long userId) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 86400000); // 1 día de validez

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey) 
                .compact();
    }


    public Optional<String> login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            System.out.println("Usuario encontrado: " + user.getEmail());
            boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
            System.out.println("¿Coinciden contraseñas? " + passwordMatch); 
            
            if (passwordMatch) {
                // Generar el token usando JwtUtil, pasando el ID y el correo del usuario
                String token = jwtUtil.generateToken(user.getUserId(), user.getEmail(), user.getRole());
                System.out.println("Token generado: " + token);
                return Optional.of(token); // Devuelve el token
            } else {
                System.out.println("Contraseña incorrecta para el usuario: " + user.getEmail());
            }
        } else {
            System.out.println("Usuario no encontrado: " + email);
        }
        return Optional.empty(); // Si no se encuentra el usuario o la contraseña es incorrecta
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName(); 
            return userRepository.findByEmail(email); 
        }
        return null; // No hay usuario autenticado
    }
}
