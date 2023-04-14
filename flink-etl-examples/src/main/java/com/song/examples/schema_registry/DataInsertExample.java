package com.song.examples.schema_registry;

import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.Record;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import io.confluent.kafka.serializers.KafkaAvroSerializer;

// https://github.com/datastax/kafka-examples/blob/master/producers/src/main/java/avro/AvroProducer.java
// https://docs.confluent.io/platform/current/schema-registry/serdes-develop/serdes-avro.html#avro-deserializer

public class DataInsertExample {

    public static void main(String[] args) {

        final String TOPIC = System.getenv("TOPIC");
        var SCHEMA_TEXT = "{\"type\":\"record\",\"name\":\"student\",\"namespace\":\"com.song.example.schema\",\"fields\":[{\"name\":\"id\",\"type\":\"int\"},{\"name\":\"name\",\"type\":\"string\"}]}";

        System.out.println(SCHEMA_TEXT);
        var schema = new Schema.Parser().parse(SCHEMA_TEXT);
        var avroRecord = new GenericData.Record(schema);
        avroRecord.put("id", 1);
        avroRecord.put("name", "Song Li");

        var key = "key-1";
        var record = new ProducerRecord<String, Record>(TOPIC, key, avroRecord);

        var kafka = Kafka();
        kafka.send(record);
        kafka.flush();
        kafka.close();

        System.out.println();
        System.out.println("Avro message published\n");
    }

    public static KafkaProducer<String, Record> Kafka() {

        final String SERVER = System.getenv("SERVER");
        final String KEY = System.getenv("KEY");
        final String SECRET = System.getenv("SECRET");
        final String SR_URL = System.getenv("SR_URL");

        final String SR_KEY = System.getenv("SR_KEY");
        final String SR_SECRET = System.getenv("SR_SECRET");

        var sasl_jaas_config = "org.apache.kafka.common.security.plain.PlainLoginModule required " +
                "username='" + KEY + "' " +
                "password='" + SECRET + "';";

        var config = new Properties();
        config.put("client.id", "test-cliet");
        config.put("bootstrap.servers", SERVER);
        config.put("security.protocol", "SASL_SSL");
        config.put("sasl.jaas.config", sasl_jaas_config);
        config.put("sasl.mechanism", "PLAIN");
        config.put("acks", "all");

        config.put("schema.registry.url", SR_URL);
        config.put("basic.auth.credentials.source", "USER_INFO");
        config.put("basic.auth.user.info", SR_KEY + ":" + SR_SECRET);
        config.put("key.serializer", StringSerializer.class);
        config.put("value.serializer", KafkaAvroSerializer.class);

        return new KafkaProducer<String, Record>(config);

    }

}
