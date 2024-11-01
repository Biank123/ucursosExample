import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ChangePasswordForm from './ChangePasswordForm';
import DeleteAccountForm from './DeleteAccountForm';
import './ProfileMenu.css';

interface User {
  user_id: number;
  username: string;
  email: string;
  role: string;
}

const UserProfile: React.FC = () => {
  const [showChangePassword, setShowChangePassword] = useState(false);
  const [showDeleteAccount, setShowDeleteAccount] = useState(false);
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const { id } = useParams<{ id: string }>();
  const userId = parseInt(id!);

  const toggleChangePasswordForm = () => {
    setShowChangePassword(!showChangePassword);
    setShowDeleteAccount(false); // cierra el otro formulario si está abierto
  };

  const toggleDeleteAccountForm = () => {
    setShowDeleteAccount(!showDeleteAccount);
    setShowChangePassword(false); // cierra el otro formulario si está abierto
  };

  const toggleLogout = () => {
    console.log("Cerrando sesión...");
  }

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/users/${userId}`);
        
        if (!response.ok) {
          throw new Error('Error fetching user data');
        }

        const data = await response.json();
        setUser(data);
      } catch (err: any) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchUserData();
  }, [userId]);

  if (loading) return <div>Cargando...</div>;
  if (error) return <div>Error: {error}</div>;
  
  if (!user) return <div>No se encontró el usuario.</div>;

  return (
    <div className="profile-container">
      <h2>Perfil de Usuario</h2>
      {/* Sección de Información */}
      <div>
        <p>Nombre de usuario: <strong>{user.username}</strong></p>
        <p>Email: <strong>{user.email}</strong></p>
        <p><strong>Rol:</strong> {user.role}</p>
      </div>

      {/* Botón para cambiar la contraseña */}
      <button onClick={toggleChangePasswordForm}>
        Cambiar Contraseña
      </button>
      {showChangePassword && <ChangePasswordForm />}

      {/* Botón para cerrar sesión */}
      <button onClick={toggleLogout}> Cerrar Sesión </button>

      {/* Botón para borrar la cuenta */}
      <button onClick={toggleDeleteAccountForm}>
        Borrar la Cuenta
      </button>
      {showDeleteAccount && <DeleteAccountForm />}

    </div>
  );
};

export default UserProfile;