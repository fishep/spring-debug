
DROP TABLE IF EXISTS `flink_cdc_1`;
CREATE TABLE `flink_cdc_1`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `comment` varchar(255) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
)COMMENT='table flink_cdc_1';

ALTER TABLE `flink_cdc_1` ADD COLUMN `column_1` VARCHAR(255) DEFAULT '' COMMENT '新增column_1';
ALTER TABLE `flink_cdc_1` ADD COLUMN `column_2` VARCHAR(255) DEFAULT '' COMMENT '新增column_2';
ALTER TABLE `flink_cdc_1` ADD COLUMN `last_update_at` DATETIME NULL DEFAULT NULL COMMENT '数据最后更新时间';
ALTER TABLE `flink_cdc_1` DROP COLUMN `column_2`;
ALTER TABLE `flink_cdc_1` DROP COLUMN `column_1`;
ALTER TABLE `flink_cdc_1` DROP COLUMN `last_update_at`;

DROP TABLE IF EXISTS `flink_cdc_2`;
CREATE TABLE `flink_cdc_2`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `comment` varchar(255) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
)COMMENT='table flink_cdc_2';

ALTER TABLE `flink_cdc_2` ADD COLUMN `flink_cdc_1_id` bigint NULL DEFAULT NULL COMMENT '对应 flink_cdc_1.id' AFTER `id`;

ALTER TABLE `flink_cdc_2` ADD COLUMN `column_1` VARCHAR(255) DEFAULT '' COMMENT '新增column_1';
ALTER TABLE `flink_cdc_2` ADD COLUMN `column_2` VARCHAR(255) DEFAULT '' COMMENT '新增column_2';
ALTER TABLE `flink_cdc_2` DROP COLUMN `column_2`;


INSERT INTO `flink_cdc_1` (`id`, `comment`, `column_1`) VALUES (1, '1', '1');
INSERT INTO `flink_cdc_1` (`id`, `comment`, `column_1`) VALUES (2, '2', '2');
INSERT INTO `flink_cdc_1` (`id`, `comment`, `column_1`) VALUES (3, '3', '3');
DELETE FROM `flink_cdc_1` WHERE `id` = 2;

INSERT INTO `flink_cdc_2` (`id`, `comment`, `column_2`) VALUES (1, '11', '11');
INSERT INTO `flink_cdc_2` (`id`, `comment`, `column_2`) VALUES (2, '22', '22');
INSERT INTO `flink_cdc_2` (`id`, `comment`, `column_2`) VALUES (3, '33', '33');
DELETE FROM `flink_cdc_2` WHERE `id` = 3;


SHOW VARIABLES LIKE '%character%';

SELECT f1.* FROM flink_cdc_1 f1 LEFT JOIN flink_cdc_2 f2 ON (f1.id = f2.id)
GROUP BY f1.id




