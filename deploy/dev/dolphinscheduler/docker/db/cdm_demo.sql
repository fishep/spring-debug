-- DROP DATABASE `cdm_demo`;
CREATE DATABASE IF NOT EXISTS `cdm_demo`;

USE `cdm_demo`;

-- DIM（Dimension�?

DROP TABLE IF EXISTS dim_city;
CREATE TABLE dim_city(
  `city` VARCHAR(255) NOT NULL
)
UNIQUE KEY(`city`)
COMMENT 'city 维度�?
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

-- 初始�?
INSERT INTO dim_city(`city`) VALUES ('shanghai');
INSERT INTO dim_city(`city`) VALUES ('shenzhen');
INSERT INTO dim_city(`city`) VALUES ('guanzhou');
-- task1：每天调度一�?
INSERT INTO dim_city(`city`) SELECT city FROM demo.`shipments` ;




-- dim_cate

DROP TABLE IF EXISTS `dim_cate`;
CREATE TABLE `dim_cate` (
	`cate_id` INT NOT NULL COMMENT '产品分类的id',
	`cate_name` VARCHAR(255) NOT NULL COMMENT '产品分类的名�?
)
UNIQUE KEY(`cate_id`)
DISTRIBUTED BY HASH(`cate_id`) BUCKETS AUTO
PROPERTIES (
"replication_allocation" = "tag.location.default: 1"
);

-- SELECT * FROM demo.`products` ;
-- SELECT DISTINCT `cate_id`,`cate_name` FROM demo.`products` ;
-- SELECT * FROM `dim_cate` ORDER BY cate_id;

-- 初始�?
INSERT INTO dim_cate(`cate_id`,`cate_name`) VALUES (1, 'Clothe') ;
INSERT INTO dim_cate(`cate_id`,`cate_name`) VALUES (2, 'Drink') ;
-- task1：每天调度一�?
INSERT INTO dim_cate(`cate_id`,`cate_name`) SELECT `cate_id`,`cate_name` FROM demo.`products` ;





-- DWD层（Data Warehouse Detail Layer�?


-- DWS层（Data Warehouse Service Layer�?








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










