import React from 'react';
import './DeleteAccountForm.css';

const DeleteAccountForm: React.FC = () => {
    const handleAccountDelete = (e: React.FormEvent) => {
      e.preventDefault();
      // Lógica para enviar solicitud de eliminación de cuenta
      console.log("Cuenta eliminada");
    };
  
    return (
      <form className="delete-account-form" onSubmit={handleAccountDelete}>
        <h3>Borrar Cuenta</h3>
        <p>¿Estás seguro de que quieres borrar tu cuenta? Esta acción no se puede deshacer.</p>
        <button type="submit" style={{ backgroundColor: 'red', color: 'white' }}>
          Confirmar Borrado
        </button>
      </form>
    );
  };
  
  export default DeleteAccountForm;