# 基于docker部署mysql服务
> debezium/example-mysql:1.1   mysql 5.7


### MySQL
```shell
#mysql 5.7
docker compose -p service up mysql -d

docker exec -it mysql bash

mysql -uroot -proot

```
```mysql
CREATE DATABASE IF NOT EXISTS app_db;
USE app_db;
CREATE USER 'app_db' IDENTIFIED BY 'app_db';
GRANT ALL ON app_db.* TO app_db;
GRANT REPLICATION SLAVE ON *.* TO 'app_db'@'%';
GRANT REPLICATION CLIENT ON *.* TO 'app_db'@'%';
ALTER USER 'app_db'@'%' IDENTIFIED WITH 'mysql_native_password' BY 'app_db';
FLUSH PRIVILEGES;

-- create orders table
CREATE TABLE `orders` (
                          `id` INT NOT NULL,
                          `price` DECIMAL(10,2) NOT NULL,
                          PRIMARY KEY (`id`)
);

-- insert records
INSERT INTO `orders` (`id`, `price`) VALUES (1, 4.00);
INSERT INTO `orders` (`id`, `price`) VALUES (2, 100.00);

-- create shipments table
CREATE TABLE `shipments` (
                             `id` INT NOT NULL,
                             `city` VARCHAR(255) NOT NULL,
                             PRIMARY KEY (`id`)
);

-- insert records
INSERT INTO `shipments` (`id`, `city`) VALUES (1, 'beijing');
INSERT INTO `shipments` (`id`, `city`) VALUES (2, 'xian');

-- create products table
CREATE TABLE `products` (
                            `id` INT NOT NULL,
                            `product` VARCHAR(255) NOT NULL,
                            PRIMARY KEY (`id`)
);

-- insert records
INSERT INTO `products` (`id`, `product`) VALUES (1, 'Beer');
INSERT INTO `products` (`id`, `product`) VALUES (2, 'Cap');
INSERT INTO `products` (`id`, `product`) VALUES (3, 'Peanut');


select * from orders;
select * from shipments;
select * from products;
-- update records
INSERT INTO orders (id, price) VALUES (3, 100.00);
ALTER TABLE orders ADD amount varchar(100) NULL;
UPDATE orders SET price=100.00, amount=100.00 WHERE id=1;
DELETE FROM orders WHERE id=2;

```