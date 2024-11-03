package com.miproyecto.ucursos.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_courses")
public class UserCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_course_id")
    private Long userCourseId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "role_in_course", nullable = false)
    private String roleInCourse;

    @Column(name = "enrollment_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime enrollmentDate;

    // Constructor predeterminado
    public UserCourse() {
        this.enrollmentDate = LocalDateTime.now();
    }

    // Constructor parametrizado
    public UserCourse(Long userCourseId, User user, Course course, String roleInCourse) {
        this.userCourseId = userCourseId;
        this.user = user;
        this.course = course;
        this.roleInCourse = roleInCourse;
        this.enrollmentDate = LocalDateTime.now(); 
    }

    // Getters y Setters
    public Long getUserCourseId() {
        return userCourseId;
    }

    public void setUserCourseId(Long userCourseId) {
        this.userCourseId = userCourseId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getRoleInCourse() {
        return roleInCourse;
    }

    public void setRoleInCourse(String roleInCourse) {
        this.roleInCourse = roleInCourse;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    
    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}

