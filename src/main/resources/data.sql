INSERT IGNORE INTO cliente (nombre, email) VALUES ('Juan Pérez', 'juan.perez@email.com');
INSERT IGNORE INTO cliente (nombre, email) VALUES ('María López', 'maria.lopez@email.com');

INSERT IGNORE INTO repartidor (nombre, telefono) VALUES ('Carlos García', '600123456');
INSERT IGNORE INTO repartidor (nombre, telefono) VALUES ('Ana Martínez', '611987654');

INSERT IGNORE INTO envio (descripcion, direccion_origen, direccion_destino, estado, id_cliente, id_repartidor)
VALUES ('Paquete pequeño - documentos','Calle Mayor 10, Madrid','Avenida Sol 25, Madrid','pendiente',1,1);
INSERT IGNORE INTO envio (descripcion, direccion_origen, direccion_destino, estado, id_cliente, id_repartidor)
VALUES ('Caja mediana - ropa','Calle Luna 5, Sevilla','Calle Real 18, Sevilla','en_ruta',2,2);

INSERT IGNORE INTO ruta_reparto (fecha, id_repartidor) VALUES ('2026-02-20', 1);
INSERT IGNORE INTO ruta_reparto (fecha, id_repartidor) VALUES ('2026-02-21', 2);

-- Insertar datos de ejemplo para 'users'. La contraseña de cada usuario es password
INSERT IGNORE INTO users (id, username, password, enabled, first_name, last_name, image, created_date, last_modified_date, last_password_change_date) VALUES
(1, 'admin', '$2b$12$FVRijCavVZ7Qt15.CQssHe9m/6eLAdjAv0PiOKFIjMU161wApxzye', true, 'Admin', 'User', '/images/admin.jpg', NOW(), NOW(), NOW()),
(2, 'manager', '$2b$12$FVRijCavVZ7Qt15.CQssHe9m/6eLAdjAv0PiOKFIjMU161wApxzye', true, 'Manager', 'User', '/images/manager.jpg', NOW(), NOW(), NOW()),
(3, 'normal', '$2b$12$FVRijCavVZ7Qt15.CQssHe9m/6eLAdjAv0PiOKFIjMU161wApxzye', true, 'Regular', 'User', '/images/user.jpg', NOW(), NOW(), NOW());

-- Asignar el rol de administrador al usuario con id 1
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (1, 1);

INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (2, 2);
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (3, 3);