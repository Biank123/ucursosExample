import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import './LoginForm.css';

const Login: React.FC = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();
    

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
                const token = data.token; 
                localStorage.setItem("token", data.token);
                setMessage('Inicio de sesión exitoso');

                // Comprueba si el token no es nulo
                if (token) {
                    const decoded: { userId: string } = jwtDecode(token);
                    navigate(`/perfil/${decoded.userId}`);
                }
                
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
        <form className='loginForm' onSubmit={handleLogin}>
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
