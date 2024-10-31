import React from 'react';
import { Link } from 'react-router-dom';

const Landing: React.FC = () => {
  return (
    <div>
      <h1>Landing Page</h1>
      <Link to="/login">
        <button>Ir a Iniciar Sesión</button>
      </Link>
      <Link to="/register">
        <button>Ir a Registro</button>
      </Link>
    </div>
  );
};

export default Landing;