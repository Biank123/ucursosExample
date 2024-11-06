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
        console.log(data);
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
      // Paso 1: Crear el curso
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

      // Paso 2: Inscribir automáticamente al creador como profesor
      await enrollAsProfessor(newCourse.courseId);

      // Limpiar campos
      setNewCourseName('');
      setNewCourseDescription('');
    } catch (err: any) {
      setError(err.message);
    }
  };

  // Función para inscribir al creador del curso como profesor
  const enrollAsProfessor = async (courseId: number) => {
    try {
      const userCourse = {
        course: { courseId },
        roleInCourse: 'professor'
      };

      const response = await fetch(`http://localhost:8080/api/enroll/professor/${courseId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(userCourse),
      });

      if (!response.ok) {
        throw new Error('Error al inscribir al profesor en el curso');
      }

      const result = await response.json();
      console.log(`Inscripción como profesor exitosa: ${result.message}`);
      alert('Inscripción de curso exitosa.');
    } catch (err: any) {
      setError(`Error al inscribir al profesor: ${err.message}`);
    }
  };

  return (
    <div className="content2">
      <h2>Añadir cursos</h2>
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
        <button type="submit">Crear Curso</button>
      </form>

      {/* Lista de cursos existentes */}
      <h3>Cursos Existentes</h3>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <ul>
        {courses.map((course) => (
          <li key={course.courseId}>
            <h4>{course.courseName}</h4>
            <p>{course.description}</p>
            <button onClick={() => enrollAsProfessor(course.courseId)}>
              Inscribir como profesor
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ApproveCourses;