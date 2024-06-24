-- DROP DATABASE `ods_demo`;
CREATE
DATABASE IF NOT EXISTS `ods_demo`;

USE
`ods_demo`;

-- 创建拉链表
DROP TABLE IF EXISTS `orders_zip`;
CREATE TABLE `orders_zip`
(
    `start_dt` DATETIMEV2,
    `end_dt`   DATETIMEV2,
    `order_id` INT,
    `price`    DECIMAL(10, 2)
)ENGINE=OLAP
UNIQUE KEY(`start_dt`, `end_dt`, `order_id`)
PARTITION BY RANGE(`end_dt`)
(
    PARTITION p_999912 VALUES [(''9999-12-31 00:00:00''), (''9999-12-31 23:59:59''))
)
DISTRIBUTED BY HASH(`order_id`) BUCKETS AUTO
PROPERTIES (
        "replication_allocation" = "tag.location.default: 1",
--         "replication_allocation" = "tag.location.default: 2",
        "estimate_partition_size" = "1G"
);

SELECT *
FROM demo.orders;
SELECT *
FROM orders_zip
ORDER BY order_id, start_dt;
SELECT *
FROM `demo`.`table_row_change_log` cl
WHERE cl.dt = ''2024 - 05 - 31'' AND cl.table_name = ''orders'' AND cl.op IN (''c'', ''u'', ''d'')
SELECT *
FROM `demo`.`table_row_change_log` cl
WHERE cl.dt = ''2024 - 06 - 01'' AND cl.table_name = ''orders'' AND cl.op IN (''c'', ''u'', ''d'')
SELECT *
FROM `demo`.`table_row_change_log` cl
WHERE cl.dt = ''2024 - 06 - 02'' AND cl.table_name = ''orders'' AND cl.op IN (''c'', ''u'', ''d'')


-- ---------------------------- 初始化拉链表  只执行一次 ----------------------------
-- ---------------------------- 初始化  2024-06-01 begin----------------------------

insert
into `orders_zip`(`start_dt`, `end_dt`, `order_id`, `price`)
select ''2024 - 06 - 01 00:00:00'' as start_dt, ''9999 - 12-31 00:00:00'' as end_dt, `id` AS `order_id`, `price`
from `demo`.`orders`;

-- ---------------------------- 初始化  2024-06-01 end ----------------------------


-- ---------------------------- 增量拉链表 每天调度一次 ----------------------------
-- 新增数据可能的情况
-- c
-- c u
-- c u d
-- 存量数据可能的情况
-- u
-- u d


-- -- ---------------------------- 调度  2024-06-01 begin----------------------------
-- -- 创建昨日数据分区
-- ALTER TABLE orders_zip ADD PARTITION IF NOT EXISTS p_20240531 VALUES [("2024-05-31 00:00:00"), ("2024-06-01 00:00:00"));
--
-- -- 将昨日删除/更新的数据 移入 昨日数据分区
-- INSERT INTO orders_zip(`start_dt`, `end_dt`, `order_id`, `price`)
-- SELECT start_dt, ''2024-05-31 23:59:59'' AS end_dt, `order_id`, `price`
-- FROM orders_zip oz
-- WHERE EXISTS (
-- SELECT 1 FROM `demo`.`table_row_change_log` cl WHERE cl.dt = ''2024-05-31'' AND cl.table_name = ''orders'' AND cl.pk_1 = oz.order_id AND cl.op IN (''d'', ''u'')
-- );
--
-- -- 将昨日删除/更新的数据删掉
-- DELETE FROM orders_zip oz USING `demo`.`table_row_change_log` cl
-- WHERE cl.dt = ''2024-05-31'' AND cl.table_name = ''orders'' AND cl.pk_1 = oz.order_id AND cl.op IN (''d'', ''u'') AND oz.end_dt = ''9999-12-31 00:00:00''
--
-- -- 将昨日新增/修改的数据 移入最新分区
-- INSERT INTO `orders_zip`(`start_dt`, `end_dt`, `order_id`, `price`)
-- SELECT ''2024-05-31 00:00:00'' as start_dt, ''9999-12-31 00:00:00'' as end_dt, `id` AS `order_id`, `price`
-- FROM `demo`.`orders` o
-- WHERE EXISTS (
-- SELECT 1 FROM `demo`.`table_row_change_log` cl WHERE cl.dt = ''2024-05-31'' AND cl.table_name = ''orders'' AND cl.pk_1 = o.id AND cl.op IN (''c'', ''u'')
-- );
--
-- -- 删除历史分区（15天以前）
-- ALTER TABLE orders_zip DROP PARTITION IF EXISTS p_20240516;
--
-- -- ---------------------------- 调度  2024-06-01 end----------------------------
--
-- ---------------------------- 调度  2024-06-02 begin----------------------------
-- 创建昨日数据分区
ALTER TABLE orders_zip
    ADD PARTITION IF NOT EXISTS p_20240601 VALUES [("2024-06-01 00:00:00"), ("2024-06-02 00:00:00"));

-- 将昨日删除/更新的数据 移入 昨日数据分区
INSERT INTO orders_zip(`start_dt`, `end_dt`, `order_id`, `price`)
SELECT start_dt, ''2024 - 06 - 01 23:59:59'' AS end_dt, `order_id`, `price`
FROM orders_zip oz
WHERE EXISTS(
              SELECT 1 FROM `demo`.`table_row_change_log` cl WHERE cl.dt = ''2024 - 06 - 01'' AND cl.table_name = ''
              orders '' AND cl.pk_1 = oz.order_id AND cl.op IN (''d'', ''u'')
          );

