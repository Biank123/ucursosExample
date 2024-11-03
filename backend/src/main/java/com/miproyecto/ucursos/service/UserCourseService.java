package com.miproyecto.ucursos.service;

import com.miproyecto.ucursos.model.UserCourse;
import com.miproyecto.ucursos.repository.UserCourseRepository; // Asegúrate de tener un repositorio para UserCourse
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCourseService {

    @Autowired
    private UserCourseRepository userCourseRepository;

    public void enrollUserCourse(UserCourse userCourse) {
        userCourseRepository.save(userCourse);
    }
}
