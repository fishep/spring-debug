
DROP TABLE IF EXISTS `dynamic_partition`;
CREATE TABLE dynamic_partition
(
    `dt` DATEV2 NOT NULL COMMENT '日期',
    `comment` VARCHAR(255) NULL DEFAULT NULL COMMENT '备注'
)
PARTITION BY RANGE(dt) ()
DISTRIBUTED BY HASH(dt)
PROPERTIES
(
    "dynamic_partition.enable" = "true",
    "dynamic_partition.time_unit" = "DAY",
    "dynamic_partition.start" = "-1",
    "dynamic_partition.end" = "1",
    "dynamic_partition.replication_num" = "1",
    "dynamic_partition.time_zone" = "Asia/Shanghai",
    "dynamic_partition.prefix" = "p_",
--     "dynamic_partition.buckets" = "10"
    "dynamic_partition.create_history_partition" = "true"
);

SHOW DYNAMIC PARTITION TABLES;
SHOW PARTITIONS FROM dynamic_partition;

INSERT INTO `dynamic_partition`(`dt`, `comment`) VALUES ('2024-07-08', 'this is a test');
INSERT INTO `dynamic_partition`(`dt`, `comment`) VALUES ('2024-07-09', 'this is a test');
INSERT INTO `dynamic_partition`(`dt`, `comment`) VALUES ('2024-07-10', 'this is a test');
INSERT INTO `dynamic_partition`(`dt`, `comment`) VALUES ('2024-07-11', 'this is a test');
INSERT INTO `dynamic_partition`(`dt`, `comment`) VALUES ('2024-07-12', 'this is a test');


