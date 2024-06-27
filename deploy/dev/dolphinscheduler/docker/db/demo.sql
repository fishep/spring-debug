-- DROP DATABASE `demo`;
CREATE DATABASE IF NOT EXISTS `demo`;

USE `demo`;

-- create orders table
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` INT NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `last_update_at` DATETIMEV2 NOT NULL COMMENT '数据最后更新时间'
)
UNIQUE KEY(`id`)
DISTRIBUTED BY HASH(`id`) BUCKETS AUTO
PROPERTIES (
"replication_allocation" = "tag.location.default: 1"
);

-- create shipments table
DROP TABLE IF EXISTS `shipments`;
CREATE TABLE `shipments` (
  `id` INT NOT NULL,
	`order_id` INT NOT NULL,
  `city` VARCHAR(255) NOT NULL,
  `last_update_at` DATETIMEV2 NOT NULL COMMENT '数据最后更新时间'
)
UNIQUE KEY(`id`)
DISTRIBUTED BY HASH(`id`) BUCKETS AUTO
PROPERTIES (
"replication_allocation" = "tag.location.default: 1"
);

-- create products table
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` INT NOT NULL,
	`order_id` INT NOT NULL,
	`product_id` INT NOT NULL,
  `product_name` VARCHAR(255) NOT NULL,
	`cate_id` INT NOT NULL COMMENT '产品分类的id',
	`cate_name` VARCHAR(255) NOT NULL COMMENT '产品分类的名称',
  `last_update_at` DATETIMEV2 NOT NULL COMMENT '数据最后更新时间'
)
UNIQUE KEY(`id`)
DISTRIBUTED BY HASH(`id`) BUCKETS AUTO
PROPERTIES (
"replication_allocation" = "tag.location.default: 1"
);

DROP TABLE IF EXISTS `table_row_change_log`;
CREATE TABLE `table_row_change_log`  (
  `dt` DATEV2 NULL,
  `table_name` VARCHAR(100) NULL DEFAULT NULL,
  `pk_1` VARCHAR(100) NULL DEFAULT NULL,
  `pk_2` VARCHAR(100) NULL DEFAULT NULL,
  `pk_3` VARCHAR(100) NULL DEFAULT NULL,
  `op` CHAR(1) NULL DEFAULT NULL COMMENT 'c,r,u,d -- 创建，快照，更新，删除'
) 
DUPLICATE KEY(`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`)
DISTRIBUTED BY HASH(`dt`) BUCKETS AUTO
PROPERTIES (
"replication_allocation" = "tag.location.default: 1"
);

-- SELECT * FROM `orders`

-- 2024-05-31
-- 新增数据可能的情况
-- c        1
-- c u      2
-- c u d    3
-- 存量数据可能的情况
-- u  
-- u d
INSERT INTO `orders` (`id`, `price`, `last_update_at`) VALUES (1, 10.00, '2024-05-31 00:00:00');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-05-31', 'orders', 1, NULL, NULL, 'c');
INSERT INTO `orders` (`id`, `price`, `last_update_at`) VALUES (2, 20.00, '2024-05-31 00:00:00');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-05-31', 'orders', 2, NULL, NULL, 'c');
UPDATE `orders` SET `price` = 21.00, `last_update_at`='2024-05-31 12:00:00' WHERE id = 2;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-05-31', 'orders', 2, NULL, NULL, 'u');
INSERT INTO `orders` (`id`, `price`, `last_update_at`) VALUES (3, 30.00, '2024-05-31 00:00:00');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-05-31', 'orders', 3, NULL, NULL, 'c');
UPDATE `orders` SET `price` = 31.00, `last_update_at`='2024-05-31 12:00:00' WHERE id = 3;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-05-31', 'orders', 3, NULL, NULL, 'u');
DELETE FROM `orders` WHERE `id` = 3;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-05-31', 'orders', 3, NULL, NULL, 'd');


-- 2024-06-01
-- 新增数据可能的情况
-- c        4
-- c u      5
-- c u d    6
-- 存量数据可能的情况
-- u        1
-- u d      2
INSERT INTO `orders` (`id`, `price`, `last_update_at`) VALUES (4, 40.00, '2024-06-01 00:00:00');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'orders', 4, NULL, NULL, 'c');
INSERT INTO `orders` (`id`, `price`, `last_update_at`) VALUES (5, 50.00, '2024-06-01 00:00:00');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'orders', 5, NULL, NULL, 'c');
UPDATE `orders` SET `price` = 51.00, `last_update_at`='2024-06-01 12:00:00' WHERE id = 5;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'orders', 5, NULL, NULL, 'u');
INSERT INTO `orders` (`id`, `price`, `last_update_at`) VALUES (6, 60.00, '2024-06-01 00:00:00');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'orders', 6, NULL, NULL, 'c');
UPDATE `orders` SET `price` = 61.00, `last_update_at`='2024-06-01 12:00:00' WHERE id = 6;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'orders', 6, NULL, NULL, 'u');
DELETE FROM `orders` WHERE `id` = 6;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'orders', 6, NULL, NULL, 'd');

UPDATE `orders` SET `price` = 11.00, `last_update_at`='2024-06-01 12:00:00' WHERE id = 1;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'orders', 1, NULL, NULL, 'u');
UPDATE `orders` SET `price` = 22.00, `last_update_at`='2024-06-01 23:59:59' WHERE id = 2;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'orders', 2, NULL, NULL, 'u');
DELETE FROM `orders` WHERE `id` = 2;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'orders', 2, NULL, NULL, 'd');

