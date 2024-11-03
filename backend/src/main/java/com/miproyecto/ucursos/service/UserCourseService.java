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

    public List<Course> getEnrolledCoursesByUserId(Long userId) {
        return userCourseRepository.findCoursesByUserId(userId);
    }

    public void enrollUserCourse(UserCourse userCourse) {
        userCourseRepository.save(userCourse);
    }
}
