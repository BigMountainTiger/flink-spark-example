package com.song.examples.kafkaconfluent;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

// https://www.conduktor.io/kafka/complete-kafka-producer-with-java/
public class WriteToConfluent {

    public static void main(String[] args) throws Exception {

        final String TOPIC = System.getenv("TOPIC");

        var kafka = Kafka();
        var record = new ProducerRecord<String, String>(TOPIC, "Sent from Java client");

        kafka.send(record);
        kafka.flush();
        kafka.close();

        System.out.println("Message sent");
        System.out.println();
    }

    private static KafkaProducer<String, String> Kafka() {

        final String SERVER = System.getenv("SERVER");
        final String KEY = System.getenv("KEY");
        final String SECRET = System.getenv("SECRET");

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

        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return new KafkaProducer<String, String>(config);

    }

}