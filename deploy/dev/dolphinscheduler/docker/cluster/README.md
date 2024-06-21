# 基于docker部署dolphinscheduler服务

### dolphinscheduler
```shell
# 如果需要初始化或者升级数据库结构，需要指定profile为schema
docker-compose -p service --profile schema up -d

# 启动dolphinscheduler所有服务，指定profile为all
docker-compose -p service --profile all up -d

docker exec -it dolphinscheduler-postgresql bash
docker exec -it dolphinscheduler-zookeeper bash
docker exec -it dolphinscheduler-schema-initializer bash
docker exec -it dolphinscheduler-api bash
docker exec -it dolphinscheduler-alert bash
docker exec -it dolphinscheduler-master bash
docker exec -it dolphinscheduler-worker bash

# http://localhost:12345/dolphinscheduler/ui -- u/p: [admin/dolphinscheduler123]

docker exec -it dolphinscheduler-worker bash
#useradd -r -s /usr/sbin/nologin fishep
useradd -m -s /usr/bin/bash fishep
passwd fishep
userdel -r fishep

```

### 测试 dolphinscheduler
```shell
```

### mysql 数据源支持
```shell
#docker cp dolphinscheduler-api:/opt/dolphinscheduler/conf/common.properties ./

docker cp mysql-connector-j-8.0.32.jar dolphinscheduler-api:/opt/dolphinscheduler/libs
docker cp mysql-connector-j-8.0.32.jar dolphinscheduler-alert:/opt/dolphinscheduler/libs
docker cp mysql-connector-j-8.0.32.jar dolphinscheduler-master:/opt/dolphinscheduler/libs
docker cp mysql-connector-j-8.0.32.jar dolphinscheduler-worker:/opt/dolphinscheduler/libs

#重启
docker compose -p service stop
docker-compose -p service --profile all up -d

```

### 镜像拉取
```shell
docker pull harbor.xx.com/proxy/bitnami/postgresql:16.0.0
docker pull harbor.xx.com/proxy/bitnami/zookeeper:3.7
docker pull harbor.xx.com/proxy/apache/dolphinscheduler-tools:3.1.9
docker pull harbor.xx.com/proxy/apache/dolphinscheduler-api:3.1.9
docker pull harbor.xx.com/proxy/apache/dolphinscheduler-alert-server:3.1.9
docker pull harbor.xx.com/proxy/apache/dolphinscheduler-master:3.1.9
docker pull harbor.xx.com/proxy/apache/dolphinscheduler-worker:3.1.9

docker tag harbor.xx.com/proxy/bitnami/postgresql:16.0.0 bitnami/postgresql:16.0.0
docker tag harbor.xx.com/proxy/bitnami/zookeeper:3.7 bitnami/zookeeper:3.7
docker tag harbor.xx.com/proxy/apache/dolphinscheduler-tools:3.1.9 apache/dolphinscheduler-tools:3.1.9
docker tag harbor.xx.com/proxy/apache/dolphinscheduler-api:3.1.9 apache/dolphinscheduler-api:3.1.9
docker tag harbor.xx.com/proxy/apache/dolphinscheduler-alert-server:3.1.9 apache/dolphinscheduler-alert-server:3.1.9
docker tag harbor.xx.com/proxy/apache/dolphinscheduler-master:3.1.9 apache/dolphinscheduler-master:3.1.9
docker tag harbor.xx.com/proxy/apache/dolphinscheduler-worker:3.1.9 apache/dolphinscheduler-worker:3.1.9

docker rmi harbor.xx.com/proxy/bitnami/postgresql:16.0.0
docker rmi harbor.xx.com/proxy/bitnami/zookeeper:3.7
docker rmi harbor.xx.com/proxy/apache/dolphinscheduler-tools:3.1.9
docker rmi harbor.xx.com/proxy/apache/dolphinscheduler-api:3.1.9
docker rmi harbor.xx.com/proxy/apache/dolphinscheduler-alert-server:3.1.9
docker rmi harbor.xx.com/proxy/apache/dolphinscheduler-master:3.1.9
docker rmi harbor.xx.com/proxy/apache/dolphinscheduler-worker:3.1.9

```