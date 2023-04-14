package com.song.examples.hudilocal;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.GenericRowData;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.data.StringData;
import org.apache.hudi.configuration.FlinkOptions;
import org.apache.hudi.util.HoodiePipeline;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class HudiLocalSink {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        String test_name = "test-data/hudi";
        final String input = MessageFormat.format("./{0}/input", new Object[] { test_name });
        final String output = MessageFormat.format("/home/song/Sandbox/flink-spark-example/flink-etl-examples/{0}/output", new Object[] { test_name });

        // FileUtils.deleteDirectory(new File(output));

        final FileSource<String> source = FileSource
                .forRecordStreamFormat(new TextLineInputFormat(), new Path(input))
                .build();

        final DataStream<String> stm = env.fromSource(source, WatermarkStrategy.noWatermarks(), "file-source");
        final DataStream<RowData> stm1 = stm.map(new MapFunction<String, RowData>() {
            @Override
            public RowData map(String value) throws JSONException {
                GenericRowData data = new GenericRowData(2);
                JSONObject o;

                o = new JSONObject(value);

                data.setField(0, o.getInt("id"));
                data.setField(1, StringData.fromString(o.getString("name")));

                return (RowData) data;
            }
        });

        stm1.print();

        String targetTable = "t1";
        String basePath = output;

        Map<String, String> options = new HashMap<>();
        options.put(FlinkOptions.PATH.key(), basePath);

        HoodiePipeline.Builder builder = HoodiePipeline.builder(targetTable)
                .column("id INT")
                .column("name VARCHAR(100)")
                .pk("id")
                .options(options);

        builder.sink(stm1, true);

        System.out.println("Start execution");
        env.execute("Flink Streaming");

    }

}
