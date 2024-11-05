import React, { useEffect, useState } from 'react';
import './TakeCourses.css';

const Courses: React.FC = () => {
  const [courses, setCourses] = useState<any[]>([]);
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

    fetchCourses();
  }, []);

 
// Para inscribir un curso
  const enrollInCourse = async (courseId: number) => {

    const userCourse = {
      course: { courseId: courseId }, 
      roleInCourse: localStorage.getItem('role')
};
    try {
      const response = await fetch(`http://localhost:8080/api/enroll/${courseId}`, {
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
          <li className="course-item" key={course.courseId}>{course.courseName}: {course.description}
          <button onClick={() => enrollInCourse(course.courseId)} className="enroll-button">Inscribir curso</button>
          </li> 
          
        ))}
      </ul>
    </div>
  );
};

export default Courses;

