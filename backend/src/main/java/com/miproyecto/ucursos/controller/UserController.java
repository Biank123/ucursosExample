package com.miproyecto.ucursos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miproyecto.ucursos.model.User;
import com.miproyecto.ucursos.security.JwtUtil;
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
            if (user.getEmail() == null || user.getPassword() == null) {
                return ResponseEntity.badRequest().body("Email y contraseña son requeridos");
            }

            System.out.println("Registrando usuario: " + user.getEmail());

            // Verificar si el usuario ya existe
            if (userService.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.badRequest().body("El correo electrónico ya está registrado");
            }

            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
            System.out.println("Contraseña encriptada al registrar: " + encryptedPassword);
            userService.save(user);
            return ResponseEntity.ok("Usuario registrado con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar usuario: " + e.getMessage());
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
        System.out.println("Intentando iniciar sesión con: " + user.getEmail());

        Optional<User> optionalUser = userService.login(user.getEmail(), user.getPassword());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            System.out.println("Usuario encontrado: " + existingUser.getEmail());
            String token = jwtUtil.generateToken(existingUser.getEmail());
            return ResponseEntity.ok(token);
        } else {
            System.out.println("Credenciales incorrectas.");
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }

}
