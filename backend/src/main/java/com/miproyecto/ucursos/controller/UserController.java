package com.miproyecto.ucursos.controller;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miproyecto.ucursos.security.JwtUtil;
import com.miproyecto.ucursos.model.User;
import com.miproyecto.ucursos.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyección de PasswordEncoder

    @Autowired
    private JwtUtil jwtUtil; // Inyección de JwtUtil

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll(); // Obtiene todos los usuarios
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            // Encriptar la contraseña antes de guardar
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.save(user); // Guarda el nuevo usuario
            return ResponseEntity.ok("Usuario registrado con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id) // Buscar usuario por su id
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id); // Borrar usuario por su id
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());
        
        if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            String token = jwtUtil.generateToken(existingUser.getEmail());
            return ResponseEntity.ok(token); // Devuelve el token si la autenticación es exitosa
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }
}
