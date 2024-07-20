SHOW VARIABLES LIKE '%zone%';
SET time_zone = 'Etc/UTC'
SET time_zone = 'Asia/Shanghai'
SET GLOBAL time_zone = 'Etc/UTC'
SET GLOBAL time_zone = 'Asia/Shanghai'




DROP TABLE IF EXISTS `flink_cdc_kafka_doris`;
CREATE TABLE `flink_cdc_kafka_doris` (
  `id` bigint NOT NULL COMMENT 'id',
  `comment` varchar(255) NULL DEFAULT NULL COMMENT '备注',
	`some_time` DATETIMEV2 NULL DEFAULT NULL COMMENT '有时',
	`created_at` DATETIMEV2 NULL DEFAULT NULL COMMENT '创建时间',
	`updated_at` DATETIMEV2 NULL DEFAULT NULL COMMENT '更新时间',
	`last_update_at` DATETIMEV2 NULL DEFAULT NULL COMMENT '数据最后更新时间'
)UNIQUE KEY(`id`)
COMMENT "table flink_cdc_kafka_doris"
DISTRIBUTED BY HASH(`id`) BUCKETS AUTO
PROPERTIES (
"replication_allocation" = "tag.location.default: 1"
);

SELECT * FROM `flink_cdc_kafka_doris`;