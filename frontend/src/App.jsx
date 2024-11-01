import Landing from "./components/LandingPage/Landing.tsx";
import Login from "./components/UserLogin/LoginForm.tsx";
import Register from "./components/UserLogin/RegisterForm.tsx";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Sidebar from "./components/MenuPage/Sidebar.tsx";
import Profile from './components/UserProfile/ProfileMenu.tsx';
import Courses from './components/MenuPage/Courses.tsx';
import Calendar from './components/MenuPage/Calendar.tsx';
import Groups from './components/MenuPage/Groups.tsx';
import TakeCourses from './components/MenuPage/TakeCourses.tsx';


function App() {
  return (
    <Router>
      <Sidebar />
      <Routes>
        <Route path="/" element={<Landing />}/>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route path="/perfil" element={<Profile />} />
        <Route path="/cursos" element={<Courses />} />
        <Route path="/calendario" element={<Calendar />} />
        <Route path="/grupos" element={<Groups />} />
        <Route path="/tomar-cursos" element={<TakeCourses />} />
      </Routes>
    </Router>
  );
}

export default App;
