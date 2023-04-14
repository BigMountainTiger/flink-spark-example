package com.song.examples.hudis3;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;
import org.apache.hudi.configuration.FlinkOptions;
import org.apache.hudi.util.HoodiePipeline;

public class hudis3Read {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        String test_name = "test-data/hudi";
        final String input = MessageFormat.format("s3a://huge-head-li-2023-glue-example/{0}/output", new Object[] { test_name });

        String targetTable = "t1";
        String basePath = input;

        Map<String, String> options = new HashMap<>();
        options.put(FlinkOptions.PATH.key(), basePath);

        HoodiePipeline.Builder builder = HoodiePipeline.builder(targetTable)
                .column("id INT")
                .column("name VARCHAR(100)")
                .pk("id")
                .options(options);

        DataStream<RowData> rowDataDataStream = builder.source(env);
        rowDataDataStream.print();

        System.out.println("Start execution");
        env.execute("Flink Streaming");

    }
}
