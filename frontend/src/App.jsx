import Landing from "./components/LandingPage/Landing.tsx";
import Login from "./components/UserLogin/LoginForm.tsx";
import Register from "./components/UserLogin/RegisterForm.tsx";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Landing />}/>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        
      </Routes>
    </Router>
  );
}

export default App;
