-- DROP DATABASE `ods_demo`;
CREATE DATABASE IF NOT EXISTS `ods_demo`;

USE `ods_demo`;

-- 创建拉链表
DROP TABLE IF EXISTS `orders_zip`;
CREATE TABLE `orders_zip` (
        `start_dt` DATETIMEV2,
        `end_dt` DATETIMEV2,
        `order_id` INT,
				`price` DECIMAL(10,2)
)ENGINE=OLAP
UNIQUE KEY(`start_dt`, `end_dt`, `order_id`)
PARTITION BY RANGE(`end_dt`)
(
    PARTITION p_999912 VALUES [('9999-12-31 00:00:00'), ('9999-12-31 23:59:59'))
)
DISTRIBUTED BY HASH(`order_id`) BUCKETS AUTO
PROPERTIES (
        "replication_allocation" = "tag.location.default: 1",
--         "replication_allocation" = "tag.location.default: 2",
        "estimate_partition_size" = "1G"
);

-- SELECT * FROM demo.orders;
-- SELECT * FROM orders_zip ORDER BY order_id, start_dt;
-- SELECT * FROM `demo`.`table_row_change_log` cl WHERE cl.dt = '2024-05-31' AND cl.table_name = 'orders' AND cl.op IN ('c', 'u', 'd')
-- SELECT * FROM `demo`.`table_row_change_log` cl WHERE cl.dt = '2024-06-01' AND cl.table_name = 'orders' AND cl.op IN ('c', 'u', 'd')
-- SELECT * FROM `demo`.`table_row_change_log` cl WHERE cl.dt = '2024-06-02' AND cl.table_name = 'orders' AND cl.op IN ('c', 'u', 'd')


-- ---------------------------- 初始化拉链表  只执行一次 ----------------------------
-- ---------------------------- 初始化  2024-06-01 begin----------------------------

insert into `orders_zip`(`start_dt`, `end_dt`, `order_id`, `price`)
select `last_update_at` as start_dt, '9999-12-31 00:00:00' as end_dt, `id` AS `order_id`, `price` from `demo`.`orders`;

-- ---------------------------- 初始化  2024-06-01 end ----------------------------


-- ---------------------------- 增量拉链表 每天调度一次 ----------------------------
-- 新增数据可能的情况
-- c 
-- c u
-- c u d
-- 存量数据可能的情况
-- u 
-- u d


-- ---------------------------- 调度（手动）  2024-06-01 begin----------------------------
-- 创建昨日数据分区
ALTER TABLE orders_zip ADD PARTITION IF NOT EXISTS p_20240531 VALUES [("2024-05-31 00:00:00"), ("2024-06-01 00:00:00"));

-- 将昨日删除/更新的数据 移入 昨日数据分区
INSERT INTO orders_zip(`start_dt`, `end_dt`, `order_id`, `price`) 
SELECT start_dt, '2024-05-31 23:59:59' AS end_dt, `order_id`, `price`
FROM orders_zip oz
WHERE oz.end_dt = '9999-12-31 00:00:00' AND EXISTS (
SELECT 1 FROM `demo`.`table_row_change_log` cl WHERE cl.dt = '2024-05-31' AND cl.table_name = 'orders' AND cl.pk_1 = oz.order_id AND cl.op IN ('c', 'u', 'd')
);

-- 将昨日删除/更新的数据删掉
DELETE FROM orders_zip oz USING `demo`.`table_row_change_log` cl
WHERE oz.end_dt = '9999-12-31 00:00:00' AND cl.dt = '2024-05-31' AND cl.table_name = 'orders' AND cl.pk_1 = oz.order_id AND cl.op IN ('c', 'u', 'd')

-- 将昨日新增/修改的数据 移入最新分区
INSERT INTO `orders_zip`(`start_dt`, `end_dt`, `order_id`, `price`)
SELECT 
IF(ISNULL(o.id),'2024-05-31 00:00:00',o.`last_update_at`) AS `start_dt`,
IF(ISNULL(o.id),'2024-05-31 23:59:59','9999-12-31 00:00:00') AS `end_dt`, 
cl.`id` as order_id, o.`price`
FROM
(SELECT DISTINCT `pk_1` AS id FROM `demo`.`table_row_change_log` where `demo`.`table_row_change_log`.`dt`='2024-05-31' AND `demo`.`table_row_change_log`.`table_name` = 'orders' AND `demo`.`table_row_change_log`.`op` IN ('c','u','d')) AS cl
LEFT JOIN 
`demo`.`orders` as o
on cl.`id` = o.`id`;

-- 删除历史分区（15天以前）
ALTER TABLE orders_zip DROP PARTITION IF EXISTS p_20240516;

-- ---------------------------- 调度  2024-06-01 end----------------------------

