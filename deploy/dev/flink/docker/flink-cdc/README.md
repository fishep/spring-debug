# 基于docker部署flink-cdc服务
> flink-cdc

### flink-cdc
````shell
# 启动flink集群

#下载地址 https://github.com/apache/flink-cdc/releases
docker cp mysql-connector-j-8.0.32.jar jobmanager:/opt/flink/lib
docker cp mysql-connector-j-8.0.32.jar taskmanager:/opt/flink/lib
docker cp flink-cdc-pipeline-connector-mysql-3.1.1.jar jobmanager:/opt/flink/lib
docker cp flink-cdc-pipeline-connector-mysql-3.1.1.jar taskmanager:/opt/flink/lib
docker cp flink-cdc-pipeline-connector-doris-3.1.1.jar jobmanager:/opt/flink/lib
docker cp flink-cdc-pipeline-connector-doris-3.1.1.jar taskmanager:/opt/flink/lib

# 启动flink集群，设置环境变量
docker exec -it jobmanager bash 
docker exec -it taskmanager bash 
env | grep FLINK_HOME
export FLINK_HOME=/e/java/flink/flink-1.18.0
#ls -l lib/ | grep mysql
#ls -l lib/ | grep cdc
cd /opt/flink/lib
chown flink:flink mysql-connector-j-8.0.32.jar flink-cdc-pipeline-connector-mysql-3.1.1.jar flink-cdc-pipeline-connector-doris-3.1.1.jar
chmod 644 mysql-connector-j-8.0.32.jar flink-cdc-pipeline-connector-mysql-3.1.1.jar flink-cdc-pipeline-connector-doris-3.1.1.jar

# 部署flink cdc
docker cp flink-cdc-3.1.1-bin.tar.gz jobmanager:/opt
docker exec -it jobmanager bash 
cd /opt
tar -zxvf flink-cdc-3.1.1-bin.tar.gz
rm flink-cdc-3.1.1-bin.tar.gz
chown -R flink:flink flink-cdc-3.1.1

#重启 jobmanager taskmanager
docker compose -p service stop jobmanager taskmanager
docker compose -p service up -d
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

```
