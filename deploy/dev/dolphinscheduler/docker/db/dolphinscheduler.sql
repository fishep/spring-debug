
-- 示例：ods_demo


-- 工作流名称：init-ods_demo

-- 节点名称：创建表(orders_zip)
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
-- 节点名称：创建表(orders_zip) end

-- 节点名称：初始化(orders_zip)
insert into `orders_zip`(`start_dt`, `end_dt`, `order_id`, `price`)
select CURRENT_TIMESTAMP() as start_dt, '9999-12-31 00:00:00' as end_dt, `id` AS `order_id`, `price` from `demo`.`orders`;
-- 节点名称：初始化(orders_zip) end

-- 工作流名称：init-ods_demo end


-- 工作流名称：sche-ods_demo

-- 节点名称：调度表(orders_zip)
-- 创建昨日数据分区
ALTER TABLE orders_zip ADD PARTITION IF NOT EXISTS p_$[yyyyMMdd-1] VALUES [("$[yyyy-MM-dd-1] 00:00:00"), ("$[yyyy-MM-dd] 00:00:00"));
-- 将昨日删除/更新的数据 移入 昨日数据分区
INSERT INTO orders_zip(`start_dt`, `end_dt`, `order_id`, `price`)
SELECT start_dt, '$[yyyy-MM-dd-1] 23:59:59' AS end_dt, `order_id`, `price`
FROM orders_zip oz
WHERE EXISTS (
SELECT 1 FROM `demo`.`table_row_change_log` cl WHERE cl.dt = '$[yyyy-MM-dd-1]' AND cl.table_name = 'orders' AND cl.pk_1 = oz.order_id AND cl.op IN ('d', 'u')
);
-- 将昨日删除/更新的数据删掉
DELETE FROM orders_zip oz USING `demo`.`table_row_change_log` cl
WHERE cl.dt = '$[yyyy-MM-dd-1]' AND cl.table_name = 'orders' AND cl.pk_1 = oz.order_id AND cl.op IN ('d', 'u') AND oz.end_dt = '9999-12-31 00:00:00';
-- 将昨日新增/修改的数据 移入最新分区
INSERT INTO `orders_zip`(`start_dt`, `end_dt`, `order_id`, `price`)
SELECT '$[yyyy-MM-dd-1] 00:00:00' as start_dt, '9999-12-31 00:00:00' as end_dt, `id` AS `order_id`, `price`
FROM `demo`.`orders` o
WHERE EXISTS (
SELECT 1 FROM `demo`.`table_row_change_log` cl WHERE cl.dt = '$[yyyy-MM-dd-1]' AND cl.table_name = 'orders' AND cl.pk_1 = o.id AND cl.op IN ('c', 'u')
);
-- 删除历史分区（15天以前）
ALTER TABLE orders_zip DROP PARTITION IF EXISTS p_$[yyyyMMdd-15];
-- 节点名称：调度表(orders_zip) end

-- 工作流名称：sche-ods_demo end