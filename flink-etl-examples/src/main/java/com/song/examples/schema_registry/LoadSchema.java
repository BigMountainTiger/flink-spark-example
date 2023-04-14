package com.song.examples.schema_registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoadSchema {

    public static void main(String[] args) throws IOException {

        String SR_KEY = System.getenv("SR_KEY");
        String SR_SECRET = System.getenv("SR_SECRET");
        String SR_URL = System.getenv("SR_URL");
        String SCHEMA_SUBJECT_NAME = System.getenv("SCHEMA_SUBJECT_NAME");

        String AUTH = SR_KEY + ":" + SR_SECRET;

        int version = -1;

        {
            URL url = new URL(SR_URL + "/subjects/" + SCHEMA_SUBJECT_NAME + "/versions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString(AUTH.getBytes()));

            BufferedReader response = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String txt = response.readLine();
            conn.disconnect();

            JSONArray versions = new JSONArray(txt);
            for (int i = 0; i < versions.length(); i++) {
                int temp = versions.getInt(i);
                version = (temp > version) ? temp : version;
            }

            System.out.println("Get latest version: " + version);
        }

        String schema_text = null;

        {
            URL url = new URL(SR_URL + "/subjects/" + SCHEMA_SUBJECT_NAME + "/versions/" + version);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString(AUTH.getBytes()));

            BufferedReader response = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String txt = response.readLine();
            conn.disconnect();

            JSONObject obj = new JSONObject(txt);
            schema_text = obj.getString("schema");

            System.out.println();
            System.out.println("Get schema for version: " + version);
            System.out.println(txt);
            System.out.println(schema_text);

        }

        {
            var schema = new Schema.Parser().parse(schema_text);
            System.out.println();

            List<Field> fields = schema.getFields();
            for (Field f : fields) {
                System.out.println(f.name() + " - " + f.schema().getType());
            }
        }

        System.out.println("");
        System.out.println("Completed the schema loading test");
    }
}
