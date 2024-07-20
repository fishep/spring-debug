package com.fishep.flink.cdc;

import com.fishep.flink.cdc.converter.DateToStringConverter;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.cdc.connectors.mysql.source.MySqlSource;
import org.apache.flink.cdc.connectors.mysql.table.StartupOptions;
import org.apache.flink.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.producer.ProducerConfig;

/**
 * @Author fly.fei
 * @Date 2024/6/7 17:50
 * @Desc
 **/
public class FlinkCdcToKafka {

    private static String host = "mysql.dev";
    private static int port = 3306;
    private static String database = "demo";
    private static String table = "demo.flink_cdc_kafka_doris";
    private static String username = "demo";
    private static String password = "demo";

    private static String kafkaServers = "kafka.dev:9092";
    private static String kafkaTopic = "flink-cdc-kafka-doris";

    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        config.set(RestOptions.PORT, 8081);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);

//      设置并行度为1，或者配置checkpoint，否则无法获取增量的binlog
        env.setParallelism(1);
        env.enableCheckpointing(10000);

        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
            .hostname(host)
            .port(port)
            .username(username)
            .password(password)
            .serverTimeZone("UTC")
            .databaseList(database)
            .tableList(table)
            .startupOptions(StartupOptions.initial())
            .debeziumProperties(DateToStringConverter.DEFAULT_PROPS)
            .deserializer(new JsonDebeziumDeserializationSchema())
            .includeSchemaChanges(true)
            .build();
        DataStreamSource<String> streamSource = env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "cdc-source");

//        streamSource.print("cdc-source");

        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
            .setBootstrapServers(kafkaServers)
            .setRecordSerializer(
                KafkaRecordSerializationSchema.<String>builder()
                    .setTopic(kafkaTopic)
                    .setValueSerializationSchema(new SimpleStringSchema())
                    .build()
            )
            .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
            .setTransactionalIdPrefix("kafka_exactly_once")
            .setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 10 * 60 * 1000 + "")
            .build();

        streamSource.sinkTo(kafkaSink);

        env.execute("FlinkCdc To Kafka");
    }

//    kafka-topics.sh --list --bootstrap-server localhost:9092
//    kafka-topics.sh --create --topic flink-cdc-kafka-doris --bootstrap-server localhost:9092
//    kafka-topics.sh --delete --topic flink-cdc-kafka-doris --bootstrap-server localhost:9092
//    kafka-topics.sh --describe --topic flink-cdc-kafka-doris --bootstrap-server localhost:9092

//    kafka-console-producer.sh --topic flink-cdc-kafka-doris --bootstrap-server localhost:9092
//    kafka-console-consumer.sh --topic flink-cdc-kafka-doris --bootstrap-server localhost:9092
//    kafka-console-consumer.sh --topic flink-cdc-kafka-doris --from-beginning --bootstrap-server localhost:9092

//    kafka-run-class.sh org.apache.kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic flink-cdc-kafka-doris --time -1

}
