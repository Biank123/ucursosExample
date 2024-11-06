package com.miproyecto.ucursos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.miproyecto.ucursos.model.FinalGrade;

@Repository
public interface FinalGradeRepository extends JpaRepository<FinalGrade, Long> {

    @Query("SELECT AVG(f.finalGrade) FROM FinalGrade f JOIN f.userCourse uc JOIN uc.course c WHERE c.courseId = :courseId")
    Double calculateClassAverage(@Param("courseId") Long courseId);

    Optional<FinalGrade> findByUserCourse_User_UserIdAndUserCourse_Course_CourseId(Long userId, Long courseId);
}

