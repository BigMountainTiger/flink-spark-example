package com.song.examples.schema_registry;

import java.util.HashMap;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class DataReadExample {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        var kafaka = kafaka();
        DataStream<GenericRecord> stm = env.fromSource(kafaka, WatermarkStrategy.noWatermarks(), "Kafka Source");

        DataStream<String> stm1 = stm.map(new MapFunction<GenericRecord, String>() {
            @Override
            public String map(GenericRecord record) {
                int id = (int)record.get("id");
                String name = record.get("name").toString();
                return "Mapped - " + id + " - " + name;
            }
        });

        stm1.print();

        System.out.println("Start execution");
        env.execute("Flink Streaming Java API Skeleton");
    }

    private static KafkaSource<GenericRecord> kafaka() {

        final String SERVER = System.getenv("SERVER");
        final String CLUSTER_ID = System.getenv("CLUSTER_ID");
        final String KEY = System.getenv("KEY");
        final String SECRET = System.getenv("SECRET");
        final String TOPIC = System.getenv("TOPIC");

        final String SR_URL = System.getenv("SR_URL");
        final String SR_KEY = System.getenv("SR_KEY");
        final String SR_SECRET = System.getenv("SR_SECRET");

        System.out.println(SERVER);
        System.out.println(CLUSTER_ID);
        System.out.println(KEY);
        System.out.println(SECRET);
        System.out.println(TOPIC);

        // The API Key & secret is the only thing needed to access the topic
        String sasl_jaas_config = "org.apache.kafka.common.security.plain.PlainLoginModule required " +
                "username='" + KEY + "' " +
                "password='" + SECRET + "';";

        var SCHEMA_TEXT = "{\"type\":\"record\",\"name\":\"student\",\"namespace\":\"com.song.example.schema\",\"fields\":[{\"name\":\"id\",\"type\":\"int\"},{\"name\":\"name\",\"type\":\"string\"}]}";
        var schema = new Schema.Parser().parse(SCHEMA_TEXT);

        var srProperties = new HashMap<String, String>();

        srProperties.put("basic.auth.credentials.source", "USER_INFO");
        srProperties.put("basic.auth.user.info", SR_KEY + ":" + SR_SECRET);

        var serializer = ConfluentRegistryAvroDeserializationSchema.forGeneric(schema, SR_URL, srProperties);

        var source = KafkaSource.<GenericRecord>builder()
                .setTopics(TOPIC)
                .setProperty("bootstrap.servers", SERVER)
                .setProperty("security.protocol", "SASL_SSL")
                .setProperty("sasl.jaas.config", sasl_jaas_config)
                .setProperty("sasl.mechanism", "PLAIN")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(serializer)
                .build();

        return source;
    }
}
