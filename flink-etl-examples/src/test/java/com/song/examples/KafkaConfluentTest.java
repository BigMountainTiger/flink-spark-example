package com.song.examples;

import org.junit.Test;
import com.song.examples.kafkaconfluent.ReadFromConfluent;
import com.song.examples.kafkaconfluent.WriteToConfluent;

public class KafkaConfluentTest {
    
    @Test
    public void ReadTest() {
        System.out.println();

        try {
            String[] args = {};
            ReadFromConfluent.main(args);
            
        } catch (Exception e) {

            e.printStackTrace();
            
            String msg = e.getMessage();
            System.out.println(msg);;
        }
    }

    @Test
    public void WriteTest() {
        System.out.println();

        try {
            String[] args = {};
            WriteToConfluent.main(args);
            
        } catch (Exception e) {

            e.printStackTrace();
            
            String msg = e.getMessage();
            System.out.println(msg);;
        }
    }
}
