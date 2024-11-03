import React, { useEffect, useState } from 'react';

const Courses: React.FC = () => {
  const [courses, setCourses] = useState<any[]>([]);
  const [error, setError] = useState<string | null>(null);

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
        setCourses(data);
      } catch (err: any) {
        setError(err.message);
      }
    };

    fetchCourses();
  }, []);

  const enrollInCourse = async (courseId: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/enroll/${courseId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}` 
        },
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
    <div>
      <h2>Cursos Disponibles</h2>
      {error && <p>Error: {error}</p>}
      <ul>
        {courses.map(course => (
          <li key={course.course_id}>{course.course_name}: {course.description}
          <button onClick={() => enrollInCourse(course.course_id)}>Inscribir curso</button>
          </li> 
          
        ))}
      </ul>
    </div>
  );
};

export default Courses;

