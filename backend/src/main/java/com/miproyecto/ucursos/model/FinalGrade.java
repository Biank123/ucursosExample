package com.miproyecto.ucursos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "final_grades")
public class FinalGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "final_grade_id")
    private Long finalGradeId;

    @ManyToOne
    @JoinColumn(name = "user_course_id", nullable = false)
    private UserCourse userCourse;

    @Column(name = "final_grade", nullable = false)
    private Double finalGrade;

    // Getters and setters
    public UserCourse getUserCourse() {
        return userCourse;
    }

    public void setUserCourse(UserCourse userCourse) {
        this.userCourse = userCourse;
    }

    public Double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public Long getFinalGradeId() {
        return finalGradeId;
    }

    public void setFinalGradeId(Long finalGradeId) {
        this.finalGradeId = finalGradeId;
    }

    public FinalGrade() {
    }

    public FinalGrade(Long finalGradeId, UserCourse userCourse, Double finalGrade) {
        this.finalGradeId = finalGradeId;
        
        this.userCourse = userCourse;
        this.finalGrade = finalGrade;
    }

    
}