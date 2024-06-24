-- DROP DATABASE `cdm_demo`;
CREATE DATABASE IF NOT EXISTS `cdm_demo`;

USE `cdm_demo`;

-- DIM（Dimension）

DROP TABLE IF EXISTS dim_city;
CREATE TABLE dim_city(
-- 	`id` INT NOT NULL COMMENT 'id',
    `city` VARCHAR(255) NOT NULL
)
-- UNIQUE KEY(`id`)
    UNIQUE KEY(`city`)
COMMENT 'city 纬度'
DISTRIBUTED BY HASH(`city`) BUCKETS AUTO
PROPERTIES (
	"replication_allocation" = "tag.location.default: 1",
	"estimate_partition_size" = "1G"
);


-- SELECT * FROM demo.`shipments` ;
-- SELECT city FROM demo.`shipments` ;
-- SELECT * FROM `dim_city` ;
-- DELETE FROM `dim_city` WHERE `city` = 'wuhan';
-- DELETE FROM `dim_city` WHERE `city` = 'beijing';

-- 初始化
INSERT INTO dim_city(`city`) VALUES ('shanghai');
INSERT INTO dim_city(`city`) VALUES ('shenzhen');
INSERT INTO dim_city(`city`) VALUES ('guanzhou');

-- 和ods层一起初始化
INSERT INTO dim_city(`city`) SELECT city FROM demo.`shipments`;

-- SELECT city FROM ods_demo.`shipments_zip` ;

-- task1：每天调度一次, 先删后加
TRUNCATE TABLE dim_city;
INSERT INTO dim_city(`city`) VALUES ('shanghai');
INSERT INTO dim_city(`city`) VALUES ('shenzhen');
INSERT INTO dim_city(`city`) VALUES ('guanzhou');
INSERT INTO dim_city(`city`) SELECT city FROM demo.`shipments`;


-- dim_cate

DROP TABLE IF EXISTS `dim_cate`;
CREATE TABLE `dim_cate` (
                            `cate_id` INT NOT NULL COMMENT '产品分类的id',
                            `cate_name` VARCHAR(255) NOT NULL COMMENT '产品分类的名称'
)
    UNIQUE KEY(`cate_id`)
DISTRIBUTED BY HASH(`cate_id`) BUCKETS AUTO
PROPERTIES (
"replication_allocation" = "tag.location.default: 1"
);

-- SELECT * FROM demo.`products` ;
-- SELECT DISTINCT `cate_id`,`cate_name` FROM demo.`products` ;
-- SELECT * FROM `dim_cate` ORDER BY cate_id;

-- 初始化
INSERT INTO dim_cate(`cate_id`,`cate_name`) VALUES (1, 'Clothe') ;
INSERT INTO dim_cate(`cate_id`,`cate_name`) VALUES (2, 'Drink') ;
INSERT INTO dim_cate(`cate_id`,`cate_name`) SELECT `cate_id`,`cate_name` FROM demo.`products` ;

-- task1：每天调度一次, 先删后加
TRUNCATE TABLE dim_cate;
INSERT INTO dim_cate(`cate_id`,`cate_name`) VALUES (1, 'Clothe') ;
INSERT INTO dim_cate(`cate_id`,`cate_name`) VALUES (2, 'Drink') ;
INSERT INTO dim_cate(`cate_id`,`cate_name`) SELECT `cate_id`,`cate_name` FROM demo.`products` ;



-- DWD层（Data Warehouse Detail Layer）
-- 大宽表
DROP TABLE IF EXISTS `dwd_order_product`;
CREATE TABLE `dwd_order_product` (
                                     `order_id` INT NOT NULL,
                                     `product_id` INT NULL,

                                     `price` DECIMAL(10,2) NULL,

                                     `city` VARCHAR(255) NULL,

                                     `product_name` VARCHAR(255) NULL,
                                     `cate_id` INT NULL COMMENT '产品分类的id',
                                     `cate_name` VARCHAR(255) NULL COMMENT '产品分类的名称',

                                     `order_last_update_at` DATETIMEV2 NULL COMMENT '订单最后更新时间',
                                     `product_last_update_at` DATETIMEV2 NULL COMMENT '产品最后更新时间'
)
    UNIQUE KEY(`order_id`,`product_id`)
DISTRIBUTED BY HASH(`order_id`) BUCKETS AUTO
PROPERTIES (
"replication_allocation" = "tag.location.default: 1"
);

-- SELECT * FROM `dwd_order_product`;

-- 初始化
INSERT INTO `dwd_order_product`(`order_id`, `product_id`, `price`, `city`, `product_name`, `cate_id`, `cate_name`, `order_last_update_at`, `product_last_update_at`)
SELECT o.id as `order_id`, p.`product_id`, o.`price`, s.`city`, p.`product_name`,p.`cate_id`,p.`cate_name`,o.`last_update_at` as order_last_update_at, p.`last_update_at` as product_last_update_at
FROM
    `demo`.`orders` o
        LEFT JOIN `demo`.`shipments` s ON (o.id = s.order_id)
        LEFT JOIN `demo`.`products` p ON (o.id = s.order_id)

-- 增量聚合，先删除变更数据，在插入变更数据


-- DWS层（Data Warehouse Service Layer）

-- dws_order_product
-- 按月，年。。。纬度聚合









/*
CREATE TABLE IF NOT EXISTS test (
  `c1` int NOT NULL DEFAULT "1",
  `c2` int NOT NULL DEFAULT "4"
) ENGINE=OLAP
UNIQUE KEY(`c1`)
PARTITION BY LIST (`c1`)
(
PARTITION p1 VALUES IN ("1","2","3"),
PARTITION p2 VALUES IN ("4","5","6")
)
DISTRIBUTED BY HASH(`c1`) BUCKETS auto
PROPERTIES (
  "replication_allocation" = "tag.location.default: 1"
);
INSERT OVERWRITE table test VALUES (1, 2);
*/










