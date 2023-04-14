package com.song.examples.kafkalocal;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class KafkaLocal {
    
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

        // The docker hosted local topic
		String topic = "song";
		String brokers = "localhost:29092";

		KafkaSource<String> source = KafkaSource.<String>builder()
				.setBootstrapServers(brokers)
				.setTopics(topic)
				.setStartingOffsets(OffsetsInitializer.latest())
				.setValueOnlyDeserializer(new SimpleStringSchema())
				.build();

		return source;
	}
}
