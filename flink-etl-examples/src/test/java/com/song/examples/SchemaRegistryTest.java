package com.song.examples;

import org.junit.Test;

import com.song.examples.schema_registry.DataInsertExample;
import com.song.examples.schema_registry.DataReadExample;
import com.song.examples.schema_registry.LoadSchema;

public class SchemaRegistryTest {

    @Test
    public void LoadSchemaTest() {
        System.out.println();

        try {
            String[] args = {};
            LoadSchema.main(args);

        } catch (Exception e) {

            e.printStackTrace();

            String msg = e.getMessage();
            System.out.println(msg);
            ;
        }
    }

    @Test
    public void InsertDataTest() {
        System.out.println();

        try {
            String[] args = {};
            DataInsertExample.main(args);

        } catch (Exception e) {

            e.printStackTrace();

            String msg = e.getMessage();
            System.out.println(msg);
            ;
        }
    }

    @Test
    public void ReadDataTest() {
        System.out.println();

        try {
            String[] args = {};
            DataReadExample.main(args);

        } catch (Exception e) {

            e.printStackTrace();

            String msg = e.getMessage();
            System.out.println(msg);
            ;
        }
    }
}
