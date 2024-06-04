# 基于docker部署doris服务

### doris
```shell
#windows主机 找到文件 ~/.wslconfig 配置 kernelCommandLine = vsyscall=emulate

docker run -it --privileged --pid=host --name=change_count debian nsenter -t 1 -m -u -n -i sh
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

### CATALOG
```shell
CREATE CATALOG jdbc_mysql PROPERTIES (
    "type"="jdbc",
    "user"="root",
    "password"="root",
    "jdbc_url" = "jdbc:mysql://mysql:3306",
    "driver_url" = "mysql-connector-java-8.0.25.jar",
    "driver_class" = "com.mysql.cj.jdbc.Driver"
);
```
