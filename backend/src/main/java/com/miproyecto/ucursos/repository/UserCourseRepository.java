package com.miproyecto.ucursos.repository;

import java.util.List;
import com.miproyecto.ucursos.model.Course;
import com.miproyecto.ucursos.model.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
     @Query("SELECT uc.course FROM UserCourse uc WHERE uc.user.user_id = :userId")
    List<Course> findCoursesByUserId(Long userId);
}
