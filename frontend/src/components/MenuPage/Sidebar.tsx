import React from 'react';
import { Link } from 'react-router-dom';
import './Sidebar.css';

const Sidebar: React.FC = () => {
  return (
    <div className="sidebar">
      <h2>Navegación</h2>
      <ul>
        <li><Link to="/perfil">Perfil</Link></li>
        <li><Link to="/cursos">Cursos</Link></li>
        <li><Link to="/calendario">Calendario</Link></li>
        <li><Link to="/grupos">Grupos</Link></li>
        <li><Link to="/tomar-cursos">Inscribir cursos</Link></li>
      </ul>
    </div>
  );
};

export default Sidebar;