// Falta:
// crear el foro 
// crear la sección de mensajería
// subida de archivos / sección para ver archivos subidos
// Componente para subir notas

import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Bar } from 'react-chartjs-2'; // Importa el gráfico de barra
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';
import './CourseDetails.css';

// Registra los componentes necesarios de Chart.js
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const CourseDetails: React.FC = () => {
  const { courseId } = useParams();
  const [courseDetails, setCourseDetails] = useState<any>(null);
  const [role, setRole] = useState<string | null>(localStorage.getItem('role'));
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchCourseDetails = async () => {
      console.log("Token enviado:", localStorage.getItem('token'));
      try {
        const response = await fetch(`http://localhost:8080/api/courses/${courseId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
          },
        });

        if (!response.ok) {
          throw new Error('No se pudo obtener los detalles del curso');
        }

        const data = await response.json();
        setCourseDetails(data);
      } catch (err: any) {
        setError(err.message);
      }
    };

    fetchCourseDetails();
  }, [courseId]);

  // Datos para el gráfico
  const data = {
    labels: ['Promedio de la clase'], // Etiqueta para el gráfico
    datasets: [
      {
        label: 'Promedio',
        data: [courseDetails?.classAverage], // El promedio de la clase que recibo del backend
        backgroundColor: '#4caf50', // Color de las barras
        borderColor: '#388e3c', // Color del borde de las barras
        borderWidth: 1,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      title: {
        display: true,
        text: 'Promedio de la Clase',
      },
      legend: {
        display: false, // Oculta la leyenda
      },
    },
  };

  return (
    <div className="course-details">
      {error && <p className="error">{error}</p>}
      {courseDetails && (
        <>
          <h2>{courseDetails.courseName}</h2>
          <p>{courseDetails.description}</p>

          {/* Sección común para ambos roles: Mostrar notas y estadísticas */}
          <div className="student-notes">
            <h3>Notas y Estadísticas</h3>

            {/* Muestra el gráfico del promedio de la clase */}
            <div className="class-average-graph">
              <Bar data={data} options={options} />
            </div>

            <p>Promedio de la clase: {courseDetails.classAverage}</p> {/* Muestra el promedio como texto */}

            {role === 'student' && (
              <>
                <h4>Mis Notas</h4>
                {typeof courseDetails.partialGrades === 'string' ? (
                  <p>{courseDetails.partialGrades}</p> // Muestra el mensaje si no hay notas parciales
                ) : (
                  <ul>
                    {courseDetails.partialGrades.map((grade: number, index: number) => (
                      <li key={index}>Nota Parcial {index + 1}: {grade}</li>
                    ))}
                  </ul>
                )}
                <p>Nota Final: {courseDetails.userGrade !== null ? courseDetails.userGrade : 'Pendiente'}</p> {/* Muestra la nota final o "Pendiente" */}
              </>
            )}
          </div>

          {/* Sección para estudiantes: Foro de Preguntas */}
          {role === 'student' && (
            <div className="student-forum">
              <h3>Foro de clase</h3>
              <textarea placeholder="Escribe tu pregunta..."></textarea>
              <button>Postear anuncio</button>
            </div>
          )}

          {/* Sección para profesores: Gestión de Notas y Archivos */}
          {role === 'professor' && (
            <div className="professor-management">
              <h3>Gestión de Notas y Archivos</h3>
              <button>Subir Notas</button>
              <button>Gestionar Archivos</button>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default CourseDetails;
