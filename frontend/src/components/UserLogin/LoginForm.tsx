import React, { useState } from 'react';

const Login: React.FC = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [token, setToken] = useState<string | null>(null);

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        
        try {
            const response = await fetch('http://localhost:8080/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
                credentials: "include",
            });

            if (response.ok) {
                const data = await response.json();
                setToken(data.token); // Guarda el token si es `{ token: string }`
                localStorage.setItem("token", data.token);
                setMessage('Inicio de sesión exitoso');
            } else {
                setMessage('Credenciales incorrectas');
            }
        } catch (error) {
            console.error("Error en la solicitud:", error);
            setMessage('Error al conectar con el servidor');
        }

        // Muestra el mensaje al usuario
        alert(message);
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
            <p>¿Olvidaste tu contraseña?</p>
        </form>
    );
};

export default Login;
