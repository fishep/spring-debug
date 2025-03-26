# fluss
> fluss

### fluss
```shell

docker compose -p service up -d

#docker compose exec jobmanager ./sql-client
docker exec -it jobmanager bash


```

### mysql
```mysql

DROP TABLE IF EXISTS `main_table`;
CREATE TABLE `main_table`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `comment` varchar(255) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
)COMMENT='主表';


DROP TABLE IF EXISTS `detail_table`;
CREATE TABLE `detail_table`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细表id',
  `main_id` bigint NOT NULL COMMENT '主表id',
  `comment` varchar(255) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
)COMMENT='明细表';


INSERT INTO `main_table` (`id`, `comment`) VALUES (1, 'main_table comment 1');
INSERT INTO `main_table` (`id`, `comment`) VALUES (2, 'main_table comment 2');
-- INSERT INTO `main_table` (`id`, `comment`) VALUES (3, 'main_table comment 3');

INSERT INTO `detail_table` (`id`, `main_id`, `comment`) VALUES (1, 1, 'detail_table comment 1');
INSERT INTO `detail_table` (`id`, `main_id`, `comment`) VALUES (2, 2, 'detail_table comment 2');
INSERT INTO `detail_table` (`id`, `main_id`, `comment`) VALUES (3, 2, 'detail_table comment 3');

-- SELECT * FROM `main_table` m LEFT JOIN `detail_table` d ON (m.id = d.main_id);

DROP TABLE IF EXISTS `target_table`;
CREATE TABLE `target_table`  (
  `main_id` bigint NOT NULL COMMENT '主表id',
  `detail_id` bigint NOT NULL COMMENT '明细表id',
  `main_comment` varchar(255) NULL DEFAULT NULL COMMENT '主表备注',
  `detail_comment` varchar(255) NULL DEFAULT NULL COMMENT '明细表备注',
  PRIMARY KEY (`main_id`, `detail_id`)
)COMMENT='目标表';

-- SELECT * FROM `target_table`;

-- INSERT INTO `target_table`
-- SELECT m.id AS `main_id`, d.id AS `detail_id`, m.`comment` AS `main_comment`, d.`comment` AS `detail_comment`
-- FROM `main_table` m LEFT JOIN `detail_table` d ON (m.id = d.main_id);

```

### sql-client
```sql

SET 'sql-client.execution.result-mode'='tableau';

DROP TABLE IF EXISTS `cdc_main_table`;
CREATE TABLE `cdc_main_table` (
    `id` BIGINT,
    `comment` STRING,
    `last_update_at` AS PROCTIME(),
    PRIMARY KEY (`id`) NOT ENFORCED
) WITH (
    'connector' = 'mysql-cdc',
    'hostname' = 'mysql.dev',
    'port' = '3306',
    'username' = 'demo',
    'password' = 'demo',
    'database-name' = 'demo',
    'table-name' = 'main_table'
);

-- SELECT * FROM `cdc_main_table`;


DROP TABLE IF EXISTS `cdc_detail_table`;
CREATE TABLE `cdc_detail_table` (
      `id` BIGINT,
      `main_id` BIGINT,
      `comment` STRING,
      `last_update_at` AS PROCTIME(),
      PRIMARY KEY (`id`) NOT ENFORCED
) WITH (
      'connector' = 'mysql-cdc',
      'hostname' = 'mysql.dev',
      'port' = '3306',
      'username' = 'demo',
      'password' = 'demo',
      'database-name' = 'demo',
      'table-name' = 'detail_table'
);

-- SELECT * FROM `cdc_detail_table`;


CREATE CATALOG fluss_catalog WITH (
    'type' = 'fluss',
    'bootstrap.servers' = 'coordinator-server:9123'
);

USE CATALOG fluss_catalog;

DROP TABLE IF EXISTS `f_main_table`;
CREATE TABLE `f_main_table` (
      `id` BIGINT,
      `comment` STRING,
      `last_update_at` TIMESTAMP,
      PRIMARY KEY (`id`) NOT ENFORCED
);

DROP TABLE IF EXISTS `f_detail_table`;
CREATE TABLE `f_detail_table` (
    `id` BIGINT,
    `main_id` BIGINT,
    `comment` STRING,
    `last_update_at` TIMESTAMP,
    PRIMARY KEY (`id`) NOT ENFORCED
);

-- 将MySQL数据同步到fluss
EXECUTE STATEMENT SET
BEGIN
INSERT INTO f_main_table SELECT * FROM `default_catalog`.`default_database`.cdc_main_table;
INSERT INTO f_detail_table SELECT * FROM `default_catalog`.`default_database`.cdc_detail_table;
END;

-- SELECT * FROM `f_main_table`;
-- SELECT * FROM `f_detail_table`;
-- SELECT * FROM `f_main_table` AS m LEFT JOIN `f_detail_table` AS d ON (m.id = d.main_id);
-- SELECT m.id AS `main_id`, d.id AS `detail_id`, m.`comment` AS `main_comment`, d.`comment` AS `detail_comment` FROM `f_main_table` AS m LEFT JOIN `f_detail_table` AS d ON (m.id = d.main_id);

-- 创建fluss中间表
DROP TABLE IF EXISTS `f_target_table`;
CREATE TABLE `f_target_table` (
      `main_id` BIGINT,
      `detail_id` BIGINT,
      `main_comment` STRING,
      `detail_comment` STRING,
      PRIMARY KEY (`main_id`, `detail_id`) NOT ENFORCED
);

INSERT INTO f_target_table SELECT m.id AS `main_id`, d.id AS `detail_id`, m.`comment` AS `main_comment`, d.`comment` AS `detail_comment` FROM `f_main_table` AS m LEFT JOIN `f_detail_table` AS d ON (m.id = d.main_id);

-- SELECT * FROM `f_target_table`;


-- USE CATALOG default_catalog;
DROP TABLE IF EXISTS `default_catalog`.`default_database`.`cdc_target_table`;
CREATE TABLE `default_catalog`.`default_database`.`cdc_target_table` (
    `main_id` BIGINT,
    `detail_id` BIGINT,
    `main_comment` STRING,
    `detail_comment` STRING,
    PRIMARY KEY (`main_id`, `detail_id`) NOT ENFORCED
) WITH (
    'connector' = 'jdbc',
    'url' = 'jdbc:mysql://mysql.dev:3306/demo',
    'username' = 'demo',
    'password' = 'demo',
    'table-name' = 'target_table'
);

INSERT INTO `default_catalog`.`default_database`.`cdc_target_table` SELECT * FROM f_target_table;


```