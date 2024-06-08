package com.fishep.flink;

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
public class FlinkCdc {

    private static String host = "mysql.dev";
    private static int port = 3306;
    private static String database = "demo";
    private static String table = "demo";
    private static String username = "demo";
    private static String password = "demo";

    private static String kafkaServers = "kafka.dev:9092";
    private static String kafkaTopic = "mysql-cdc-kafka";

    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        config.set(RestOptions.PORT, 8081);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);

        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
            .hostname(host)
            .port(port)
            .username(username)
            .password(password)
            .databaseList(database)
            .tableList(table)
            .startupOptions(StartupOptions.initial())
            .deserializer(new JsonDebeziumDeserializationSchema())
            .build();
        DataStreamSource<String> streamSource = env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "cdc-source");

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

        env.execute("mysql-cdc-kafka");
    }

}
