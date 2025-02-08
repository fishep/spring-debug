# 基于docker部署flink-cdc服务
> flink-cdc

### flink-cdc
````shell
# 启动flink集群

docker cp mysql-connector-j-8.0.32.jar jobmanager:/opt/flink/lib
docker cp mysql-connector-j-8.0.32.jar taskmanager:/opt/flink/lib

# 启动flink集群，设置环境变量
docker exec -it jobmanager bash 
docker exec -it taskmanager bash 
env | grep FLINK_HOME
#export FLINK_HOME=/e/java/flink/flink-1.19.0
#ls -l lib/ | grep mysql
#ls -l lib/ | grep cdc
chown flink:flink ./lib/mysql-connector-j-8.0.32.jar
chmod 644 ./lib/mysql-connector-j-8.0.32.jar

#重启 jobmanager taskmanager
docker compose -p service stop jobmanager taskmanager
docker compose -p service up -d

# 部署flink cdc
#下载地址 https://github.com/apache/flink-cdc/releases
docker cp flink-cdc-3.1.1-bin.tar.gz jobmanager:/opt
docker cp flink-cdc-3.1.1-bin.tar.gz taskmanager:/opt
docker exec -it jobmanager bash 
docker exec -it taskmanager bash 
cd /opt
tar -zxvf flink-cdc-3.1.1-bin.tar.gz
rm flink-cdc-3.1.1-bin.tar.gz

docker cp flink-cdc-pipeline-connector-mysql-3.1.1.jar jobmanager:/opt/flink-cdc-3.1.1/lib
docker cp flink-cdc-pipeline-connector-mysql-3.1.1.jar taskmanager:/opt/flink-cdc-3.1.1/lib
docker cp flink-cdc-pipeline-connector-doris-3.1.1.jar jobmanager:/opt/flink-cdc-3.1.1/lib
docker cp flink-cdc-pipeline-connector-doris-3.1.1.jar taskmanager:/opt/flink-cdc-3.1.1/lib
docker cp flink-cdc-pipeline-connector-kafka-3.1.1.jar jobmanager:/opt/flink-cdc-3.1.1/lib
docker cp flink-cdc-pipeline-connector-kafka-3.1.1.jar taskmanager:/opt/flink-cdc-3.1.1/lib

chown -R flink:flink flink-cdc-3.1.1
````

### Streaming ELT from MySQL to Doris

#### Prepare records for MySQL
```shell
```

#### Create database in Doris
```shell
create database demo;
```

### Submit job with Flink CDC CLI
```shell
docker cp ./job jobmanager:/opt/flink-cdc-3.1.1
#docker cp ./job/mysql-to-doris.yaml jobmanager:/opt/flink-cdc-3.1.1/job

docker exec -it jobmanager bash 
cd /opt/flink-cdc-3.1.1
cat job/mysql-to-doris.yaml
#bash -x bin/flink-cdc.sh job/mysql-to-doris.yaml
bash bin/flink-cdc.sh job/mysql-to-doris.yaml
#job运行后，可以同步字段的新增删除，无法同步表的新增和删除

#flink stop --savepointPath [:targetDirectory] :jobId
flink stop --savepointPath ab2b60be80aa181beaa3a3c2a5c01220
#Savepoint completed. Path: file:/opt/flink/flink-savepoints/savepoint-ab2b60-b7de720dcc92
bash bin/flink-cdc.sh -s file:/opt/flink/flink-savepoints/savepoint-ab2b60-b7de720dcc92 job/mysql-to-doris.yaml




cat job/mysql-to-kafka.yaml
bash bin/flink-cdc.sh job/mysql-to-kafka.yaml


```

### Paimon
```shell
cd application/
docker compose -p service up -d 

docker cp flink-cdc-3.1.1-bin.tar.gz jobmanager:/opt/flink

docker exec -it jobmanager bash
tar -zxvf flink-cdc-3.1.1-bin.tar.gz
rm flink-cdc-3.1.1-bin.tar.gz

docker cp flink-cdc-pipeline-connector-mysql-3.1.1.jar jobmanager:/opt/flink/flink-cdc-3.1.1/lib
#docker cp flink-cdc-pipeline-connector-doris-3.1.1.jar jobmanager:/opt/flink/flink-cdc-3.1.1/lib
#docker cp flink-cdc-pipeline-connector-kafka-3.1.1.jar jobmanager:/opt/flink/flink-cdc-3.1.1/lib
docker cp flink-cdc-pipeline-connector-paimon-3.1.1.jar jobmanager:/opt/flink/flink-cdc-3.1.1/lib

chown -R flink:flink flink-cdc-3.1.1

docker cp ./job jobmanager:/opt/flink/flink-cdc-3.1.1
#docker cp ./job/mysql-to-paimon.yaml jobmanager:/opt/flink/flink-cdc-3.1.1/job

docker exec -it jobmanager bash 
cd flink-cdc-3.1.1
cat job/mysql-to-paimon.yaml
#bash -x bin/flink-cdc.sh job/mysql-to-paimon.yaml
bash bin/flink-cdc.sh job/mysql-to-paimon.yaml

```
