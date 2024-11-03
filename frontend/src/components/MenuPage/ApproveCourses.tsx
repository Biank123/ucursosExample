import React, { useEffect, useState } from 'react';
import './ApproveCourses.css';

const ApproveCourses: React.FC = () => {
  const [courses, setCourses] = useState<any[]>([]);
  const [newCourseName, setNewCourseName] = useState<string>('');
  const [newCourseDescription, setNewCourseDescription] = useState<string>('');
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    // Cargar la lista de cursos al iniciar el componente
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
          throw new Error('Error al obtener la lista de cursos');
        }

        const data = await response.json();
        setCourses(data);
      } catch (err: any) {
        setError(err.message);
      }
    };

    fetchCourses();
  }, []);

// Lógica para añadir un nuevo curso con el rol de profesor
  const handleAddCourse = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:8080/api/courses', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify({
          courseName: newCourseName,
          description: newCourseDescription,
        }),
      });

      if (!response.ok) {
        throw new Error('Error al crear el curso');
      }

      const newCourse = await response.json();
      setCourses([...courses, newCourse]); // Actualiza la lista con el nuevo curso
      setNewCourseName(''); // Limpia el campo de nombre del curso
      setNewCourseDescription(''); // Limpia el campo de descripción
    } catch (err: any) {
      setError(err.message);
    }
  };

  return (
    <div className="content2">
      <h2>Aprobar Cursos</h2>
      <p>Aquí podrás añadir cursos y ver la lista de los cursos existentes.</p>

      {/* Formulario para añadir un nuevo curso */}
      <form onSubmit={handleAddCourse}>
        <div>
          <label>Nombre del curso:</label>
          <input
            type="text"
            value={newCourseName}
            onChange={(e) => setNewCourseName(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Descripción del curso:</label>
          <input
            type="text"
            value={newCourseDescription}
            onChange={(e) => setNewCourseDescription(e.target.value)}
            required
          />
        </div>
        <button type="submit">Añadir Curso</button>
      </form>

      {/* Lista de cursos existentes */}
      <h3>Cursos Existentes</h3>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <ul>
        {courses.map((course) => (
          <li key={course.course_id}>
            <h4>{course.course_name}</h4>
            <p>{course.description}</p>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ApproveCourses;