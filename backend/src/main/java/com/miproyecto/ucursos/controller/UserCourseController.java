package com.miproyecto.ucursos.controller;

import com.miproyecto.ucursos.model.Course;
import com.miproyecto.ucursos.model.User;
import com.miproyecto.ucursos.model.UserCourse;
import com.miproyecto.ucursos.security.JwtUtil;
import com.miproyecto.ucursos.service.UserCourseService;
import com.miproyecto.ucursos.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<Course>> getEnrolledCourses() {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                System.out.println("Usuario no autenticado");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            List<Course> enrolledCourses = userCourseService.getEnrolledCoursesByUserId(currentUser.getUserId());
            return new ResponseEntity<>(enrolledCourses, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error al obtener los cursos inscritos: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{courseId}")
    public ResponseEntity<Map<String, String>> enrollCourse(@PathVariable Long courseId, HttpServletRequest request) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                System.out.println("Usuario no autenticado"); // Log para depuración
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Usuario no autenticado"));
            }
    
            // Obtener el rol desde el token
            String token = request.getHeader("Authorization").substring(7);
            String role = jwtUtil.extractRole(token);
    
            UserCourse userCourse = new UserCourse();
            userCourse.setCourse(new Course(courseId));
            userCourse.setUser(currentUser);
            userCourse.setRoleInCourse(role);
    
            userCourseService.enrollUserCourse(userCourse);
            System.out.println("Inscripción exitosa para el curso: " + courseId + " por el usuario: " + currentUser.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Inscripción exitosa"));
        } catch (Exception e) {
            System.out.println("Error en la inscripción: " + e.getMessage()); // Log para depuración
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Error en la inscripción: " + e.getMessage()));
        }
    }

}
