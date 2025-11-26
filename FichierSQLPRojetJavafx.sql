CREATE DATABASE IF NOT EXISTS store;
USE store;

DROP TABLE IF EXISTS products;

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    purprice DOUBLE,
    sellprice DOUBLE,
    discprice DOUBLE,
    nbitems INT,
    type VARCHAR(20),
    size VARCHAR(20)
);

INSERT INTO products(name, purprice, sellprice, discprice, nbitems, type, size)
VALUES
("T-Shirt Blue", 70, 100, 0, 0, "Clothes", "40"),
("Dress Red", 90, 120, 0, 0, "Clothes", "38"),
("Nike AirMax", 30, 50, 0, 0, "Shoes", "42"),
("Adidas Run", 50, 70, 0, 0, "Shoes", "50"),
("Necklace Gold", 20, 30, 0, 0, "Accessories", "-"),
("Necklace paGold", 30, 40, 0, 0, "Accessories", "-");
