import React, { useState } from 'react';

const Login: React.FC = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [token, setToken] = useState<string | null>(null);

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        const response = await fetch('http://localhost:8080/api/users/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password }),
        });

        if (response.ok) {
            const data = await response.text(); // El token se devuelve como texto
            setToken(data); // Guarda el token
            setMessage('Inicio de sesión exitoso');
        } else {
            setMessage('Credenciales incorrectas');
        }
    };

    return (
        <form onSubmit={handleLogin}>
            <h2>Iniciar Sesión</h2>
            <div>
                <label>Email:</label>
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>Contraseña:</label>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
            </div>
            <button type="submit">Iniciar Sesión</button>
            {message && <p>{message}</p>}
        </form>
    );
};

export default Login;
