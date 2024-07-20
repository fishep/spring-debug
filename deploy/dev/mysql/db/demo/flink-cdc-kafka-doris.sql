SHOW VARIABLES LIKE '%zone%';
SET time_zone = 'system'
SET time_zone = 'Etc/UTC'
SET time_zone = 'Asia/Shanghai'
-- SET GLOBAL time_zone = 'Etc/UTC' 设置无效，见鬼
-- SET GLOBAL time_zone = 'Asia/Shanghai' 设置无效，见鬼
-- SELECT * FROM `performance_schema`.`persisted_variables`;
-- SET PERSIST_ONLY time_zone = 'Asia/Shanghai';
-- SET PERSIST time_zone = 'Asia/Shanghai';
-- RESET PERSIST IF EXISTS time_zone;



DROP TABLE IF EXISTS `flink_cdc_kafka_doris`;
CREATE TABLE `flink_cdc_kafka_doris`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `comment` varchar(255) NULL DEFAULT NULL COMMENT '备注',
	`some_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '有时',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
)COMMENT='table flink_cdc_kafka_doris';

SELECT * FROM `flink_cdc_kafka_doris`;

ALTER TABLE `flink_cdc_kafka_doris` ADD COLUMN `column_1` VARCHAR(255) DEFAULT '' COMMENT '新增column_1';
ALTER TABLE `flink_cdc_kafka_doris` DROP COLUMN `column_1`
