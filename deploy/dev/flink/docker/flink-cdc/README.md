# 基于docker部署flink-cdc服务
> flink-cdc

### flink-cdc
````shell
# 启动flink集群
cd application/
docker compose -p service up -d 

docker cp flink-cdc-3.1.1-bin.tar.gz jobmanager:/opt/flink

docker exec -it jobmanager bash
tar -zxvf flink-cdc-3.1.1-bin.tar.gz
rm flink-cdc-3.1.1-bin.tar.gz

#包冲突 flink-cdc-dist-3.1.1.jar
# rm jobmanager:/opt/flink/lib/flink-sql-connector-mysql-cdc-3.1.1.jar 

#包冲突 flink-cdc-pipeline-connector-paimon-3.1.1.jar
# rm jobmanager:/opt/flink/lib/paimon-flink-1.19-1.1-20250204.002533-45.jar
#docker cp paimon-flink-1.19-1.1-20250204.002533-45.jar jobmanager:/opt/flink/tmp
#chown flink:flink ./opt/flink/jar/paimon-flink-1.19-1.1-20250204.002533-45.jar
#chmod 644 ./opt/flink/jar/paimon-flink-1.19-1.1-20250204.002533-45.jar
# ./bin/sql-client.sh -j /opt/flink/jar/paimon-flink-1.19-1.1-20250204.002533-45.jar

docker cp flink-cdc-pipeline-connector-mysql-3.1.1.jar jobmanager:/opt/flink/flink-cdc-3.1.1/lib
docker cp flink-cdc-pipeline-connector-doris-3.1.1.jar jobmanager:/opt/flink/flink-cdc-3.1.1/lib
docker cp flink-cdc-pipeline-connector-kafka-3.1.1.jar jobmanager:/opt/flink/flink-cdc-3.1.1/lib
docker cp flink-cdc-pipeline-connector-paimon-3.1.1.jar jobmanager:/opt/flink/flink-cdc-3.1.1/lib

chown -R flink:flink flink-cdc-3.1.1

docker cp ./job jobmanager:/opt/flink/flink-cdc-3.1.1
#docker cp ./job/mysql-to-doris.yaml jobmanager:/opt/flink-cdc-3.1.1/job
#docker cp ./job/mysql-to-paimon.yaml jobmanager:/opt/flink/flink-cdc-3.1.1/job
````

### Submit job with Flink CDC CLI
```shell
docker exec -it jobmanager bash 
cd flink-cdc-3.1.1
cat job/mysql-to-doris.yaml
#bash -x bin/flink-cdc.sh job/mysql-to-doris.yaml
bash bin/flink-cdc.sh job/mysql-to-doris.yaml
#job运行后，可以同步字段的新增删除，无法同步表的新增和删除

#flink stop --savepointPath [:targetDirectory] :jobId
flink stop --savepointPath ab2b60be80aa181beaa3a3c2a5c01220
#Savepoint completed. Path: file:/opt/flink/flink-savepoints/savepoint-ab2b60-b7de720dcc92
bash bin/flink-cdc.sh -s file:/opt/flink/flink-savepoints/savepoint-ab2b60-b7de720dcc92 job/mysql-to-doris.yaml

```

### Kafka
```shell
cat job/mysql-to-kafka.yaml
bash bin/flink-cdc.sh job/mysql-to-kafka.yaml
```

### Paimon
```shell
docker exec -it jobmanager bash 
cd flink-cdc-3.1.1

chown -R flink:flink /tmp/paimon

cat job/mysql-to-paimon.yaml
#bash -x bin/flink-cdc.sh job/mysql-to-paimon.yaml
bash bin/flink-cdc.sh job/mysql-to-paimon.yaml

```
