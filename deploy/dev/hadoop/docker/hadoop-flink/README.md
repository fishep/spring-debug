### docker部署，将flink部署到hadoop容器
````shell
# 先启动hadoop服务

#下载 解压 https://www.apache.org/dyn/closer.lua/flink/flink-1.19.0/flink-1.19.0-bin-scala_2.12.tgz

#拷贝
docker cp flink-1.19.0 namenode:/opt/flink
# 拷贝测试项目到容器
docker cp flink-0.0.1-SNAPSHOT.jar namenode:/opt/flink

docker cp ./hadoop/env.sh  namenode:/etc/profile.d/
docker cp ./flink/config.yaml  namenode:/opt/flink/conf

docker exec -it --user=root namenode bash 
#env | grep HADOOP
#env | grep FLINK
chown -R hadoop /opt/flink
exit

docker exec -it namenode bash
. /etc/profile.d/env.sh

# session 模式
yarn-session.sh -nm test
cd /opt/flink/
flink run -c com.fishep.flink.job.SocketSource flink-0.0.1-SNAPSHOT.jar
flink run -c com.fishep.flink.job.CheckPointConfig flink-0.0.1-SNAPSHOT.jar

#application 模式
flink run-application -t yarn-application -c com.fishep.flink.job.SocketSource flink-0.0.1-SNAPSHOT.jar
flink list -t yarn-application -Dyarn.application.id=application_XXXX_YY
#flink list -t yarn-application -Dyarn.application.id=application_1717572546839_0005
flink cancel -t yarn-application -Dyarn.application.id=application_XXXX_YY <jobId>
#flink cancel -t yarn-application -Dyarn.application.id=application_1717572546839_0005 0375e305d9bdf74db01cc7dcca81f45d

#savepoint
flink savepoint :jobId [:targetDirectory]
#flink savepoint f236a87896223ffd2aa754d182020dd0 -yid application_1717572546839_0007
flink stop --savepointPath [:targetDirectory] :jobId
#flink stop --savepointPath hdfs://namenode:8020/flink-savepoints/savepoint-test -yid application_1717572546839_0010 66f44a35c87090f0a0f3b45187860110
flink run -s :savepointPath [:runArgs]
#flink run -s hdfs://namenode:8020/flink-checkpoints/5738ac60066d81d031db94cf93d4fe41/chk-108 -yid application_1717579427412_0002 -c com.fishep.flink.job.CheckPointConfig flink-0.0.1-SNAPSHOT.jar
#flink run-application -s hdfs://namenode:8020/flink-savepoints/savepoint-test -t yarn-application -c com.fishep.flink.job.SocketSource flink-0.0.1-SNAPSHOT.jar

````