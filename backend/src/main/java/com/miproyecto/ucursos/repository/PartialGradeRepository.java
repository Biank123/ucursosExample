package com.miproyecto.ucursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miproyecto.ucursos.model.PartialGrade;

@Repository
public interface PartialGradeRepository extends JpaRepository<PartialGrade, Long> {
    List<PartialGrade> findByUserCourse_User_UserIdAndUserCourse_Course_CourseId(Long userId, Long courseId);
}



