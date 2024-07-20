### flink sql
```shell
docker cp mysql-connector-j-8.0.32.jar jobmanager:/opt/flink/lib
docker cp mysql-connector-j-8.0.32.jar taskmanager:/opt/flink/lib
docker cp flink-connector-jdbc-3.2.0-1.19.jar jobmanager:/opt/flink/lib
docker cp flink-connector-jdbc-3.2.0-1.19.jar taskmanager:/opt/flink/lib
docker cp flink-sql-connector-mysql-cdc-3.1.1.jar jobmanager:/opt/flink/lib
docker cp flink-sql-connector-mysql-cdc-3.1.1.jar taskmanager:/opt/flink/lib
docker cp flink-doris-connector-1.19-1.6.1.jar jobmanager:/opt/flink/lib
docker cp flink-doris-connector-1.19-1.6.1.jar taskmanager:/opt/flink/lib

docker exec -it jobmanager bash 
docker exec -it taskmanager bash 
chown flink:flink ./lib/mysql-connector-j-8.0.32.jar ./lib/flink-connector-jdbc-3.2.0-1.19.jar ./lib/flink-sql-connector-mysql-cdc-3.1.1.jar ./lib/flink-doris-connector-1.19-1.6.1.jar
chmod 644 ./lib/mysql-connector-j-8.0.32.jar ./lib/flink-connector-jdbc-3.2.0-1.19.jar ./lib/flink-sql-connector-mysql-cdc-3.1.1.jar ./lib/flink-doris-connector-1.19-1.6.1.jar

#重启 jobmanager taskmanager
docker compose -p service stop jobmanager taskmanager
docker compose -p service up -d

docker exec -it jobmanager bash
./bin/sql-client.sh

#默认table，还可以设置为tableau、changelog
#SET sql-client.execution.result-mode=tableau;
#SET execution.runtime-mode=streaming; #默认streaming，也可以设置batch
# PRIMARY KEY (`id`) NOT ENFORCED
# 如果定义了主键，则连接器以upsert模式操作，否则，连接器以追加模式操作。

```

### demo
```shell
DROP TABLE IF EXISTS `datagen_source`;
CREATE TABLE datagen_source (
    id INT,
    ts BIGINT,
    vc INT
)
WITH (
    'connector' = 'datagen',
    'rows-per-second'='1',
    'fields.id.kind'='random',
    'fields.id.min'='1',
    'fields.id.max'='10',
    'fields.ts.kind'='sequence',
    'fields.ts.start'='1',
    'fields.ts.end'='1000000',
    'fields.vc.kind'='random',
    'fields.vc.min'='1',
    'fields.vc.max'='100'
);

DROP TABLE IF EXISTS `datagen_sink`;
CREATE TABLE datagen_sink (
    id INT,
    ts BIGINT,
    vc INT
) WITH (
    'connector' = 'print'
);

INSERT INTO datagen_sink SELECT * FROM datagen_source;
#SELECT * FROM datagen_source;
#SELECT * FROM datagen_sink;
  
CREATE TABLE datagen_sum_sink (
    id INT,
    sumVC INT
) WITH (
    'connector' = 'print'
);

INSERT INTO datagen_sum_sink(id, sumVC)
SELECT id, SUM(vc) AS sumVC FROM datagen_source GROUP BY id;

#SELECT * FROM datagen_source;
#SELECT * FROM datagen_sum_sink;
```

### mysql to doris
```shell
DROP TABLE IF EXISTS `mysql_flink_cdc_1`;
CREATE TABLE `mysql_flink_cdc_1`  (
  `id` BIGINT,
  `comment` STRING
) WITH (
   'connector' = 'jdbc',
   'url' = 'jdbc:mysql://mysql.dev:3306/demo',
   'table-name' = 'flink_cdc_1',
   'username' = 'demo',
   'password' = 'demo'
);

DROP TABLE IF EXISTS `doris_flink_cdc_1`;
CREATE TABLE `doris_flink_cdc_1`  (
  `id` BIGINT,
  `comment` STRING
) WITH (
   'connector' = 'jdbc',
   'url' = 'jdbc:mysql://doris.dev:9030/demo',
   'table-name' = 'flink_cdc_1',
   'username' = 'root',
   'password' = ''
);

SELECT * FROM `mysql_flink_cdc_1`;
SELECT * FROM `doris_flink_cdc_1`;

INSERT INTO `mysql_flink_cdc_1` (`id`, `comment`) VALUES (1, 'this is a test');
INSERT INTO `doris_flink_cdc_1` (`id`, `comment`) VALUES (2, 'this is a test');

INSERT INTO `doris_flink_cdc_1` (`id`, `comment`)
SELECT `id`, `comment` FROM `mysql_flink_cdc_1`;



DROP TABLE IF EXISTS `datagen_sink`;
CREATE TABLE datagen_sink (
    id INT,
    ts BIGINT,
    vc INT
) WITH (
   'connector' = 'jdbc',
   'url' = 'jdbc:mysql://doris.dev:9030/demo',
   'table-name' = 'datagen_sink',
   'username' = 'root',
   'password' = ''
);

INSERT INTO datagen_sink SELECT * FROM datagen_source;

```

