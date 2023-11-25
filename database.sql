-- create the database

create database womenstore;

-- table creatation

CREATE TABLE Product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    price DOUBLE,
    nbItems INT,
    type ENUM('Clothes', 'Shoes', 'Accessories'),
    size INT,
);

-- table transaction

CREATE TABLE Transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE,
    total DOUBLE
);

-- insert data

-- clothes
INSERT INTO Product (name, price, nbItems, type, size) VALUES ('T-shirt', 20.00, 50, 'Clothes', 38);
INSERT INTO Product (name, price, nbItems, type, size) VALUES ('Jeans', 35.00, 40, 'Clothes', 38);

-- Shoes
INSERT INTO Product (name, price, nbItems, type, size) VALUES ('Running Shoes', 60.00, 30, 'Shoes', 38);
INSERT INTO Product (name, price, nbItems, type, size) VALUES ('High Heels', 80.00, 20, 'Shoes', 38);


-- Accessories
INSERT INTO Product (name, price, nbItems, type) VALUES ('Sunglasses', 25.00, 50, 'Accessories');
INSERT INTO Product (name, price, nbItems, type) VALUES ('Handbag', 45.00, 30, 'Accessories');
