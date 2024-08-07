# mysql 服务搭建

### 目录结构
- init-sql   mysql需要初始化的脚本
- local      基于物理机部署
- docker     基于docker部署
- k8s        基于k8s部署

### 数据库初始化
执行 init-sql 目录的所有mysql脚步文件
```shell
cd init-sql && for SQL in *.sql; do mysql -uroot -p"root" < $SQL; done
```

### 数据库测试
```shell
mysql -udemo -pdemo
```
```mysql
USE demo;
select * from demo;
insert into `demo` (`comment`) values ('this is a demo');
update `demo` set `comment` = '这是一个测试' where `id` = 1;
delete from `demo` where `id` = 1;
```

### 参考命令
```shell
mysql < demo.sql
mysql –uroot –proot < demo.sql
mysql –uroot –proot -Ddemo < demo.sql
```
```mysql
show databases;
show tables;
select * from mysql.user;
select * from mysql.user where User = "demo";
show variables like '%character%';

CREATE DATABASE demo;
DROP DATABASE demo;

show engines;
show create table demo;
set global default_storage_engine = 'ndbcluster';

SHOW variables like '%log_bin%';
SHOW variables like 'binlog_format';
SHOW BINARY LOGS;
SHOW binlog events [IN 'log_name'] [FROM pos] [LIMIT [offset,] row_count];
SHOW binlog events;
SHOW binlog events IN 'e4c26551355e-bin.000003';
SHOW binlog events IN '774ab89ac8bd-bin.000003';
SHOW binlog events IN '774ab89ac8bd-bin.000003' LIMIT 10;

SELECT @@server_id,NOW();
select @@server_uuid,NOW();

show master status;
SHOW binlog events IN 'mysql-bin.000003';

```

### 开启 gtid_mode
```mysql
# 直接配置，重启
# gtid_mode = on
# enforce_gtid_consistency = on

# 命令行

# 主库
set global ENFORCE_GTID_CONSISTENCY = WARN;
set global ENFORCE_GTID_CONSISTENCY = ON;

set global GTID_MODE = OFF_PERMISSIVE;
set global GTID_MODE = ON_PERMISSIVE;
# 检查是否还有事务没有结束，值必须为 0
SHOW STATUS LIKE 'ONGOING_ANONYMOUS_TRANSACTION_COUNT';
show slave status;
set global GTID_MODE = ON;

show global variables like '%gtid%';

# 从库
stop slave;
CHANGE MASTER TO MASTER_AUTO_POSITION = 1;
start slave;

show variables like '%wait_timeout%';
show variables like '%timeout%';


```
