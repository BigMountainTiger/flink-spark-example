package com.song.examples.kafkaconfluent;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class ReadFromConfluent {

	public static void main(String[] args) throws Exception {

		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		KafkaSource<String> kafaka = kafaka();
		DataStream<String> stm = env.fromSource(kafaka, WatermarkStrategy.noWatermarks(), "Kafka Source");

		DataStream<String> stm1 = stm.map(new MapFunction<String, String>() {
			@Override
			public String map(String value) {
				return "Mapped - " + value;
			}
		});

		stm1.print();

		System.out.println("Start execution");
		env.execute("Flink Streaming Java API Skeleton");
	}

	private static KafkaSource<String> kafaka() {

		final String SERVER = System.getenv("SERVER");
		final String CLUSTER_ID = System.getenv("CLUSTER_ID");
		final String KEY = System.getenv("KEY");
		final String SECRET = System.getenv("SECRET");
		final String TOPIC = System.getenv("TOPIC");

		System.out.println(SERVER);
		System.out.println(CLUSTER_ID);
		System.out.println(KEY);
		System.out.println(SECRET);
		System.out.println(TOPIC);

		// The API Key & secret is the only thing needed to access the topic
		String sasl_jaas_config = "org.apache.kafka.common.security.plain.PlainLoginModule required " +
				"username='" + KEY + "' " +
				"password='" + SECRET + "';";

		KafkaSource<String> source = KafkaSource.<String>builder()
				.setTopics(TOPIC)
				.setProperty("bootstrap.servers", SERVER)
				.setProperty("security.protocol", "SASL_SSL")
				.setProperty("sasl.jaas.config", sasl_jaas_config)
				.setProperty("sasl.mechanism", "PLAIN")
				.setStartingOffsets(OffsetsInitializer.earliest())
				.setValueOnlyDeserializer(new SimpleStringSchema())
				.build();

		return source;
	}
}
