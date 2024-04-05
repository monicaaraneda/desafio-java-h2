-- Insertar un usuario en la tabla 'users'
INSERT INTO users (id, name, email, password) VALUES
    ('a0b1c2d3-e4f5-6789-0123-456789abcdef', 'Juan Rodriguez', 'juan@rodriguez.org', 'hunter2');

-- Insertar un teléfono en la tabla 'phones' asociado al usuario insertado anteriormente
INSERT INTO phones (number, city_code, country_code, user_id) VALUES
    ('1234567', 1, '57', 'a0b1c2d3-e4f5-6789-0123-456789abcdef');

-- Insertar otro teléfono para el mismo usuario
INSERT INTO phones (number, city_code, country_code, user_id) VALUES
    ('7654321', 2, '57', 'a0b1c2d3-e4f5-6789-0123-456789abcdef');