package com.miproyecto.ucursos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miproyecto.ucursos.model.Course;
import com.miproyecto.ucursos.model.FinalGrade;
import com.miproyecto.ucursos.model.PartialGrade;
import com.miproyecto.ucursos.repository.CourseRepository;
import com.miproyecto.ucursos.repository.FinalGradeRepository;
import com.miproyecto.ucursos.repository.PartialGradeRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // Método para obtener todos los cursos
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Método para agregar un nuevo curso
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    @Autowired
    private PartialGradeRepository partialGradeRepository;

    @Autowired
    private FinalGradeRepository finalGradeRepository;

    public Map<String, Object> getCourseDetails(Long courseId, Long userId) {
        Map<String, Object> courseDetails = new HashMap<>();
    
        // Obtener información del curso
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
    
        courseDetails.put("courseName", course.getCourseName());
        courseDetails.put("description", course.getDescription());
    
        // Obtener el promedio de la clase de las notas finales
        Double classAverage = finalGradeRepository.calculateClassAverage(courseId);
        courseDetails.put("classAverage", classAverage != null ? classAverage : 0.0); // Cambia a 0.0 en vez de "Sin notas"
    
        // Obtener las notas parciales del usuario
        List<PartialGrade> partialGrades = partialGradeRepository.findByUserCourse_User_UserIdAndUserCourse_Course_CourseId(userId, courseId);
        if (partialGrades.isEmpty()) {
            courseDetails.put("partialGrades", "No hay notas parciales disponibles");
        } else {
            courseDetails.put("partialGrades", partialGrades.stream().map(PartialGrade::getPartialGrade).collect(Collectors.toList()));
        }
    
        // Obtener la nota final del usuario
        Optional<FinalGrade> finalGrade = finalGradeRepository.findByUserCourse_User_UserIdAndUserCourse_Course_CourseId(userId, courseId);
        courseDetails.put("userGrade", finalGrade.map(FinalGrade::getFinalGrade).orElse(null)); // Cambia a null si no hay nota final
    
        return courseDetails;
    }
    
}
