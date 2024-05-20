### flink 添加job
````shell

docker compose -p service up -d 

docker exec -it jobmanager bash 

flink run -c org.example.MyJob myFatJar.jar

````

### 启动 historyserver
```shell
#https://repo.maven.apache.org/maven2/org/apache/flink/flink-shaded-hadoop-2-uber/2.6.5-9.0/
docker cp ./flink-shaded-hadoop-2-uber-2.6.5-9.0.jar jobmanager:/opt/flink/lib
chown flink:flink flink-shaded-hadoop-2-uber-2.6.5-9.0.jar
chmod 644 flink-shaded-hadoop-2-uber-2.6.5-9.0.jar

# 配置config.yaml
#      dir: file:///opt/flink/completed-jobs
#      dir: hdfs://namenode:8020/flink/completed-jobs
jobmanager:
  archive:
    fs:
      dir: hdfs://namenode:8020/flink/completed-jobs
historyserver:
  archive:
    fs:
      refresh-interval: 10000
      dir: hdfs://namenode:8020/flink/completed-jobs

# 启动 historyserver
flink-daemon.sh start historyserver --configDir /opt/flink/conf

# flink webui  http://localhost:8081
# historyserver webui http://localhost:8082

```