-- 2024-06-02
-- 新增数据可能的情况
-- c        7
-- c u      8
-- c u d    9
-- 存量数据可能的情况
-- u        4
-- u d      5
INSERT INTO `orders` (`id`, `price`, `last_update_at`) VALUES (7, 70.00, '2024-06-02 00:00:00');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'orders', 7, NULL, NULL, 'c');
INSERT INTO `orders` (`id`, `price`, `last_update_at`) VALUES (8, 80.00, '2024-06-02 00:00:00');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'orders', 8, NULL, NULL, 'c');
UPDATE `orders` SET `price` = 81.00, `last_update_at`='2024-06-02 12:00:00' WHERE id = 8;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'orders', 8, NULL, NULL, 'u');
INSERT INTO `orders` (`id`, `price`, `last_update_at`) VALUES (9, 90.00, '2024-06-02 00:00:00');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'orders', 9, NULL, NULL, 'c');
UPDATE `orders` SET `price` = 91.00, `last_update_at`='2024-06-02 12:00:00' WHERE id = 9;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'orders', 9, NULL, NULL, 'u');
DELETE FROM `orders` WHERE `id` = 9;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'orders', 9, NULL, NULL, 'd');

UPDATE `orders` SET `price` = 41.00, `last_update_at`='2024-06-02 12:00:00' WHERE id = 4;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'orders', 4, NULL, NULL, 'u');
UPDATE `orders` SET `price` = 52.00, `last_update_at`='2024-06-02 23:59:59' WHERE id = 5;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'orders', 5, NULL, NULL, 'u');
DELETE FROM `orders` WHERE `id` = 5;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'orders', 5, NULL, NULL, 'd');

UPDATE `orders` SET `price` = 12.00, `last_update_at`='2024-06-02 23:59:59' WHERE id = 1;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'orders', 1, NULL, NULL, 'u');

-- SELECT * FROM `shipments`
-- 2024-06-01
INSERT INTO `shipments` (`id`, `order_id`, `city`, `last_update_at`) VALUES (1, 1,'beijing', '2024-06-01 00:00:00');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'shipments', 1, NULL, NULL, 'c');
UPDATE `shipments` SET `city`='wuhan',`last_update_at`='2024-06-01 12:00:00' WHERE `id`=1;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'shipments', 1, NULL, NULL, 'u');
-- DELETE FROM `shipments` WHERE `id` = 1;
-- INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'shipments', 1, NULL, NULL, 'd');

INSERT INTO `shipments` (`id`, `order_id`, `city`, `last_update_at`) VALUES (2, 2,'beijing', '2024-06-01 00:00:00');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'shipments', 2, NULL, NULL, 'c');

-- 2024-06-02
UPDATE `shipments` SET `city`='beijing',`last_update_at`='2024-06-02 12:00:00' WHERE `id`=1;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'shipments', 1, NULL, NULL, 'u');
UPDATE `shipments` SET `city`='wuhan',`last_update_at`='2024-06-02 12:00:00' WHERE `id`=2;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'shipments', 2, NULL, NULL, 'u');
-- DELETE FROM `shipments` WHERE `id` = 2;
-- INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'shipments', 2, NULL, NULL, 'd');




-- SELECT * FROM `products`
INSERT INTO `products` (`id`, `order_id`, `product_id`, `product_name`, `cate_id`, `cate_name`, `last_update_at`) VALUES (1, 1, 1, 'Beer',   1, 'Drink' ,'2024-06-01 00:00:00');
INSERT INTO `products` (`id`, `order_id`, `product_id`, `product_name`, `cate_id`, `cate_name`, `last_update_at`) VALUES (2, 1, 2, 'Cap',    2, 'Clothe', '2024-06-02 00:00:00');
INSERT INTO `products` (`id`, `order_id`, `product_id`, `product_name`, `cate_id`, `cate_name`, `last_update_at`) VALUES (3, 1, 3, 'Peanut', 3, 'Food'  , '2024-06-03 00:00:00');
INSERT INTO `products` (`id`, `order_id`, `product_id`, `product_name`, `cate_id`, `cate_name`, `last_update_at`) VALUES (4, 2, 1, 'Beer',   1, 'Drink' ,'2024-06-01 00:00:00');
INSERT INTO `products` (`id`, `order_id`, `product_id`, `product_name`, `cate_id`, `cate_name`, `last_update_at`) VALUES (5, 2, 4, 'Milk',   1, 'Drink', '2024-06-02 00:00:00');
INSERT INTO `products` (`id`, `order_id`, `product_id`, `product_name`, `cate_id`, `cate_name`, `last_update_at`) VALUES (6, 2, 3, 'Peanut', 3, 'Food'  , '2024-06-03 00:00:00');

-- SELECT * FROM `table_row_change_log`;
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'products', 1, NULL, NULL, 'c');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'products', 2, NULL, NULL, 'c');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-03', 'products', 3, NULL, NULL, 'c');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-01', 'products', 4, NULL, NULL, 'c');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-02', 'products', 5, NULL, NULL, 'c');
INSERT INTO `table_row_change_log` (`dt`, `table_name`, `pk_1`, `pk_2`, `pk_3`, `op`) VALUES ('2024-06-03', 'products', 6, NULL, NULL, 'c');








