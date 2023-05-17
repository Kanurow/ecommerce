CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);
CREATE TABLE user_roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(50),
    role_id VARCHAR(50)
);

CREATE TABLE product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(50),
    price INT,
    quantity INT
);

CREATE TABLE favourite (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    user_id INT
);

CREATE TABLE promo_table (
    id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50),
    promo_amount INT
);

CREATE TABLE users_table (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    `password` VARCHAR(10000) NOT NULL ,
    mobile VARCHAR(100) UNIQUE,
    authorities VARCHAR(50),
    account_balance INT,
    voucher_balance INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');