package com.miproyecto.ucursos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.miproyecto.ucursos.model.User;
import com.miproyecto.ucursos.repository.UserRepository;

@Service
public class UserService {

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

    public Optional<User> login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            System.out.println("Usuario encontrado: " + user.getEmail());
            boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
            System.out.println("Contraseña ingresada: " + password);
            System.out.println("Contraseña encriptada: " + user.getPassword());
            System.out.println("¿Coincide? " + passwordMatch); 
            
            if (passwordMatch) {
                return Optional.of(user);
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
}
