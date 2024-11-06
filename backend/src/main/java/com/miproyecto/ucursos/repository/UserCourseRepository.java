package com.miproyecto.ucursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.miproyecto.ucursos.model.Course;
import com.miproyecto.ucursos.model.UserCourse;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    @Query("SELECT uc.course FROM UserCourse uc WHERE uc.user.userId = :userId")
    List<Course> findCoursesByUserId(Long userId);
    

    UserCourse findByUser_UserIdAndCourse_CourseId(Long userId, Long courseId);
}
 