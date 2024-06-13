# 基于docker部署doris服务

### doris
```shell
#windows主机 找到文件 ~/.wslconfig 配置 kernelCommandLine = vsyscall=emulate

docker run -it --privileged --pid=host --name=change_count debian nsenter -t 1 -m -u -n -i sh
# docker 每次重启都要设置
#docker start -ai change_count
sysctl -q vm.max_map_count
sysctl -w vm.max_map_count=2000000

#cd cluster-1.2.2
cd cluster-2.0.0
docker compose -p service -f docker-compose-amd64.yaml up -d 
#docker compose -p service -f docker-compose-arm64.yaml up -d 

```

### doris test
```shell
docker cp ./test.csv doris-fe:/opt

docker exec -it doris-fe bash
docker exec -it doris-be bash

# 检查 Doris 是否启动成功
curl http://127.0.0.1:8030/api/bootstrap

# 
mysql -uroot -P9030 -h127.0.0.1
#查看 FE 运行状态
show frontends\G;
show backends\G;
#
create database demo;
use demo;
CREATE TABLE IF NOT EXISTS demo.example_tbl
(
    `user_id` LARGEINT NOT NULL COMMENT "用户id",
#    `date` DATE NOT NULL COMMENT "数据灌入日期时间",
    `date` DATEV2 NOT NULL COMMENT "数据灌入日期时间",
    `city` VARCHAR(20) COMMENT "用户所在城市",
    `age` SMALLINT COMMENT "用户年龄",
    `sex` TINYINT COMMENT "用户性别",
    `last_visit_date` DATETIME REPLACE DEFAULT "1970-01-01 00:00:00" COMMENT "用户最后一次访问时间",
    `cost` BIGINT SUM DEFAULT "0" COMMENT "用户总消费",
    `max_dwell_time` INT MAX DEFAULT "0" COMMENT "用户最大停留时间",
    `min_dwell_time` INT MIN DEFAULT "99999" COMMENT "用户最小停留时间"
)
AGGREGATE KEY(`user_id`, `date`, `city`, `age`, `sex`)
DISTRIBUTED BY HASH(`user_id`) BUCKETS 1
PROPERTIES (
"replication_allocation" = "tag.location.default: 1"
);

cd /opt
curl  --location-trusted -u root: -T test.csv -H "column_separator:," http://127.0.0.1:8030/api/demo/example_tbl/_stream_load

mysql -uroot -P9030 -h127.0.0.1
use demo;
select * from example_tbl;

# http://127.0.0.1:8030/login -- u/p: [root/ ]

```

