import React from 'react';
import { Link } from 'react-router-dom';
import jwt_decode from 'jwt-decode';
import './Sidebar.css';

interface DecodedToken {
  userId: number;
}

const getUserIdFromToken = () => {
  const token = localStorage.getItem('token');

  if (token) {
    try {
      const decoded: DecodedToken = jwt_decode(token);
      return decoded.userId; 
    } catch (error) {
      console.error("Error decodificando el token", error);
    }
  }
  return null;
};

const Sidebar: React.FC = () => {
  const userId = getUserIdFromToken(); // Obtener ID del token

  return (
    <div className="sidebar">
      <h2>Navegación</h2>
      <ul>
        {userId && <li><Link to={`/perfil/${userId}`}>Perfil</Link></li>}
        <li><Link to="/cursos">Cursos</Link></li>
        <li><Link to="/calendario">Calendario</Link></li>
        <li><Link to="/grupos">Grupos</Link></li>
        <li><Link to="/tomar-cursos">Inscribir cursos</Link></li>
      </ul>
    </div>
  );
};

export default Sidebar;