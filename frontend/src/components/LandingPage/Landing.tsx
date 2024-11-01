import React from 'react';
import { Link } from 'react-router-dom';
import './Landing.css';

const Landing: React.FC = () => {
  return (
    <div className="landing-page">
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