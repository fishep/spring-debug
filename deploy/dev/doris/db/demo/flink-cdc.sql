
DROP TABLE IF EXISTS `flink_cdc_1`;
CREATE TABLE `flink_cdc_1` (
  `id` bigint NOT NULL COMMENT 'id',
  `comment` varchar(255) NULL DEFAULT NULL COMMENT '备注',
	`last_update_at` DATETIMEV2 NULL DEFAULT NULL COMMENT '数据最后更新时间'
)UNIQUE KEY(`id`)
COMMENT "table flink_cdc_1"
DISTRIBUTED BY HASH(`id`) BUCKETS AUTO
PROPERTIES (
"replication_allocation" = "tag.location.default: 1"
);

ALTER TABLE `flink_cdc_1` ADD COLUMN `column_1` VARCHAR(255) DEFAULT '' COMMENT '新增column_1';
ALTER TABLE `flink_cdc_1` ADD COLUMN `column_2` VARCHAR(255) DEFAULT '' COMMENT '新增column_2';
ALTER TABLE `flink_cdc_1` DROP COLUMN `column_2`;
ALTER TABLE `flink_cdc_1` DROP COLUMN `column_1`;

SHOW CREATE TABLE `flink_cdc_1`;
CREATE TABLE `flink_cdc_1` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `comment` varchar(255) NULL COMMENT '备注',
  `column_1` varchar(765) NULL,
  `column_2` varchar(765) NULL COMMENT '?????olumn_2'
) ENGINE=OLAP
UNIQUE KEY(`id`)
COMMENT 'table flink_cdc_1'
DISTRIBUTED BY HASH(`id`) BUCKETS AUTO
PROPERTIES (
"replication_allocation" = "tag.location.default: 1",
"is_being_synced" = "false",
"storage_format" = "V2",
"light_schema_change" = "true",
"disable_auto_compaction" = "false",
"enable_single_replica_compaction" = "false"
);

DROP TABLE IF EXISTS `flink_cdc_2`;
CREATE TABLE `flink_cdc_2` (
  `id` bigint NOT NULL COMMENT 'id',
  `comment` varchar(255) NULL DEFAULT NULL COMMENT '备注',
  `column_1` varchar(255) NULL DEFAULT '' COMMENT '新增column_1',
  `column_2` varchar(255) NULL DEFAULT '' COMMENT '新增column_2',
	`last_update_at` DATETIMEV2 NULL DEFAULT NULL COMMENT '数据最后更新时间'
)UNIQUE KEY(`id`)
COMMENT "table flink_cdc_2"
DISTRIBUTED BY HASH(`id`) BUCKETS AUTO
PROPERTIES (
"replication_allocation" = "tag.location.default: 1"
);

DROP TABLE IF EXISTS `datagen_sink`;
CREATE TABLE datagen_sink (
    id INT,
    ts BIGINT,
    vc INT
)UNIQUE KEY(`id`)
COMMENT "table datagen_sink"
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



