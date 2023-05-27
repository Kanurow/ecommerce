use ecommerce;
CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);
CREATE TABLE user_roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(50),
    role_id VARCHAR(50)
);




CREATE TABLE favourite (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    user_id INT
);
CREATE TABLE cart_table (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    user_id INT
);


CREATE TABLE product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(50),
    price INT,
    quantity INT
);
CREATE TABLE users_table (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    `password` VARCHAR(10000) NOT NULL ,
    mobile VARCHAR(100) ,
    authorities VARCHAR(50),
    account_balance INT NOT NULL,
    voucher_balance INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);








CREATE TABLE promo_table (
    id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50),
    promo_amount INT
);



INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');











CREATE TABLE cart_checkout (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_address VARCHAR(255) NOT NULL,
  price DOUBLE NOT NULL,
  quantity INT NOT NULL,
  user_id INT NOT NULL
);

CREATE TABLE cart_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_name VARCHAR(255) NOT NULL,
  price DOUBLE NOT NULL,
  quantity INT NOT NULL,
  subtotal DOUBLE NOT NULL,
  cart_checkout_id BIGINT,
  FOREIGN KEY (cart_checkout_id) REFERENCES cart_checkout(id)
);
