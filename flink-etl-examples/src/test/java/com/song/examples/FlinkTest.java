package com.song.examples;

import org.junit.Test;

import com.song.examples.hudilocal.HudiLocalSink;
import com.song.examples.hudilocal.hudiLocalRead;
import com.song.examples.hudis3.Hudis3Sink;
import com.song.examples.hudis3.hudis3Read;
import com.song.examples.kafkalocal.KafkaLocal;
import com.song.examples.simpletext.SimpleTextSink;

public class FlinkTest {

    @Test
    public void PlainTextTest() {
        System.out.println();

        try {
            String[] args = {};
            SimpleTextSink.main(args);

        } catch (Exception e) {

            e.printStackTrace();

            String msg = e.getMessage();
            System.out.println(msg);
            ;
        }
    }

    @Test
    public void HudiLocalTest() {
        System.out.println();

        try {
            String[] args = {};
            HudiLocalSink.main(args);

            System.out.println();
            hudiLocalRead.main(args);

        } catch (Exception e) {

            e.printStackTrace();

            String msg = e.getMessage();
            System.out.println(msg);
            ;
        }
    }

    @Test
    public void HudiS3Test() {
        System.out.println();

        try {
            String[] args = {};
            Hudis3Sink.main(args);

            System.out.println();
            hudis3Read.main(args);

            System.out.println();

        } catch (Exception e) {

            e.printStackTrace();

            String msg = e.getMessage();
            System.out.println(msg);
            ;
        }
    }

    @Test
    public void KafkaLocalTest() {
        System.out.println();

        try {
            String[] args = {};
            KafkaLocal.main(args);

        } catch (Exception e) {

            e.printStackTrace();

            String msg = e.getMessage();
            System.out.println(msg);
            ;
        }
    }

}
