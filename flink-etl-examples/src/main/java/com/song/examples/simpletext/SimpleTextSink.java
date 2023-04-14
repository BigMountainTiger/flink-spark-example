package com.song.examples.simpletext;

import java.io.File;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.FileUtils;

public class SimpleTextSink {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final String input = "./test-data/plain-text/input";
        final String output = "./test-data/plain-text/output";

        final Path input_path = new Path(input);
        final Path output_path = new Path(output);

        FileUtils.deleteDirectory(new File(output));

        final FileSource<String> source = FileSource.forRecordStreamFormat(new TextLineInputFormat(), input_path)
                .build();

        final FileSink<String> sink = FileSink
                .forRowFormat(output_path, new SimpleStringEncoder<String>("UTF-8"))
                .build();

        final DataStream<String> stm = env.fromSource(source, WatermarkStrategy.noWatermarks(), "file-source");

        DataStream<String> stm1 = stm.map(new MapFunction<String, String>() {
            @Override
            public String map(String value) {
                return "Mapped - " + value;
            }
        });

        stm1.print();
        stm1.sinkTo(sink);

        System.out.println("Start execution");
        env.execute("Flink Streaming");

    }
}
