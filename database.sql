-- create the database

create database womenstore;

-- table creatation

CREATE TABLE Product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    price DOUBLE,
    nbItems INT,
    category VARCHAR(255)
);
