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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user); // Guarda el nuevo usuario
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id); // Elimina un usuario por su ID
    }

    public Optional<User> login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return Optional.of(user);
        } else {
            return Optional.empty(); // Si no se encuentra el usuario o la contraseña es incorrecta
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}