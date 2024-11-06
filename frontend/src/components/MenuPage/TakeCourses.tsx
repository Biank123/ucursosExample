import React, { useEffect, useState } from 'react';
import './TakeCourses.css';

const Courses: React.FC = () => {
  const [courses, setCourses] = useState<any[]>([]);
  const [enrolledCourses, setEnrolledCourses] = useState<number[]>([]);
  const [error, setError] = useState<string | null>(null);

  // Para mostrar los cursos disponibles
  useEffect(() => {
    const fetchCourses = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/courses', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}` 
          },
        });
        
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        const data = await response.json();
        console.log(data);
        setCourses(data);
      } catch (err: any) {
        setError(err.message);
      }
    };

    const fetchEnrolledCourses = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/enroll', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
        });

        if (!response.ok) {
          throw new Error('No se pudo obtener la lista de cursos inscritos');
        }

        const data = await response.json();
        // Extraemos los IDs de los cursos inscritos
        setEnrolledCourses(data.map((course: any) => course.courseId));
      } catch (err: any) {
        setError(err.message);
      }
    };

    fetchCourses();
    fetchEnrolledCourses();
  }, []);

 
  // Para inscribir un curso
  const enrollInCourse = async (courseId: number) => {
    const userCourse = {
      course: { courseId: courseId },
      roleInCourse: localStorage.getItem('role')
    };
    try {
      const response = await fetch(`http://localhost:8080/api/enroll/student/${courseId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(userCourse),
      });

      if (!response.ok) {
        throw new Error('Failed to enroll in course');
      }

      const result = await response.json();
      alert(`Inscripción exitosa: ${result.message}`);
      
      // Agregar el curso inscrito a la lista localmente
      setEnrolledCourses(prev => [...prev, courseId]);
    } catch (err: any) {
      alert(`Error: ${err.message}`);
    }
  };

  return (
    <div className="content">
      <h2>Cursos Disponibles</h2>
      {error && <p>Error: {error}</p>}
      <ul className="course-list">
        {courses.map(course => (
          <li className="course-item" key={course.courseId}>
            {course.courseName}: {course.description}
            <button
              onClick={() => enrollInCourse(course.courseId)}
              className="enroll-button"
              disabled={enrolledCourses.includes(course.courseId)} // Deshabilitar si ya está inscrito
            >
              {enrolledCourses.includes(course.courseId) ? 'Ya inscrito' : 'Inscribir curso'}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Courses;
