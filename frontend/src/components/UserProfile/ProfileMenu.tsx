import React, { useState } from 'react';
import ChangePasswordForm from './ChangePasswordForm.tsx';
import DeleteAccountForm from './DeleteAccountForm.tsx';

const UserProfile: React.FC = () => {
  const [showChangePassword, setShowChangePassword] = useState(false);
  const [showDeleteAccount, setShowDeleteAccount] = useState(false);

  const toggleChangePasswordForm = () => {
    setShowChangePassword(!showChangePassword);
    setShowDeleteAccount(false); // cierra el otro formulario si está abierto
  };

  const toggleDeleteAccountForm = () => {
    setShowDeleteAccount(!showDeleteAccount);
    setShowChangePassword(false); // cierra el otro formulario si está abierto
  };

  const toggleLogout = () => {
    // Agregar conexión con backend para cierre de sesión
  }

  return (
    <div>
      <h2>Perfil de Usuario</h2>
      {/* Sección de Información */}
      <div>
        <p>Nombre de usuario: <strong>nombre_usuario</strong></p>
        <p>Email: <strong>email_usuario@ejemplo.com</strong></p>
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