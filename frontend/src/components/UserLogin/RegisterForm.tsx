import React, { useState } from 'react';

const RoleForm: React.FC = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [username, setUsername] = useState('');
    const [role, setRole] = useState('');

    const roleMapping: { [key: string]: string } = {
        'estudiante': 'student',
        'profesor': 'professor'
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const userData = {
            email,
            password,
            username,
            role: roleMapping[role] || role // Convertir a inglés
        };

        try {
            const response = await fetch('http://localhost:8080/api/users/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData),
            });

            if (response.ok) {
                const result = await response.json();
                console.log('Registro exitoso:', result);
            } else {
                const error = await response.text();
                console.error('Error al registrarse:', error);
            }
        } catch (error) {
            console.error('Error en la solicitud:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>
                Email:
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
            </label>
            <label>
                Nombre de usuario:
                <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} required />
            </label>
            <label>
                Contraseña:
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
            </label>
            <label>
                Rol:
                <select value={role} onChange={(e) => setRole(e.target.value)} required>
                    <option value="">Seleccionar un rol:</option>
                    <option value="estudiante">Estudiante</option>
                    <option value="profesor">Profesor</option>
                </select>
            </label>
            <button type="submit">Registrar</button>
        </form>
    );
};

export default RoleForm;
