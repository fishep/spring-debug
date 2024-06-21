-- DROP DATABASE `ods_demo`;
CREATE DATABASE IF NOT EXISTS `ods_demo`;

USE `ods_demo`;

-- 创建拉链表
DROP TABLE IF EXISTS `orders_zip`;
CREATE TABLE `orders_zip` (
	`id` INT NOT NULL,
	`start_dt` DATETIMEV2,
	`end_dt` DATETIMEV2,
-- 	`price` DECIMAL(10,2) NOT NULL
	`price` DECIMAL(10,2)
-- 	,`last_update_at` DATETIMEV2 NOT NULL COMMENT '数据最后更新时间'
)ENGINE=OLAP
-- UNIQUE KEY(`start_dt`, `end_dt`, `id`)
-- 1105 - errCode = 2, detailMessage = Only unique table could be updated.
-- DUPLICATE KEY(`start_dt`, `end_dt`, `id`)
UNIQUE KEY(`id`,`start_dt`)
-- PARTITION BY RANGE(`end_dt`)
-- (
--     PARTITION p_999912 VALUES [('9999-12-01 00:00:00'), ('9999-12-31 23:59:59'))
-- )
DISTRIBUTED BY HASH(`id`) BUCKETS AUTO
PROPERTIES (
    "replication_allocation" = "tag.location.default: 1",
    "estimate_partition_size" = "1G"
);

-- SELECT * FROM `orders_zip`;
-- DELETE FROM orders_zip PARTITION p_999912 WHERE `id` > 0 ;


-- 初始化拉链表
insert into `orders_zip`(`start_dt`, `end_dt`, `id`, `price`)
select `last_update_at` as start_dt, '9999-12-31 00:00:00' as end_dt, `id`, `price` from `demo`.`orders`;

-- SELECT * FROM orders_zip;

-- 变更记录
SELECT * FROM `demo`.`table_row_change_log` where `demo`.`table_row_change_log`.`dt`='2024-06-02' AND `demo`.`table_row_change_log`.`table_name` = 'orders';
-- 变更记录的id
SELECT DISTINCT `pk_1` AS id FROM `demo`.`table_row_change_log` where `demo`.`table_row_change_log`.`dt`='2024-06-02' AND `demo`.`table_row_change_log`.`table_name` = 'orders';
SELECT DISTINCT `pk_1` AS id FROM `demo`.`table_row_change_log` where `demo`.`table_row_change_log`.`dt`='2024-06-02' AND `demo`.`table_row_change_log`.`table_name` = 'orders' AND `demo`.`table_row_change_log`.`op` IN ('c','u','d');


-- 拉链表, 变更数据，在拉链表有记录, 变更最后一条记录，task1：每天调度一次 
-- 1105 - errCode = 2, detailMessage = Only value columns of unique table could be updated
-- SELECT * FROM `orders_zip`;
UPDATE `orders_zip`
SET `orders_zip`.`end_dt` = IF(ISNULL(o.id), '2024-06-02', o.`last_update_at`)
FROM
(SELECT DISTINCT `pk_1` AS `id` FROM `demo`.`table_row_change_log` where `demo`.`table_row_change_log`.`dt`='2024-06-02' AND `demo`.`table_row_change_log`.`table_name` = 'orders' AND `demo`.`table_row_change_log`.`op` IN ('c','u','d')) AS cl
LEFT JOIN 
`demo`.`orders` as o
ON cl.`id` = o.`id`
WHERE `orders_zip`.`id` = cl.`id` AND `orders_zip`.`end_dt` = '9999-12-31 00:00:00'


-- 变更的数据，现在的样子
SELECT * FROM
(SELECT DISTINCT `pk_1` AS id FROM `demo`.`table_row_change_log` where `demo`.`table_row_change_log`.`dt`='2024-06-02' AND `demo`.`table_row_change_log`.`table_name` = 'orders' AND `demo`.`table_row_change_log`.`op` IN ('c','u','d')) AS cl
LEFT JOIN 
`demo`.`orders` as o
on cl.`id` = o.`id`;

-- 拉链表,增量数据 task2：每天调度一次 ，依赖task1
-- SELECT * FROM `orders_zip` ORDER BY `id`, `start_dt`;
INSERT INTO `orders_zip`(`start_dt`, `end_dt`, `id`, `price`)
SELECT 
IF(ISNULL(o.id),'2024-06-02',o.`last_update_at`) AS `start_dt`,
IF(ISNULL(o.id),'2024-06-02','9999-12-31 00:00:00') AS `end_dt`, 
cl.`id`, o.`price`
FROM
(SELECT DISTINCT `pk_1` AS id FROM `demo`.`table_row_change_log` where `demo`.`table_row_change_log`.`dt`='2024-06-02' AND `demo`.`table_row_change_log`.`table_name` = 'orders' AND `demo`.`table_row_change_log`.`op` IN ('c','u','d')) AS cl
LEFT JOIN 
`demo`.`orders` as o
on cl.`id` = o.`id`;




-- 大宽表 
DROP TABLE IF EXISTS `order_product`;
CREATE TABLE `order_product` (
  `order_id` INT NOT NULL,
  `price` DECIMAL(10,2) NULL,
  
	`city` VARCHAR(255) NULL,

	`product_id` INT NULL,
  `product_name` VARCHAR(255) NULL,
	`cate_id` INT NULL COMMENT '产品分类的id',
	`cate_name` VARCHAR(255) NULL COMMENT '产品分类的名称',
	
	`order_last_update_at` DATETIMEV2 NULL COMMENT '订单最后更新时间',
  `product_last_update_at` DATETIMEV2 NULL COMMENT '产品最后更新时间'
)
DUPLICATE KEY(`order_id`,`price`,`city`,`product_id`,`product_name`,`cate_id`,`cate_name`,`order_last_update_at`,`product_last_update_at`)
DISTRIBUTED BY HASH(`order_id`) BUCKETS AUTO
PROPERTIES (
"replication_allocation" = "tag.location.default: 1"
);

-- SELECT * FROM `order_product`

-- 初始化
INSERT INTO `order_product`(`order_id`, `price`, `city`, `product_id`, `product_name`, `cate_id`, `cate_name`, `order_last_update_at`, `product_last_update_at`)
SELECT o.id as `order_id`, o.`price`, s.`city`, p.`product_id`,p.`product_name`,p.`cate_id`,p.`cate_name`,o.`last_update_at` as order_last_update_at, p.`last_update_at` as product_last_update_at
FROM
`demo`.`orders` o
LEFT JOIN `demo`.`shipments` s ON (o.id = s.order_id)
LEFT JOIN `demo`.`products` p ON (o.id = s.order_id)