-- ---------------------------- 调度（自动）  2024-06-02 begin----------------------------
-- 创建昨日数据分区
ALTER TABLE orders_zip ADD PARTITION IF NOT EXISTS p_20240601 VALUES [("2024-06-01 00:00:00"), ("2024-06-02 00:00:00"));

-- 将昨日删除/更新的数据 移入 昨日数据分区
INSERT INTO orders_zip(`start_dt`, `end_dt`, `order_id`, `price`) 
SELECT start_dt, '2024-06-01 23:59:59' AS end_dt, `order_id`, `price`
FROM orders_zip oz
WHERE oz.end_dt = '9999-12-31 00:00:00' AND EXISTS (
SELECT 1 FROM `demo`.`table_row_change_log` cl WHERE cl.dt = '2024-06-01' AND cl.table_name = 'orders' AND cl.pk_1 = oz.order_id AND cl.op IN ('c', 'u', 'd')
);

-- 将昨日删除/更新的数据删掉
DELETE FROM orders_zip oz USING `demo`.`table_row_change_log` cl
WHERE oz.end_dt = '9999-12-31 00:00:00' AND cl.dt = '2024-06-01' AND cl.table_name = 'orders' AND cl.pk_1 = oz.order_id AND cl.op IN ('c', 'u', 'd')

-- 将昨日新增/修改的数据 移入最新分区
INSERT INTO `orders_zip`(`start_dt`, `end_dt`, `order_id`, `price`)
SELECT 
IF(ISNULL(o.id),'2024-06-01 00:00:00',o.`last_update_at`) AS `start_dt`,
IF(ISNULL(o.id),'2024-06-01 23:59:59','9999-12-31 00:00:00') AS `end_dt`, 
cl.`id` as order_id, o.`price`
FROM
(SELECT DISTINCT `pk_1` AS id FROM `demo`.`table_row_change_log` where `demo`.`table_row_change_log`.`dt`='2024-06-01' AND `demo`.`table_row_change_log`.`table_name` = 'orders' AND `demo`.`table_row_change_log`.`op` IN ('c','u','d')) AS cl
LEFT JOIN 
`demo`.`orders` as o
on cl.`id` = o.`id`;

-- 删除历史分区（15天以前）
ALTER TABLE orders_zip DROP PARTITION IF EXISTS p_20240517;

-- ---------------------------- 调度  2024-06-02 end----------------------------

-- ---------------------------- 调度（自动）  2024-06-03 begin ----------------------------
-- 创建昨日数据分区
ALTER TABLE orders_zip ADD PARTITION IF NOT EXISTS p_20240602 VALUES [("2024-06-02 00:00:00"), ("2024-06-03 00:00:00"));

-- 将昨日删除/更新的数据 移入 昨日数据分区
INSERT INTO orders_zip(`start_dt`, `end_dt`, `order_id`, `price`) 
SELECT start_dt, '2024-06-02 23:59:59' AS end_dt, `order_id`, `price`
FROM orders_zip oz
WHERE oz.end_dt = '9999-12-31 00:00:00' AND EXISTS (
SELECT 1 FROM `demo`.`table_row_change_log` cl WHERE cl.dt = '2024-06-02' AND cl.table_name = 'orders' AND cl.pk_1 = oz.order_id AND cl.op IN ('c', 'u', 'd')
);

-- 将昨日删除/更新的数据删掉
DELETE FROM orders_zip oz USING `demo`.`table_row_change_log` cl
WHERE oz.end_dt = '9999-12-31 00:00:00' AND cl.dt = '2024-06-02' AND cl.table_name = 'orders' AND cl.pk_1 = oz.order_id AND cl.op IN ('c', 'u', 'd')

-- 将昨日新增/修改的数据 移入最新分区
INSERT INTO `orders_zip`(`start_dt`, `end_dt`, `order_id`, `price`)
SELECT 
IF(ISNULL(o.id),'2024-06-02 00:00:00',o.`last_update_at`) AS `start_dt`,
IF(ISNULL(o.id),'2024-06-02 23:59:59','9999-12-31 00:00:00') AS `end_dt`, 
cl.`id` as order_id, o.`price`
FROM
(SELECT DISTINCT `pk_1` AS id FROM `demo`.`table_row_change_log` where `demo`.`table_row_change_log`.`dt`='2024-06-02' AND `demo`.`table_row_change_log`.`table_name` = 'orders' AND `demo`.`table_row_change_log`.`op` IN ('c','u','d')) AS cl
LEFT JOIN 
`demo`.`orders` as o
on cl.`id` = o.`id`;

-- 删除历史分区（15天以前）
ALTER TABLE orders_zip DROP PARTITION IF EXISTS p_20240518;

-- ---------------------------- 调度  2024-06-03 end ----------------------------


-- 调度会产生时间区域重叠的数据



