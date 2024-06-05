### hadoop 管理
````shell
#export HADOOP_HOME=/opt/hadoop
#$Env:HADOOP_HOME = "/opt/hadoop"

#MAPRED-SITE.XML_yarn.app.mapreduce.am.env=HADOOP_MAPRED_HOME=$HADOOP_HOME
#MAPRED-SITE.XML_mapreduce.map.env=HADOOP_MAPRED_HOME=$HADOOP_HOME
#MAPRED-SITE.XML_mapreduce.reduce.env=HADOOP_MAPRED_HOME=$HADOOP_HOME

docker compose -p service up -d 

docker exec -it namenode bash 
yarn jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.3.6.jar pi 10 15

echo 'this is a test ' >  test.txt
hdfs dfs -put test.txt /
hadoop fs -ls /

hadoop fs --help
hadoop fs -ls /flink
hadoop fs -mkdir /flink
hadoop fs -mkdir -p /flink/completed-jobs
hadoop fs -chmod 777 /flink/completed-jobs
hadoop fs -rm -r -f /completed-jobs

#cat etc/hadoop/core-site.xml
#hdfs://namenode/flink/completed-jobs

docker exec -it datanode bash 

````