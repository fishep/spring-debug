DROP TABLE IF EXISTS `stream`;
CREATE TABLE `stream`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `comment` varchar(255) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
)COMMENT='datax test, mysql to stream, stream to mysql';

INSERT INTO `demo`.`stream` (`id`, `comment`) VALUES (1, 'this is a test 1');
INSERT INTO `demo`.`stream` (`id`, `comment`) VALUES (2, 'this is a test 2');
INSERT INTO `demo`.`stream` (`id`, `comment`) VALUES (3, 'this is a test 3');


DROP TABLE IF EXISTS `tmp_stream`;
CREATE TABLE `tmp_stream`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `comment` varchar(255) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
)COMMENT='tmp table for stream';

INSERT INTO `demo`.`tmp_stream` (`id`, `comment`) VALUES (4, 'this is a test 4');
INSERT INTO `demo`.`tmp_stream` (`id`, `comment`) VALUES (5, 'this is a test 5');
INSERT INTO `demo`.`tmp_stream` (`id`, `comment`) VALUES (6, 'this is a test 6');




-- 使用datax全量同步数据策略

-- step 1 清空零时表
TRUNCATE TABLE `tmp_stream`;

-- step 2 同步数据零时表，datax 全量同步

-- step 3 用零时表替换原表
RENAME TABLE `tmp_stream` TO `new_stream`;
RENAME TABLE `stream` TO `tmp_stream`;
RENAME TABLE `new_stream` TO `stream`;

