### kafka 管理
````shell
docker compose -p service up -d 

docker exec -it kafka bash

kafka-topics.sh --create --topic topic-demo --bootstrap-server localhost:9092
kafka-topics.sh --delete --topic topic-demo --bootstrap-server localhost:9092
kafka-topics.sh --alter --topic topic-demo --partitions 2 --bootstrap-server localhost:9092
kafka-topics.sh --describe --topic topic-demo --bootstrap-server localhost:9092

kafka-console-producer.sh --topic topic-demo --bootstrap-server localhost:9092
kafka-console-consumer.sh --topic topic-demo --from-beginning --bootstrap-server localhost:9092
kafka-console-consumer.sh --topic topic-demo --from-beginning --bootstrap-server localhost:9092 --group kafka-consumer-group

kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group kafka-consumer-group --describe

kafka-console-producer.sh --broker-list localhost:9092 --topic topic-demo

#-1 or latest / -2 or earliest / -3 or max-timestamp
kafka-run-class.sh org.apache.kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic topic-demo --time -1
kafka-run-class.sh org.apache.kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic topic-demo --time -2

````

###kafka demo
```shell
docker compose -p service up -d 

docker exec -it kafka bash

kafka-topics.sh --create --topic quickstart-events --bootstrap-server localhost:9092
kafka-topics.sh --describe --topic quickstart-events --bootstrap-server localhost:9092
kafka-console-producer.sh --topic quickstart-events --bootstrap-server localhost:9092
kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server localhost:9092

cat config/connect-standalone.properties
cat config/connect-file-source.properties
cat config/connect-file-sink.properties
echo "plugin.path=libs/connect-file-3.7.0.jar" >> config/connect-standalone.properties
echo -e "foo\nbar" > test.txt

connect-standalone.sh config/connect-standalone.properties config/connect-file-source.properties config/connect-file-sink.properties
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic connect-test --from-beginning
more test.sink.txt
echo Another line>> test.txt
#echo Another line from test.sink.txt>> test.sink.txt


kafka-topics.sh --list --bootstrap-server kafka:9092
kafka-console-producer.sh --producer.config /opt/bitnami/kafka/config/producer.properties --bootstrap-server kafka:9092 --topic test
kafka-console-consumer.sh --consumer.config /opt/bitnami/kafka/config/consumer.properties --bootstrap-server kafka:9092 --topic test --from-beginning


kafka-topics.sh --create --topic streams-plaintext-input --bootstrap-server localhost:9092
kafka-topics.sh --create --topic streams-pipe-output --bootstrap-server localhost:9092
kafka-topics.sh --create --topic streams-linesplit-output --bootstrap-server localhost:9092
kafka-topics.sh --create --topic streams-wordcount-output --config cleanup.policy=compact --bootstrap-server localhost:9092
kafka-topics.sh --list --bootstrap-server kafka:9092
kafka-topics.sh --bootstrap-server localhost:9092 --describe
kafka-run-class.sh org.apache.kafka.streams.examples.wordcount.WordCountDemo
kafka-console-producer.sh --producer.config /opt/bitnami/kafka/config/producer.properties --bootstrap-server kafka:9092 --topic streams-plaintext-input
kafka-console-consumer.sh --consumer.config /opt/bitnami/kafka/config/consumer.properties --bootstrap-server kafka:9092 --topic streams-pipe-output --from-beginning
kafka-console-consumer.sh --consumer.config /opt/bitnami/kafka/config/consumer.properties --bootstrap-server kafka:9092 --topic streams-linesplit-output --from-beginning
kafka-console-consumer.sh --consumer.config /opt/bitnami/kafka/config/consumer.properties --bootstrap-server kafka:9092 --topic streams-wordcount-output --from-beginning
kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic streams-wordcount-output \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
```