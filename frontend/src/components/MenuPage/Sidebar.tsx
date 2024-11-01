import React from 'react';
import { Link } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import './Sidebar.css';

interface DecodedToken {
  userId: number;
  role: string; 
}

const getDecodedToken = () => {
  const token = localStorage.getItem('token');

  if (token) {
    try {
      const decoded: DecodedToken = jwtDecode(token);
      return decoded; 
    } catch (error) {
      console.error("Error decodificando el token", error);
    }
  }
  return null;
};

const Sidebar: React.FC = () => {
  const decodedToken = getDecodedToken(); // Obtener el token decodificado
  const userId = decodedToken ? decodedToken.userId : null;
  const role = decodedToken ? decodedToken.role : '';

  return (
    <div className="sidebar">
      <h2>Navegación</h2>
      <ul>
        {userId && <li><Link to={`/perfil/${userId}`}>Perfil</Link></li>}
        <li><Link to="/cursos">Cursos</Link></li>
        <li><Link to="/calendario">Calendario</Link></li>
        <li><Link to="/grupos">Grupos</Link></li>
        {role === 'student' && <li><Link to="/tomar-cursos">Inscribir cursos</Link></li>}
        {role === 'teacher' && <li><Link to="/aprobar-cursos">Aprobar cursos</Link></li>}
        </ul>
    </div>
  );
};

export default Sidebar;