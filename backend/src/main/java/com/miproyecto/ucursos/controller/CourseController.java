package com.miproyecto.ucursos.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miproyecto.ucursos.model.Course;
import com.miproyecto.ucursos.repository.CourseRepository;
import com.miproyecto.ucursos.service.CourseService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Value("${jwt.secret}") // Define el secreto JWT en application.properties
    private String jwtSecret;

    // Endpoint para obtener todos los cursos
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    
    // Endpoint para agregar un nuevo curso (rol profesor)
    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
        Course createdCourse = courseService.addCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Map<String, Object>> getCourseDetails(
        @PathVariable Long courseId,
        @RequestHeader("Authorization") String token) {

    // Intentamos obtener el curso usando findById() que devuelve un Optional
    Optional<Course> course = courseRepository.findById(courseId);

    // Verifica si el curso no está presente, en cuyo caso devuelve 404 Not Found
    if (!course.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Si el curso está presente, obtenemos el userId del token y luego los detalles del curso
    Long userId = getUserIdFromToken(token); 
    Map<String, Object> courseDetails = courseService.getCourseDetails(courseId, userId);

    // Retorna los detalles del curso
    return ResponseEntity.ok(courseDetails);
}

    private Long getUserIdFromToken(String token) {
    // Eliminar el prefijo "Bearer " del token
    token = token.replace("Bearer ", "");
    
    // Crear la clave secreta a partir del secreto
    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

    try {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // Usar la clave para validar el token
                .build()
                .parseClaimsJws(token)
                .getBody();
                
        return claims.get("userId", Long.class); 
    } catch (Exception e) {
        throw new RuntimeException("Invalid token");
    }
}

}