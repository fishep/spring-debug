# 基于docker部署flink-cdc服务
> flink-cdc

### flink-cdc
````shell
# 启动flink集群

#下载地址 https://github.com/apache/flink-cdc/releases
docker cp mysql-connector-java-8.0.27.jar jobmanager:/opt/flink/lib
#docker cp mysql-connector-j-8.0.32.jar jobmanager:/opt/flink/lib
docker cp mysql-connector-java-8.0.27.jar taskmanager:/opt/flink/lib
#docker cp mysql-connector-j-8.0.32.jar taskmanager:/opt/flink/lib
docker cp flink-cdc-pipeline-connector-mysql-3.1.0.jar jobmanager:/opt/flink/lib
docker cp flink-cdc-pipeline-connector-mysql-3.1.0.jar taskmanager:/opt/flink/lib
docker cp flink-cdc-pipeline-connector-doris-3.1.0.jar jobmanager:/opt/flink/lib
docker cp flink-cdc-pipeline-connector-doris-3.1.0.jar taskmanager:/opt/flink/lib
#flink-cdc-3.1.0-bin.tar.gz/lib
#docker cp flink-cdc-dist-3.1.0.jar jobmanager:/opt/flink/lib
#docker cp flink-cdc-dist-3.1.0.jar taskmanager:/opt/flink/lib

# 启动flink集群，设置环境变量
docker exec -it jobmanager bash 
docker exec -it taskmanager bash 
env | grep FLINK_HOME
#ls -l lib/ | grep mysql
#ls -l lib/ | grep cdc
cd /opt/flink/lib
#chown flink:flink mysql-connector-j-8.0.32.jar flink-cdc-pipeline-connector-mysql-3.1.0.jar flink-cdc-pipeline-connector-doris-3.1.0.jar flink-cdc-dist-3.1.0.jar
#chmod 644 mysql-connector-j-8.0.32.jar flink-cdc-pipeline-connector-mysql-3.1.0.jar flink-cdc-pipeline-connector-doris-3.1.0.jar flink-cdc-dist-3.1.0.jar
#chown flink:flink mysql-connector-j-8.0.32.jar flink-cdc-pipeline-connector-mysql-3.1.0.jar flink-cdc-pipeline-connector-doris-3.1.0.jar
chown flink:flink mysql-connector-java-8.0.27.jar flink-cdc-pipeline-connector-mysql-3.1.0.jar flink-cdc-pipeline-connector-doris-3.1.0.jar
#chmod 644 mysql-connector-j-8.0.32.jar flink-cdc-pipeline-connector-mysql-3.1.0.jar flink-cdc-pipeline-connector-doris-3.1.0.jar
chmod 644 mysql-connector-java-8.0.27.jar flink-cdc-pipeline-connector-mysql-3.1.0.jar flink-cdc-pipeline-connector-doris-3.1.0.jar

#cd /opt/flink/lib
#chown flink:flink flink-cdc-common-3.1.0.jar
#chmod 644 flink-cdc-common-3.1.0.jar

# 部署flink cdc
docker cp flink-cdc-3.1.0-bin.tar.gz jobmanager:/opt
docker exec -it jobmanager bash 
cd /opt
tar -zxvf flink-cdc-3.1.0-bin.tar.gz
rm flink-cdc-3.1.0-bin.tar.gz
chown -R flink:flink flink-cdc-3.1.0

#docker cp flink-cdc-3.1.0-bin.tar.gz taskmanager:/opt
#docker exec -it taskmanager bash 
#cd /opt
#tar -zxvf flink-cdc-3.1.0-bin.tar.gz
#rm flink-cdc-3.1.0-bin.tar.gz
#chown -R flink:flink flink-cdc-3.1.0

重启 jobmanager taskmanager

````

### Streaming ELT from MySQL to Doris

#### Prepare records for MySQL
```shell
#使用mysql服务，镜像debezium/example-mysql:1.1，版本mysql 5.7，填充数据
```

#### Create database in Doris
```shell
#使用doris服务，镜像doris-standalone:2.1.3，版本doris2.1.3，创建数据库
create database app_db;
```

### Submit job with Flink CDC CLI
```shell
docker cp ./job jobmanager:/opt/flink-cdc-3.1.0
#docker cp ./job/mysql-to-doris.yaml jobmanager:/opt/flink-cdc-3.1.0/job

#export FLINK_CDC_HOME=/opt/flink-cdc-3.1.0
#export PATH=$PATH:$FLINK_CDC_HOME
#bash bin/flink-cdc.sh job/job.yaml
cd /opt/flink-cdc-3.1.0
cat job/mysql-to-doris.yaml
bash bin/flink-cdc.sh job/mysql-to-doris.yaml

```