-- 将昨日删除/更新的数据删掉
DELETE
FROM orders_zip oz USING `demo`.`table_row_change_log` cl
WHERE cl.dt = ''2024-06-01'' AND cl.table_name = ''orders'' AND cl.pk_1 = oz.order_id AND cl.op IN (''d'', ''u'') AND oz.end_dt = ''9999-12-31 00:00:00''

-- 将昨日新增/修改的数据 移入最新分区
INSERT
INTO `orders_zip`(`start_dt`, `end_dt`, `order_id`, `price`)
SELECT ''2024 - 06 - 01 00:00:00'' as start_dt, ''9999 - 12-31 00:00:00'' as end_dt, `id` AS `order_id`, `price`
FROM `demo`.`orders` o
WHERE EXISTS(
              SELECT 1 FROM `demo`.`table_row_change_log` cl WHERE cl.dt = ''2024 - 06 - 01'' AND cl.table_name = ''
              orders '' AND cl.pk_1 = o.id AND cl.op IN (''c'', ''u'')
          );

-- 删除历史分区（15天以前）
ALTER TABLE orders_zip DROP PARTITION IF EXISTS p_20240517;

-- ---------------------------- 调度  2024-06-02 end----------------------------

-- ---------------------------- 调度  2024-06-03 begin ----------------------------
-- 创建昨日数据分区
ALTER TABLE orders_zip
    ADD PARTITION IF NOT EXISTS p_20240602 VALUES [("2024-06-02 00:00:00"), ("2024-06-03 00:00:00"));

-- 将昨日删除/更新的数据 移入 昨日数据分区
INSERT INTO orders_zip(`start_dt`, `end_dt`, `order_id`, `price`)
SELECT start_dt, ''2024 - 06 - 02 23:59:59'' AS end_dt, `order_id`, `price`
FROM orders_zip oz
WHERE EXISTS(
              SELECT 1 FROM `demo`.`table_row_change_log` cl WHERE cl.dt = ''2024 - 06 - 02'' AND cl.table_name = ''
              orders '' AND cl.pk_1 = oz.order_id AND cl.op IN (''d'', ''u'')
          );

-- 将昨日删除/更新的数据删掉
DELETE
FROM orders_zip oz USING `demo`.`table_row_change_log` cl
WHERE cl.dt = ''2024-06-02'' AND cl.table_name = ''orders'' AND cl.pk_1 = oz.order_id AND cl.op IN (''d'', ''u'') AND oz.end_dt = ''9999-12-31 00:00:00''

-- 将昨日新增/修改的数据 移入最新分区
INSERT
INTO `orders_zip`(`start_dt`, `end_dt`, `order_id`, `price`)
SELECT ''2024 - 06 - 02 00:00:00'' as start_dt, ''9999 - 12-31 00:00:00'' as end_dt, `id` AS `order_id`, `price`
FROM `demo`.`orders` o
WHERE EXISTS(
              SELECT 1 FROM `demo`.`table_row_change_log` cl WHERE cl.dt = ''2024 - 06 - 02'' AND cl.table_name = ''
              orders '' AND cl.pk_1 = o.id AND cl.op IN (''c'', ''u'')
          );

-- 删除历史分区（15天以前）
ALTER TABLE orders_zip DROP PARTITION IF EXISTS p_20240518;

-- ---------------------------- 调度  2024-06-03 end ----------------------------

-- 缺陷

-- 第一个调度会产生时间区域重叠的数据，如
-- 2024-05-31 -- c        1
-- 2024-06-01 初始化
-- 2024-06-01 -- u        1
-- 2024-06-02 调度
start_dt
end_dt	order_id	price
2024-06-01 00:00:00	2024-06-01 23:59:59	1	10.00
2024-06-01 00:00:00	9999-12-31 00:00:00	1	11.00

-- 新增数据的情况，一个调度周期内新增之后，又删除的数据，无法体现 如
-- c u d    3
-- c u d    6
-- c u d    9

-- 存量数据的情况，更新之后又删除的无法体现，最后修改的数据
-- 2024-05-31 -- c u      2 -- 20.00 21.00
-- 2024-06-01 -- u d      2 -- 22.00 NULL
start_dt	end_dt	order_id	price
2024-06-01 00:00:00	2024-06-01 23:59:59	2	21.00
-- 存量数据的情况，删除的数据无法体现，跟上一个类似
start_dt	end_dt	order_id	price
2024-06-01 00:00:00	2024-06-02 23:59:59	5	51.00

-- 会产生重叠时间区域
-- 2024-06-01 -- c        4
-- 2024-06-02 调度   产生 2024-06-01 00:00:00 - 9999-12-31 00:00:00
-- 2024-06-02 -- u        4
-- 2024-06-03 调度
start_dt	end_dt	order_id	price
2024-06-01 00:00:00	2024-06-02 23:59:59	4	40.00
2024-06-02 00:00:00	9999-12-31 00:00:00	4	41.00

-- 会产生数据覆盖，存在多条历史记录的时候
-- orders_zip 查询插入没有限定 oz.end_dt = ''9999-12-31 00:00:00''
-- orders_zip 删除     限定了 oz.end_dt = ''9999-12-31 00:00:00''