### doris command
```mysql
insert into example_tbl(`user_id`,`date`,`city`,`age`,`sex`,`last_visit_date`,`cost`,`max_dwell_time`,`min_dwell_time`)
values (10000,"2017-10-01","北京",20,0,"2017-10-01 06:00:00",20,10,10);
insert into example_tbl(`user_id`,`date`,`city`,`age`,`sex`,`last_visit_date`,`cost`,`max_dwell_time`,`min_dwell_time`) 
values (10004,"2017-10-03","深圳",35,0,"2017-10-03 10:20:22",11,6,6);
SHOW LAST INSERT;
select * from example_tbl;

desc example_tbl all;
alter table example_tbl add rollup example_rollup(`city`, `age`, `sex`);
alter table example_tbl drop rollup example_rollup;

explain graph SELECT `city`, `age`, `sex`, `cost` FROM example_tbl;
explain SELECT `city`, `age`, `sex`, sum(cost) FROM example_tbl GROUP BY `city`, `age`, `sex`;
explain SELECT `city`, `age`, sum(cost) FROM example_tbl GROUP BY `city`, `age`;
explain SELECT `city`, sum(cost) FROM example_tbl GROUP BY `city`;
explain SELECT `age`, sum(cost) FROM example_tbl GROUP BY `age`;
    
SHOW ALTER TABLE ROLLUP;

create materialized view example_view as
SELECT `city`, `age`, `sex`, sum(cost) FROM example_tbl GROUP BY `city`, `age`, `sex`;
SHOW ALTER TABLE MATERIALIZED VIEW;
SHOW ALTER TABLE MATERIALIZED VIEW FROM demo;
desc example_tbl all;
EXPLAIN SELECT `city`, `age`, `sex`, sum(cost) FROM example_tbl GROUP BY `city`, `age`, `sex`;
EXPLAIN SELECT `city`, `age`, sum(cost) FROM example_tbl GROUP BY `city`, `age`;
DROP MATERIALIZED VIEW example_view on example_tbl;

SHOW VARIABLES LIKE "%mem_limit%";


EXPLAIN SELECT * FROM `example_tbl` JOIN demo WHERE `example_tbl`.`user_id` = `demo`.`user_id`;
EXPLAIN SELECT * FROM `example_tbl` JOIN [broadcast] demo WHERE `example_tbl`.`user_id` = `demo`.`user_id`;
EXPLAIN SELECT * FROM `example_tbl` JOIN [shuffle] demo WHERE `example_tbl`.`user_id` = `demo`.`user_id`;
# BUCKET_SHUFFLE
EXPLAIN SELECT SUM(`example_tbl`.`cost`) FROM `example_tbl` JOIN demo WHERE `example_tbl`.`user_id` = `demo`.`user_id`;

CREATE TABLE `tbl1` (
    `k1` datev2 NOT NULL COMMENT "",
    `k2` int(11) NOT NULL COMMENT "",
    `v1` int(11) SUM NOT NULL COMMENT ""
) ENGINE=OLAP
AGGREGATE KEY(`k1`, `k2`)
PARTITION BY RANGE(`k1`)
(
    PARTITION p1 VALUES LESS THAN ('2019-05-31'),
    PARTITION p2 VALUES LESS THAN ('2019-06-30'),
    PARTITION p3 VALUES LESS THAN (MAXVALUE)
)
DISTRIBUTED BY HASH(`k2`) BUCKETS 8
PROPERTIES (
    "colocate_with" = "group1",
    "replication_allocation" = "tag.location.default: 1"
);

CREATE TABLE `tbl2` (
    `k1` datetime NOT NULL COMMENT "",
    `k2` int(11) NOT NULL COMMENT "",
    `v1` double SUM NOT NULL COMMENT ""
) ENGINE=OLAP
AGGREGATE KEY(`k1`, `k2`)
DISTRIBUTED BY HASH(`k2`) BUCKETS 8
PROPERTIES (
    "colocate_with" = "group1",
    "replication_allocation" = "tag.location.default: 1"
);

explain SELECT * FROM tbl1 INNER JOIN tbl2 ON (tbl1.k2 = tbl2.k2);
SHOW PROC '/colocation_group';
ALTER TABLE tbl SET ("colocate_with" = "group2");
ALTER TABLE tbl SET ("colocate_with" = "");

show variables like '%bucket_shuffle_join%';
set enable_bucket_shuffle_join = true;

show variables like '%runtime_filter_type%'; #IN_OR_BLOOM_FILTER
set runtime_filter_type="BLOOM_FILTER,IN,MIN_MAX";
CREATE TABLE test (t1 INT) DISTRIBUTED BY HASH (t1) BUCKETS 2 PROPERTIES("replication_num" = "1");
INSERT INTO test VALUES (1), (2), (3), (4);
CREATE TABLE test2 (t2 INT) DISTRIBUTED BY HASH (t2) BUCKETS 2 PROPERTIES("replication_num" = "1");
INSERT INTO test2 VALUES (3), (4), (5);
EXPLAIN SELECT t1 FROM test JOIN test2 where test.t1 = test2.t2;

show variables like '%enable_profile%';
set enable_profile=true;
EXPLAIN SELECT t1 FROM test JOIN test2 where test.t1 = test2.t2;


show builtin functions in demo;
show full builtin  functions in demo like 'year';

select bitmap_to_string(to_bitmap(1));
select bitmap_to_string(to_bitmap(2));
select *, bitmap_to_string(to_bitmap(`user_id`)) from example_tbl;
SELECT `city`, bitmap_to_string(bitmap_union(to_bitmap(`user_id`))) FROM example_tbl GROUP BY `city`;
SELECT `city`, bitmap_union_count(to_bitmap(`user_id`)) FROM example_tbl GROUP BY `city`;

SELECT hll_cardinality(HLL_HASH(1));
SELECT hll_cardinality(HLL_HASH(10000));
SELECT hll_cardinality(hll_union(HLL_HASH(`user_id`))) from example_tbl;
SELECT COUNT(DISTINCT `user_id`) FROM example_tbl;
SELECT HLL_UNION_AGG(HLL_HASH(`user_id`)) from example_tbl;
select HLL_UNION_AGG(HLL_HASH(`user_id`)) from example_tbl group by city;

```

### doris connect mysql
```shell
docker exec -it doris-fe bash
#cd /opt/apache-doris/fe/conf
#cat fe.conf | grep mysql
#cat fe.conf | grep jdbc
mkdir /opt/apache-doris/fe/jdbc_drivers/

docker exec -it doris-be bash
#cd /opt/apache-doris/be/conf
#cat be.conf | grep mysql
#cat be.conf | grep jdbc
mkdir /opt/apache-doris/be/jdbc_drivers/

docker cp mysql-connector-j-8.0.32.jar doris-fe:/opt/apache-doris/fe/jdbc_drivers/
docker cp mysql-connector-j-8.0.32.jar doris-be:/opt/apache-doris/be/jdbc_drivers/

docker exec -it doris-fe bash
mysql -uroot -P9030 -h127.0.0.1
SHOW CATALOGS;
DROP CATALOG mysql_catalog;
CREATE CATALOG mysql_catalog PROPERTIES (
    "type"="jdbc",
    "user"="root",
    "password"="root",
    "jdbc_url" = "jdbc:mysql://mysql.dev:3306",
    "driver_url" = "mysql-connector-j-8.0.32.jar",
    "driver_class" = "com.mysql.cj.jdbc.Driver"
);

select * from mysql_catalog.demo.demo;
switch mysql_catalog;
use demo;
select * from demo;


#CREATE EXTERNAL RESOURCE `mysql_resource` PROPERTIES (
CREATE RESOURCE `mysql_resource` PROPERTIES (
    "type"="jdbc",
    "user"="root",
    "password"="root",
    "jdbc_url" = "jdbc:mysql://mysql.dev:3306/demo",
    "driver_url" = "mysql-connector-j-8.0.32.jar",
    "driver_class" = "com.mysql.cj.jdbc.Driver"
);
SHOW RESOURCES;
DROP RESOURCE 'mysql_resource';

CREATE EXTERNAL TABLE `demo_jdbc`
(
    `id`      bigint       null comment 'id',
    `comment` varchar(255) null comment '备注'
) ENGINE=JDBC
PROPERTIES (
    "resource" = "mysql_resource",
    "table_type" = "mysql",
    "table" = "demo"
);
select * from demo_jdbc;
insert into `demo_jdbc` (`comment`) values ('this is a test');

help CREATE INDEX;
```
