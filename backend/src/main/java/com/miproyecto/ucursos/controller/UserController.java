package com.miproyecto.ucursos.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miproyecto.ucursos.model.User;
import com.miproyecto.ucursos.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll(); // Obtiene todos los usuarios
    }

    // @PostMapping("/register")
    // public ResponseEntity<String> registerUser(@RequestBody User user) {
    //     try {
    //         if (user.getEmail() == null || user.getPassword() == null) {
    //             return ResponseEntity.badRequest().body("Email y contraseña son requeridos");
    //         }

    //         System.out.println("Registrando usuario: " + user.getEmail());
    //         userService.save(user); // Aquí ya se verifica si el correo ya está registrado
    //         return ResponseEntity.ok("Usuario registrado con éxito");

    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     } catch (Exception e) {
    //         System.out.println("Error al registrar usuario: " + e.getMessage());
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar usuario: " + e.getMessage());
    //     }
    // }

      @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        try {
            if (user.getEmail() == null || user.getPassword() == null) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("error", "Email y contraseña son requeridos"));
            }

            System.out.println("Registrando usuario: " + user.getEmail());
            userService.save(user); // Aquí ya se verifica si el correo ya está registrado
            return ResponseEntity.ok(Collections.singletonMap("message", "Usuario registrado con éxito"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error al registrar usuario: " + e.getMessage()));
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
public ResponseEntity<Map<String, String>> loginUser(@RequestBody User user) {
    System.out.println("Intentando iniciar sesión con: " + user.getEmail());

    Optional<String> optionalToken = userService.login(user.getEmail(), user.getPassword());
    if (optionalToken.isPresent()) {
        String token = optionalToken.get();
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response); // Devuelve el token como parte de un objeto JSON
    } else {
        System.out.println("Credenciales incorrectas.");
        return ResponseEntity.status(401).body(Collections.singletonMap("message", "Credenciales incorrectas"));
    }
}


}
