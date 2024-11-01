import React, { useState } from 'react';
import { jwtDecode } from 'jwt-decode';
import { useParams, useNavigate } from 'react-router-dom';
import './DeleteAccountForm.css';

interface TokenPayload {
  userId: string;
}

const DeleteAccountForm: React.FC = () => {
  const { userId: userIdFromParams } = useParams<{ userId: string }>();
  const [reason, setReason] = useState('');
  const [customReason, setCustomReason] = useState('');
  const navigate = useNavigate();

  // Obtener el ID del usuario desde el token JWT
  const getUserId = () => {
    const token = localStorage.getItem('token'); 
    if (token) {
      const decoded: TokenPayload = jwtDecode(token);
      return decoded.userId;
    }
    return userIdFromParams;
  };

  const handleAccountDelete = async (e: React.FormEvent) => {
    e.preventDefault();
    const userId = getUserId();

    if (!userId) {
      console.error("No se pudo obtener el ID del usuario.");
      return;
    }

    const deleteReason = reason === 'other' ? customReason : reason;

    try {
      const response = await fetch(`http://localhost:8080/api/users/${userId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify({ reason: deleteReason }),
      });

      if (response.ok) {
        console.log("Cuenta eliminada");
        alert('Cuenta eliminada con éxito.');
        navigate('/');
      } else {
        console.error("Error al eliminar la cuenta");
        alert('Error eliminando la cuenta');
      }
    } catch (error) {
      console.error("Hubo un problema con la solicitud de eliminación:", error);
    }
  };

  return (
    <form className="delete-account-form" onSubmit={handleAccountDelete}>
      <h3>Borrar Cuenta</h3>
      <p>¿Estás seguro de que quieres borrar tu cuenta? Esta acción no se puede deshacer.</p>
      
      <label htmlFor="reason">Motivo de la eliminación:</label>
      <select
        id="reason"
        value={reason}
        onChange={(e) => setReason(e.target.value)}
        required
      >
        <option value="">Selecciona un motivo</option>
        <option value="privacy">Preocupaciones de privacidad</option>
        <option value="notUseful">No encuentro útil la plataforma</option>
        <option value="notUse">No uso la cuenta</option>
        <option value="other">Otro</option>
      </select>
      
      {reason === 'other' && (
        <input
          type="text"
          placeholder="Escribe tu motivo"
          value={customReason}
          onChange={(e) => setCustomReason(e.target.value)}
          required
        />
      )}

      <button type="submit" style={{ backgroundColor: 'red', color: 'white' }}>
        Confirmar Borrado
      </button>
    </form>
  );
};

export default DeleteAccountForm;