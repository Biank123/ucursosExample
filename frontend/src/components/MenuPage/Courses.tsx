import React, { useEffect, useState } from 'react';
import './Courses.css';

const EnrolledCourses: React.FC = () => {
  const [enrolledCourses, setEnrolledCourses] = useState<any[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchEnrolledCourses = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/enroll`, {
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
        setEnrolledCourses(data);
      } catch (err: any) {
        setError(err.message);
      }
    };

    fetchEnrolledCourses();
  }, []);

  return (
    <div className='courses-list'>
      <h2>Cursos Inscritos</h2>
      {error && <p>Error: {error}</p>}
      <ul>
        {enrolledCourses.map(course => (
          <li key={course.course_id}>
            {course.course_name}: {course.description}
          </li>
        ))}
        <button>Eliminar curso</button>
      </ul>
      
    </div>
  );
};

export default EnrolledCourses;