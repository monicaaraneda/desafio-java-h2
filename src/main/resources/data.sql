-- Creación de la tabla 'users'
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
    role VARCHAR(255),
    isactive BOOLEAN NOT NULL DEFAULT TRUE
);

-- Creación de la tabla 'phones'
CREATE TABLE phones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(255),
    city_code INT,
    country_code VARCHAR(255),
    user_id UUID,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

SELECT H2VERSION() FROM DUAL;