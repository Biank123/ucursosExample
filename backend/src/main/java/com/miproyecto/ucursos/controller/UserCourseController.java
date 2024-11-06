package com.miproyecto.ucursos.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miproyecto.ucursos.model.Course;
import com.miproyecto.ucursos.model.User;
import com.miproyecto.ucursos.model.UserCourse;
import com.miproyecto.ucursos.security.JwtUtil;
import com.miproyecto.ucursos.service.UserCourseService;
import com.miproyecto.ucursos.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

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

    // Para que un estudiante inscriba un curso
    @PostMapping("/student/{courseId}")
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

    // Para que un profesor inscriba un curso con su rol al crear un curso nuevo 
    @PostMapping("/professor/{courseId}")
    public ResponseEntity<Map<String, String>> enrollProfessor(@PathVariable Long courseId, HttpServletRequest request) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                System.out.println("Usuario no autenticado"); // Log para depuración
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Usuario no autenticado"));
            }

            UserCourse userCourse = new UserCourse();
            userCourse.setCourse(new Course(courseId));
            userCourse.setUser(currentUser);
            userCourse.setRoleInCourse("professor"); // Establecer el rol como profesor

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

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Map<String, String>> removeCourse(@PathVariable Long courseId, HttpServletRequest request) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                System.out.println("Usuario no autenticado"); // Log para depuración
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Usuario no autenticado"));
            }

            // Verificar si el usuario está inscrito en el curso
            UserCourse userCourse = userCourseService.getUserCourseByUserAndCourse(currentUser.getUserId(), courseId);
            if (userCourse == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "El usuario no está inscrito en este curso"));
            }

            // Eliminar la inscripción usando el ID de `userCourse`
            userCourseService.removeUserCourseById(userCourse.getUserCourseId());
            System.out.println("El curso con id " + courseId + " ha sido eliminado de los cursos inscritos por el usuario " + currentUser.getEmail());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Curso eliminado de los inscritos"));
        } catch (Exception e) {
            System.out.println("Error al eliminar el curso: " + e.getMessage()); // Log para depuración
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al eliminar el curso: " + e.getMessage()));
        }
    }
}
