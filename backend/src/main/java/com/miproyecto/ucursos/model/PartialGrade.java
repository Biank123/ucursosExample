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
@Table(name = "partial_grades")
public class PartialGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partial_grade_id")
    private Long partialGradeId;

    @ManyToOne
    @JoinColumn(name = "user_course_id", nullable = false)
    private UserCourse userCourse;

    @Column(name = "grade", nullable = false)
    private Double partialGrade;

    // Getters y setters

    public Long getPartialGradeId() {
        return partialGradeId;
    }


    public void setPartialGradeId(Long partialGradeId) {
        this.partialGradeId = partialGradeId;
    }


    public UserCourse getUserCourse() {
        return userCourse;
    }


    public void setUserCourse(UserCourse userCourse) {
        this.userCourse = userCourse;
    }


    public Double getPartialGrade() {
        return partialGrade;
    }


    public void setPartialGrade(Double partialGrade) {
        this.partialGrade = partialGrade;
    }


    public PartialGrade(Long partialGradeId, UserCourse userCourse, 
   
    Double partialGrade) {
        this.partialGradeId = partialGradeId;
        this.userCourse = userCourse;
        
        this.partialGrade = partialGrade;
    }


    public PartialGrade() {
    }

   
}