DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS direccion;
DROP TABLE IF EXISTS repartidor;
DROP TABLE IF EXISTS rutaReparto;
DROP TABLE IF EXISTS envio;

CREATE TABLE IF NOT EXISTS cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS direccion (
    id_direccion INT AUTO_INCREMENT PRIMARY KEY,
    calle VARCHAR(100) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    id_cliente INT,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

CREATE TABLE IF NOT EXISTS repartidor (
    id_repartidor INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS rutaReparto (
    id_ruta INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    id_repartidor INT,
    FOREIGN KEY (id_repartidor) REFERENCES repartidor(id_repartidor)
);

CREATE TABLE IF NOT EXISTS envio (
    id_envio INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(200),
    estado VARCHAR(200),
    id_cliente INT NOT NULL,
    id_direccion_origen INT NOT NULL,
    id_direccion_destino INT NOT NULL,
    id_repartidor INT,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_direccion_origen) REFERENCES direccion(id_direccion),
    FOREIGN KEY (id_direccion_destino) REFERENCES direccion(id_direccion),
    FOREIGN KEY (id_repartidor) REFERENCES repartidor(id_repartidor)
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    image VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE
    CURRENT_TIMESTAMP,
    last_password_change_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

