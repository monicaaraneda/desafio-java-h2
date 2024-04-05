CREATE TABLE users (
       id UUID PRIMARY KEY,
       name VARCHAR(255),
       email VARCHAR(255) UNIQUE,
       password VARCHAR(255)
);

CREATE TABLE phones (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        number VARCHAR(255),
        city_code INT,
        country_code VARCHAR(255),
        user_id UUID,
        FOREIGN KEY (user_id) REFERENCES users(id)
);
