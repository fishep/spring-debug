# 基于docker部署mysql服务

### 目录结构
- mysql                      mysql单实例部署
- mysql-cluster              mysql集群部署，使用ndb
- mysql-replication          mysql集群部署，使用主从复制
- percona-xtradb-cluster     mysql集群部署，使用多主复制

#### mysql
```shell
cd mysql
docker compose -p grace up -d 
```

#### mysql-cluster
```shell
cd mysql-cluster
docker compose -p grace up -d 

# Run the SHOW command to print cluster status. 
docker exec -it management1 ndb_mgm
show

# 打印节点ip
docker exec -it alpine bash
for i in {1,2}; do ping -c 1 mysql$i; done | grep PING | sed -r "s/PING mysql[0-9] \((.*)\): 56 data bytes/\1/"  
```

#### mysql-replication
```shell
cd mysql-replication
docker compose -p grace up -d 

# master开启同步
docker exec -it mysql-master bash

mysql -uroot -proot
create user 'mover'@'%' identified by 'mover';
grant replication slave on *.*  to 'mover'@'%';
flush privileges;

# slave开启同步
docker exec -it mysql-slave-1 bash

mysql -uroot -proot
SHOW MASTER STATUS;
SHOW SLAVE STATUS;
CHANGE MASTER TO MASTER_HOST='mysql-master', MASTER_USER='mover', MASTER_PASSWORD='mover', MASTER_AUTO_POSITION=1, MASTER_CONNECT_RETRY=3;
#CHANGE MASTER TO MASTER_HOST='mysql-master', MASTER_USER='mover', MASTER_PASSWORD='mover', MASTER_AUTO_POSITION=0, MASTER_LOG_FILE='e4c26551355e-bin.000003' ,MASTER_LOG_POS=3402, MASTER_CONNECT_RETRY=3;
START SLAVE;
#STOP SLAVE;

```

#### percona-xtradb-cluster
```shell
cd percona-xtradb-cluster
docker compose -p grace up -d 

# 打印节点ip
docker exec -it alpine bash
for i in {1..3}; do ping -c 1 mysql-node$i; done | grep PING | sed -r "s/PING mysql-node[0-9] \((.*)\): 56 data bytes/\1/"  
```

### 数据库初始化
```shell
docker cp ../init-sql mysql:/docker-entrypoint-initdb.d/
docker exec -it mysql bash
cd /docker-entrypoint-initdb.d/init-sql && for SQL in *.sql; do mysql -uroot -p"root" < $SQL; done
```

###数据库测试
```shell
```