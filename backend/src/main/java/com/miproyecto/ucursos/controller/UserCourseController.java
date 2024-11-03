package com.miproyecto.ucursos.controller;

import com.miproyecto.ucursos.model.Course;
import com.miproyecto.ucursos.model.User;
import com.miproyecto.ucursos.model.UserCourse;
import com.miproyecto.ucursos.service.UserCourseService;
import com.miproyecto.ucursos.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enroll")
public class UserCourseController {

    @Autowired
    private UserCourseService userCourseService;

    @Autowired
    private UserService userService;

    @PostMapping("/{courseId}")
    public ResponseEntity<String> enrollCourse(@PathVariable Long courseId, @RequestBody UserCourse userCourse) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                System.out.println("Usuario no autenticado"); // Log para depuración
                return new ResponseEntity<>("Usuario no autenticado", HttpStatus.UNAUTHORIZED);
            }
    
            userCourse.setCourse(new Course(courseId));
            userCourse.setUser(currentUser);
    
            userCourseService.enrollUserCourse(userCourse);
            System.out.println("Inscripción exitosa para el curso: " + courseId + " por el usuario: " + currentUser.getEmail());
            return new ResponseEntity<>("Inscripción exitosa", HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error en la inscripción: " + e.getMessage()); // Log para depuración
            return new ResponseEntity<>("Error en la inscripción: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}