import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
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

  const handleRemoveCourse = async (courseId: number) => {
    console.log('El ID del curso a eliminar:', courseId);
    
    try {
      const response = await fetch(`http://localhost:8080/api/enroll/${courseId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
  
      if (!response.ok) {
        throw new Error('No se pudo eliminar el curso');
      }
  
      // Actualiza la lista de cursos inscritos después de eliminar el curso
      setEnrolledCourses(enrolledCourses.filter(course => course.courseId !== courseId));
      alert('Curso eliminado correctamente');
    } catch (err: any) {
      setError(err.message);
    }
};
  return (
    <div className='courses-list'>
      <h2>Cursos Inscritos</h2>
      {error && <p>Error: {error}</p>}
      <ul>
        {enrolledCourses.map(course => (
          <li key={course.courseId}>
            <Link to={`/cursos/${course.courseId}`}>
              {course.courseName}: {course.description}
            </Link>
            <button onClick={() => handleRemoveCourse(course.courseId)}>Eliminar curso</button>

          </li>
        ))}
      </ul>
    </div>
  );
};

export default EnrolledCourses;