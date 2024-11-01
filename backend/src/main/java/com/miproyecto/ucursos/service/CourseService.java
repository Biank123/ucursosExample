package com.miproyecto.ucursos.service;

import com.miproyecto.ucursos.model.Course;
import com.miproyecto.ucursos.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    
}
