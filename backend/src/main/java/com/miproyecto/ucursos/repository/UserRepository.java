package com.miproyecto.ucursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miproyecto.ucursos.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}