### mysql cdc to doris
```shell
#https://github.com/apache/flink-cdc/blob/master/docs/content/docs/connectors/flink-sources/mysql-cdc.md
#https://doris.apache.org/docs/2.0/ecosystem/flink-doris-connector

DROP TABLE IF EXISTS `mysql_flink_cdc_1`;
CREATE TABLE `mysql_flink_cdc_1`  (
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
    'table-name' = 'flink_cdc_1'
);

DROP TABLE IF EXISTS `mysql_flink_cdc_2`;
CREATE TABLE `mysql_flink_cdc_2`  (
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
    'table-name' = 'flink_cdc_2'
);

DROP TABLE IF EXISTS `doris_flink_cdc_1`;
CREATE TABLE `doris_flink_cdc_1`  (
  `id` BIGINT,
  `comment` STRING,
  `last_update_at` TIMESTAMP(3)
) WITH (
  'connector' = 'doris',
  'fenodes' = 'doris.dev:8030',
  'benodes' = 'doris.dev:8040',
  'table.identifier' = 'demo.flink_cdc_1',
  'username' = 'root',
  'password' = '',
  'sink.properties.format' = 'json',
  'sink.properties.read_json_by_line' = 'true',
  'sink.enable-delete' = 'true',
  'sink.label-prefix' = 'doris_label_1'
);
  
DROP TABLE IF EXISTS `doris_flink_cdc_2`;
CREATE TABLE `doris_flink_cdc_2`  (
  `id` BIGINT,
  `comment` STRING,
  `column_1` STRING,
  `column_2` STRING,
  `last_update_at` TIMESTAMP(3)
) WITH (
  'connector' = 'doris',
  'fenodes' = 'doris.dev:8030',
  'benodes' = 'doris.dev:8040',
  'table.identifier' = 'demo.flink_cdc_2',
  'username' = 'root',
  'password' = '',
  'sink.properties.format' = 'json',
  'sink.properties.read_json_by_line' = 'true',
  'sink.enable-delete' = 'true',
  'sink.label-prefix' = 'doris_label_2'
);

SELECT * FROM `mysql_flink_cdc_1`;
SELECT * FROM `mysql_flink_cdc_2`;
SELECT * FROM `doris_flink_cdc_1`;
SELECT * FROM `doris_flink_cdc_2`;

#INSERT INTO `mysql_flink_cdc_1` (`id`, `comment`) VALUES (1, 'this is a test');
#INSERT INTO `mysql_flink_cdc_2` (`id`, `comment`) VALUES (1, 'this is a test');
#INSERT INTO `doris_flink_cdc_1` (`id`, `comment`) VALUES (1, 'this is a test');
#INSERT INTO `doris_flink_cdc_2` (`id`, `comment`, `column_1`, `column_2`, `last_update_at`) VALUES (1, 'this is a test', 'this is column_1', 'this is column_2', PROCTIME());

INSERT INTO `doris_flink_cdc_1` (`id`, `comment`, `last_update_at`)
SELECT `id`, `comment`, `last_update_at` FROM `mysql_flink_cdc_1`;

INSERT INTO `doris_flink_cdc_2` (`id`, `comment`, `column_1`, `column_2`, `last_update_at`)
SELECT t1.`id`, 'this is a test' AS `comment`, t1.`comment` AS `column_1`, t2.`comment` AS `column_2`, IF(t2.`last_update_at` > t1.`last_update_at`,t2.`last_update_at`,t1.`last_update_at`) AS `last_update_at`
FROM mysql_flink_cdc_1 t1
LEFT JOIN mysql_flink_cdc_2 t2
ON t1.id = t2.id;

# 增量同步

#flink stop --savepointPath [:targetDirectory] :jobId
flink stop --savepointPath a9af5ceb96141f25eb424daca97d531b
#Savepoint completed. Path: file:/opt/flink/flink-savepoints/savepoint-a9af5c-943615839fc0
bash bin/flink-cdc.sh -s file:/opt/flink/flink-savepoints/savepoint-a9af5c-943615839fc0 job/mysql-to-doris.yaml

SET sql-client.execution.result-mode=tableau;
SET 'execution.savepoint.path' = '/opt/flink/flink-savepoints/savepoint-a9af5c-943615839fc0';

```