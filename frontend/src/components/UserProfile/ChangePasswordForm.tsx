import React from 'react';
import './ChangePasswordForm.css';

const ChangePasswordForm: React.FC = () => {
    const handlePasswordChange = (e: React.FormEvent) => {
      e.preventDefault();
      // Lógica para enviar solicitud de cambio de contraseña
      console.log("Contraseña cambiada");
    };
  
    return (
      <form className="change-password-form" onSubmit={handlePasswordChange}>
        <h3>Cambiar Contraseña</h3>
        <label>
          Contraseña actual:
          <input type="password" name="currentPassword" required />
        </label>
        <label>
          Nueva contraseña:
          <input type="password" name="newPassword" required />
        </label>
        <label>
          Confirmar nueva contraseña:
          <input type="password" name="confirmPassword" required />
        </label>
        <button type="submit">Guardar Cambios</button>
      </form>
    );
  };
  
  export default ChangePasswordForm;