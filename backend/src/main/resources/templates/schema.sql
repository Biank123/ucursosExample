CREATE DATABASE Ucursos;

-- Tabla de Usuarios
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('student', 'professor')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Cursos
CREATE TABLE courses (
    course_id SERIAL PRIMARY KEY,
    course_name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Intermedia para Usuarios y Cursos
CREATE TABLE user_courses (
    user_course_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    course_id INT NOT NULL REFERENCES courses(course_id) ON DELETE CASCADE,
    role_in_course VARCHAR(50) NOT NULL CHECK (role_in_course IN ('student', 'professor')),
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, course_id, role_in_course) -- Evita duplicados en la relación
);


-- Tabla de Contenidos del Curso (subidos desde el frontend)
CREATE TABLE course_contents (
    content_id SERIAL PRIMARY KEY,
    course_id INT NOT NULL,
    content_title VARCHAR(255) NOT NULL,
    content_type VARCHAR(50) NOT NULL,
    file_url VARCHAR(255),
    file_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- Tabla de mensajería
CREATE TABLE messages (
    message_id SERIAL PRIMARY KEY,
    sender_id INT REFERENCES users(user_id),
    receiver_id INT REFERENCES users(user_id),
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tablas de foros
CREATE TABLE forums (
    forum_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE posts (
    post_id SERIAL PRIMARY KEY,
    forum_id INT REFERENCES forums(forum_id),
    author_id INT REFERENCES users(user_id),
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE comments (
    comment_id SERIAL PRIMARY KEY,
    post_id INT REFERENCES posts(post_id),
    author_id INT REFERENCES users(user_id),
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Función que actualiza la columna updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para la tabla users
CREATE TRIGGER update_users_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Trigger para la tabla courses
CREATE TRIGGER update_courses_updated_at
BEFORE UPDATE ON courses
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Trigger para la tabla course_contents
CREATE TRIGGER update_course_contents_updated_at
BEFORE UPDATE ON course_contents
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Trigger para la tabla messages
CREATE TRIGGER update_messages_updated_at
BEFORE UPDATE ON messages
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Trigger para la tabla forums
CREATE TRIGGER update_forums_updated_at
BEFORE UPDATE ON forums
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Trigger para la tabla posts
CREATE TRIGGER update_posts_updated_at
BEFORE UPDATE ON posts
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Trigger para la tabla comments
CREATE TRIGGER update_comments_updated_at
BEFORE UPDATE ON comments
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Tabla de notas parciales
CREATE TABLE partial_grades (
    partial_grade_id SERIAL PRIMARY KEY,
    user_course_id INT NOT NULL REFERENCES user_courses(user_course_id) ON DELETE CASCADE,
    grade NUMERIC(5, 2) CHECK (grade >= 1.00 AND grade <= 7.00),
    UNIQUE (user_course_id, partial_grade_id) -- Asegura que cada nota parcial es única
);


-- Tabla de notas finales
CREATE TABLE final_grades (
    final_grade_id SERIAL PRIMARY KEY,
    user_course_id INT NOT NULL REFERENCES user_courses(user_course_id) ON DELETE CASCADE,
    final_grade NUMERIC(5, 2) CHECK (final_grade >= 1.00 AND final_grade <= 7.00),
    UNIQUE (user_course_id) -- Asegura que solo haya una nota final por curso por usuario
);
