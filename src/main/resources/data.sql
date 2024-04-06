CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255),
                       username VARCHAR(255) UNIQUE,
                       email VARCHAR(255) UNIQUE,
                       password VARCHAR(255),
                       created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       modified TIMESTAMP NULL,
                       last_login TIMESTAMP NULL,
                       token VARCHAR(300) NULL,
                       role VARCHAR(255) NULL,
                       isactive BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE phones (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        number VARCHAR(255),
                        city_code INT,
                        country_code VARCHAR(255),
                        user_id UUID,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);
-- Insertar un usuario en la tabla 'users'
INSERT INTO users (id, name, username, email, password, role, isactive) VALUES
    ('a0b1c2d3-e4f5-6789-0123-456789abcdef', 'John Doe', 'johndoe', 'john.doe@example.com', '$2a$10$CvnTvMf85SNMku/jNKzlQenfpxz7V0lyiQxK94KREkexLMSf/OW0e', 'USER', FALSE);
INSERT INTO users (id, name, username, email, password, role, isactive) VALUES
    ('48dd711e-e197-449e-a0f2-956363dbff54', 'John Doe', 'moniara', 'moni.ara@example.com', '$2a$10$u2rd5wYjffNfgLSkoqxyf.yIYxqLR0OPMgZzBREEKt2HFhdm3wiNq', 'ADMIN', TRUE);

-- Insertar un teléfono en la tabla 'phones' asociado al usuario insertado anteriormente
INSERT INTO phones (number, city_code, country_code, user_id) VALUES
    ('1234567', 1, '57', 'a0b1c2d3-e4f5-6789-0123-456789abcdef');

-- Insertar otro teléfono para el mismo usuario
INSERT INTO phones (number, city_code, country_code, user_id) VALUES
    ('7654321', 2, '57', 'a0b1c2d3-e4f5-6789-0123-456789abcdef');

-- Insertar un teléfono en la tabla 'phones' asociado al usuario insertado anteriormente
INSERT INTO phones (number, city_code, country_code, user_id) VALUES
    ('31318888', 1, '56', '48dd711e-e197-449e-a0f2-956363dbff54');
