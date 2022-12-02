package com.jeremy.aoc2022;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

public class Loader {

    public String getDataForDay(int day) {
        String data = loadInput(day);
        if (data == null) {
            data = downloadDay(day);
        }
        return data;
    }

    private String loadData(int day, String type) {
        String path = "day" + day + "/" + type + ".txt";
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path);
        try {
            String data = readFromInputStream(inputStream);
            return data;
        } catch (Exception e) {
            return null;
        }
    }

    public String loadInput(int day) {
        return loadData(day, "input");
    }

    public String loadTest(int day) {
        return loadData(day, "test");
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public static String downloadDay(int day) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("https://adventofcode.com/2022/day/" + day + "/input")
                    .addHeader("Cookie",
                            "session=" + System.getenv("SESSION_COOKIE"))
                    .addHeader("User-Agent", "github.com/valentine195/advent-of-code by valentine.195@gmail.com")

                    .build();
            Response response = client.newCall(request).execute();
            String contents = response.peekBody(Long.MAX_VALUE).string();

            File downloadedFile = new File(System.getenv("RESOURCE_PATH") + "/day" + day, "input.txt");
            BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
            sink.writeAll(response.body().source());
            sink.close();

            return contents;
        } catch (Exception e) {
            return null;
        }
    }
}