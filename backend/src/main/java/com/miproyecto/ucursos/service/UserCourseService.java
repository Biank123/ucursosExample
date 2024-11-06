package com.miproyecto.ucursos.service;

import java.util.List;
import com.miproyecto.ucursos.model.Course;
import com.miproyecto.ucursos.model.UserCourse;
import com.miproyecto.ucursos.repository.UserCourseRepository; // Asegúrate de tener un repositorio para UserCourse
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCourseService {

    @Autowired
    private UserCourseRepository userCourseRepository;

    // Obtener los cursos inscritos por el usuario
    public List<Course> getEnrolledCoursesByUserId(Long userId) {
        List<Course> courses = userCourseRepository.findCoursesByUserId(userId);
        return courses;
    }

    // Inscribir un usuario en un curso
    public void enrollUserCourse(UserCourse userCourse) {
        userCourseRepository.save(userCourse);
    }

    // Obtener la inscripción específica de un usuario en un curso
    public UserCourse getUserCourseByUserAndCourse(Long userId, Long courseId) {
        return userCourseRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId);
    }    

    // Eliminar la inscripción de un usuario en un curso
    // public void removeUserCourse(UserCourse userCourse) {
    //     try {
    //         userCourseRepository.delete(userCourse);
    //     } catch (Exception e) {
    //         // Log para depuración en caso de error
    //         System.out.println("Error al eliminar la inscripción: " + e.getMessage());
    //         throw e;  
    //     }
    // }

    // Eliminar por ID
    public void removeUserCourseById(Long userCourseId) {
    userCourseRepository.deleteById(userCourseId);  // Método disponible sin definirlo explícitamente
    }
}
