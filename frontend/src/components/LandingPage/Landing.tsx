import React from 'react';
import { Link } from 'react-router-dom';
import './Landing.css';

const Landing: React.FC = () => {
  return (
    <div className="landing-page">
      <h1>Para acceder a las funcionalidades, inicia sesión con tu cuenta o regístrate</h1>
      <Link to="/login">
        <button>Ir a Iniciar Sesión</button>
      </Link>
      <Link to="/register">
        <button>Ir al Registro de cuenta</button>
      </Link>
    </div>
  );
};

export default Landing;